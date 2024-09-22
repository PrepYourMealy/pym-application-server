package app.prepmymealy.application.service

import app.prepmymealy.application.ai.model.MenuGenerationModel
import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.converter.MenuResponseToMenuConverter
import app.prepmymealy.application.converter.MenuResponseToShoppingListConverter
import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.domain.user.User
import java.util.Optional
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.util.function.Tuples

@Service
class MenuCreationService(
    private val settingsService: SettingsService,
    private val menuService: MenuService,
    private val userService: UserService,
    private val shoppingListService: ShoppingListService,
    private val menuGenerationModel: MenuGenerationModel,
    private val menuConverter: MenuResponseToMenuConverter,
    private val listConverter: MenuResponseToShoppingListConverter,
) {
    @Scheduled(cron = "0 0 3 * * Mon")
    fun createAllUserMenus() {
        // to seed with the current discounts
        settingsService.getAllSettingsAsStream()
            .parallel()
            .map {
                val menuResponse = menuGenerationModel.generateMenu(it)
                val list = listConverter.convert(menuResponse, it.id)
                val menu = menuConverter.convert(menuResponse, it.id)
                Tuples.of(menu, list)
            }.forEach {
                menuService.updateUserMenu(it.t1)
                shoppingListService.updateShoppingList(it.t2)
            }
    }

    @Async
    fun recreateMenuForUser(userId: String) {
        val validationResult = validateUser(userId)
        if (validationResult.isEmpty) {
            return
        }
        val (user, settings) = validationResult.get()
        val menuResponse = menuGenerationModel.generateMenu(settings)
        updateEntities(menuResponse, user)
    }

    @Async
    fun recreateMenuForUser(userId: String, userPrompt: String) {
        val validationResult = validateUser(userId)
        if (validationResult.isEmpty) {
            return
        }
        val (user, settings) = validationResult.get()
        val existingMenu = menuService.getMenuById(userId)
        val menuResponse = menuGenerationModel.recreateMenuWithPrompt(userPrompt, settings, existingMenu.orElse(null))
        updateEntities(menuResponse, user)
    }

    private fun updateEntities(menuResponse: MenuResponse, user: User) {
        val list = listConverter.convert(menuResponse, user.id)
        val menu = menuConverter.convert(menuResponse, user.id)
        menuService.updateUserMenu(menu)
        shoppingListService.updateShoppingList(list)
        userService.incrementUserRegenerateRequestAndSave(user)
    }

    private fun validateUser(userId: String) : Optional<Pair<User, Settings>> {
        val user = userService.getUserById(userId)
        if (user.isEmpty) {
            return Optional.empty()
        }
        if (!userService.isUserAllowedToRecreateMenu(user.get())) {
            return Optional.empty()
        }
        val settings = settingsService.getSettingsById(userId)
        if (settings.isEmpty) {
            return Optional.empty()
        }
        return Optional.of(Pair(user.get(), settings.get()))
    }
}
