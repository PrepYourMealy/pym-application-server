package app.prepmymealy.application.service

import app.prepmymealy.application.ai.model.MenuGenerationModel
import app.prepmymealy.application.ai.model.ShoppingListGenerationModel
import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.ai.response.ShoppingListResponse
import app.prepmymealy.application.converter.MenuResponseToMenuConverter
import app.prepmymealy.application.converter.MenuResponseToShoppingListConverter
import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.domain.user.User
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.Optional
import org.mockito.kotlin.times

class MenuCreationServiceTest {
    private val settingsService: SettingsService = mock()
    private val menuService: MenuService = mock()
    private val userService: UserService = mock()
    private val shoppingListService: ShoppingListService = mock()
    private val menuGenerationModel: MenuGenerationModel = mock()
    private val menuConverter: MenuResponseToMenuConverter = mock()
    private val listConverter: MenuResponseToShoppingListConverter = mock()
    private val shoppingListGenerationModel: ShoppingListGenerationModel = mock()

    private val user: User = mock()
    private val menuResponse: MenuResponse = mock()
    private val shoppingListResponse: ShoppingListResponse = mock()
    private val shoppingList: ShoppingList = mock()
    private val menu: Menu = mock()
    private val settings: Settings = mock()

    @BeforeMethod
    fun setUp() {
        reset(settingsService, menuService, userService, shoppingListService, menuGenerationModel, menuConverter, listConverter, shoppingListGenerationModel, shoppingListResponse)
    }

    private val menuCreationService =
        MenuCreationService(
            settingsService = settingsService,
            menuService = menuService,
            userService = userService,
            shoppingListService = shoppingListService,
            menuGenerationModel = menuGenerationModel,
            menuConverter = menuConverter,
            listConverter = listConverter,
            shoppingListGenerationModel = shoppingListGenerationModel,
        )

    @Test
    fun `should not regenerate menu for user when user not found`() {
        // given
        val userId = "someUserId"

        whenever(userService.getUserById(userId)).thenReturn(Optional.empty())

        // when
        menuCreationService.recreateMenuForUser(userId)

        // then
        verify(userService).getUserById(userId)
        verifyNoMoreInteractions(userService, menuService, shoppingListService)
    }

    @Test
    fun `should not regenerate menu for user when threshold exceeded`() {
        // given
        val userId = "someUserId"

        whenever(userService.getUserById(userId)).thenReturn(Optional.of(user))
        whenever(userService.isUserAllowedToRecreateMenu(user)).thenReturn(false)

        // when
        menuCreationService.recreateMenuForUser(userId)

        // then
        verify(userService).getUserById(userId)
        verify(userService).isUserAllowedToRecreateMenu(user)
        verifyNoMoreInteractions(userService, menuService, shoppingListService)
    }

    @Test
    fun `should not regenerate menu for user when settings not found`() {
        // given
        val userId = "someUserId"
        whenever(userService.getUserById(userId)).thenReturn(Optional.of(user))
        whenever(userService.isUserAllowedToRecreateMenu(user)).thenReturn(true)
        whenever(settingsService.getSettingsById(userId)).thenReturn(Optional.empty())

        // when
        menuCreationService.recreateMenuForUser(userId)

        // then
        verify(userService).getUserById(userId)
        verify(userService).isUserAllowedToRecreateMenu(user)
        verify(settingsService).getSettingsById(userId)
        verifyNoMoreInteractions(userService, menuService, shoppingListService, settingsService)
    }

    @Test
    fun `should regenerate menu for user and store the new menu and the new shopping list`() {
        // given
        val userId = "someUserId"
        whenever(userService.getUserById(userId)).thenReturn(Optional.of(user))
        whenever(userService.isUserAllowedToRecreateMenu(user)).thenReturn(true)
        whenever(settingsService.getSettingsById(userId)).thenReturn(Optional.of(settings))
        whenever(menuGenerationModel.generateMenu(settings)).thenReturn(menuResponse)
        whenever(shoppingListGenerationModel.generateShoppingList(menu)).thenReturn(shoppingListResponse)
        whenever(listConverter.convert(shoppingListResponse, userId)).thenReturn(shoppingList)
        whenever(menuConverter.convert(menuResponse, userId)).thenReturn(menu)
        whenever(user.id).thenReturn(userId)

        // when
        menuCreationService.recreateMenuForUser(userId)

        // then
        verify(userService).getUserById(userId)
        verify(userService).isUserAllowedToRecreateMenu(user)
        verify(settingsService).getSettingsById(userId)
        verify(menuGenerationModel).generateMenu(settings)
        verify(shoppingListGenerationModel).generateShoppingList(menu)
        verify(listConverter).convert(shoppingListResponse, userId)
        verify(menuConverter).convert(menuResponse, userId)
        verify(menuService).updateUserMenu(menu)
        verify(shoppingListService).updateShoppingList(shoppingList)
        verify(userService).incrementUserRegenerateRequestAndSave(user)
        verify(user, times(2)).id
        verifyNoMoreInteractions(
            userService,
            menuService,
            shoppingListService,
            settingsService,
            menuGenerationModel,
            shoppingListResponse,
            shoppingListGenerationModel,
            listConverter,
            menuConverter,
        )
    }

    @Test
    fun `should create menus for all users cron`() {
        // given
        whenever(settingsService.getAllSettingsAsStream()).thenReturn(listOf(settings).stream())
        whenever(menuGenerationModel.generateMenu(settings)).thenReturn(menuResponse)
        whenever(shoppingListGenerationModel.generateShoppingList(menu)).thenReturn(shoppingListResponse)
        whenever(listConverter.convert(shoppingListResponse, settings.id)).thenReturn(shoppingList)
        whenever(menuConverter.convert(menuResponse, settings.id)).thenReturn(menu)

        // when
        menuCreationService.createAllUserMenus()

        // then
        verify(settingsService).getAllSettingsAsStream()
        verify(menuGenerationModel).generateMenu(settings)
        verify(shoppingListGenerationModel).generateShoppingList(menu)
        verify(listConverter).convert(shoppingListResponse, settings.id)
        verify(menuConverter).convert(menuResponse, settings.id)
        verify(menuService).updateUserMenu(menu)
        verify(shoppingListService).updateShoppingList(shoppingList)
    }
}
