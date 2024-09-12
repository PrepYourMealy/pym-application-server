package app.prepmymealy.application.service

import app.prepmymealy.application.ai.model.MenuGenerationModel
import app.prepmymealy.application.converter.MenuResponseToMenuConverter
import app.prepmymealy.application.converter.MenuResponseToShoppingListConverter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.util.function.Tuples

@Service
class MenuCreationService(private val settingsService: SettingsService,
                          private val menuService: MenuService,
                          private val shoppingListService: ShoppingListService,
                          private val menuCreationModel: MenuGenerationModel,
                          private val menuConverter: MenuResponseToMenuConverter,
                          private val listConverter: MenuResponseToShoppingListConverter) {


    fun recreateMenuForUser(userId: String) {
        val settings = settingsService.getSettingsById(userId)
        if (settings.isEmpty) {
            return
        }
        val menuResponse = menuCreationModel.generateMenu(settings.get())
        val list = listConverter.convert(menuResponse, userId)
        val menu = menuConverter.convert(menuResponse, userId)
        menuService.updateUserMenu(menu)
        shoppingListService.updateShoppingList(list)
    }

    @Scheduled(cron = "0 0 10 * * Mon")
    fun createAllUserMenus() {
        // to seed with the current discounts
        menuCreationModel.reloadDiscountCache()
        settingsService.getAllSettingsAsStream()
            .parallel()
            .map {
                val menuResponse = menuCreationModel.generateMenu(it)
                val list = listConverter.convert(menuResponse, it.id)
                val menu = menuConverter.convert(menuResponse, it.id)
                Tuples.of(menu, list)
            }.forEach {
                menuService.updateUserMenu(it.t1)
                shoppingListService.updateShoppingList(it.t2)
            }
    }
}