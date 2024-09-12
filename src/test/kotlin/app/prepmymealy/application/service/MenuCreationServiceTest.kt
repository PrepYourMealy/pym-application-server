package app.prepmymealy.application.service

import app.prepmymealy.application.ai.model.MenuGenerationModel
import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.converter.MenuResponseToMenuConverter
import app.prepmymealy.application.converter.MenuResponseToShoppingListConverter
import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class MenuCreationServiceTest {
    private val settingsService: SettingsService = mock()
    private val menuService: MenuService = mock()
    private val userService: UserService = mock()
    private val shoppingListService: ShoppingListService = mock()
    private val menuGenerationModel: MenuGenerationModel = mock()
    private val menuConverter: MenuResponseToMenuConverter = mock()
    private val listConverter: MenuResponseToShoppingListConverter = mock()

    private val user: User = mock()
    private val menuResponse: MenuResponse = mock()
    private val shoppingList: ShoppingList = mock()
    private val menu: Menu = mock()
    private val settings: Settings = mock()

    private val menuCreationService =
        MenuCreationService(
            settingsService = settingsService,
            menuService = menuService,
            userService = userService,
            shoppingListService = shoppingListService,
            menuGenerationModel = menuGenerationModel,
            menuConverter = menuConverter,
            listConverter = listConverter,
        )

    @Test
    fun `should not regenerate menu for user when user not found`() {
        // given
        val userId = "someUserId"

        whenever(userService.getUserById(userId)).thenReturn(Optional.empty())

        // when
        val result = menuCreationService.recreateMenuForUser(userId)

        // then
        assertThat(result).isFalse()
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
        val result = menuCreationService.recreateMenuForUser(userId)

        // then
        assertThat(result).isFalse()
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
        val result = menuCreationService.recreateMenuForUser(userId)

        // then
        assertThat(result).isFalse()
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
        whenever(listConverter.convert(menuResponse, userId)).thenReturn(shoppingList)
        whenever(menuConverter.convert(menuResponse, userId)).thenReturn(menu)

        // when
        val result = menuCreationService.recreateMenuForUser(userId)

        // then
        assertThat(result).isTrue()
        verify(userService).getUserById(userId)
        verify(userService).isUserAllowedToRecreateMenu(user)
        verify(settingsService).getSettingsById(userId)
        verify(menuGenerationModel).generateMenu(settings)
        verify(listConverter).convert(menuResponse, userId)
        verify(menuConverter).convert(menuResponse, userId)
        verify(menuService).updateUserMenu(menu)
        verify(shoppingListService).updateShoppingList(shoppingList)
        verify(userService).incrementUserRegenerateRequestAndSave(user)
        verifyNoMoreInteractions(
            userService,
            menuService,
            shoppingListService,
            settingsService,
            menuGenerationModel,
            listConverter,
            menuConverter,
        )
    }

    @Test
    fun `should create menus for all users cron`() {
        // given
        whenever(settingsService.getAllSettingsAsStream()).thenReturn(listOf(settings).stream())
        whenever(menuGenerationModel.generateMenu(settings)).thenReturn(menuResponse)
        whenever(listConverter.convert(menuResponse, settings.id)).thenReturn(shoppingList)
        whenever(menuConverter.convert(menuResponse, settings.id)).thenReturn(menu)

        // when
        menuCreationService.createAllUserMenus()

        // then
        verify(menuGenerationModel).reloadDiscountCache()
        verify(settingsService).getAllSettingsAsStream()
        verify(menuGenerationModel).generateMenu(settings)
        verify(listConverter).convert(menuResponse, settings.id)
        verify(menuConverter).convert(menuResponse, settings.id)
        verify(menuService).updateUserMenu(menu)
        verify(shoppingListService).updateShoppingList(shoppingList)
    }
}
