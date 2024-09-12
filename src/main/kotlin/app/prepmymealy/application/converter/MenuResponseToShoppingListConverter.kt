package app.prepmymealy.application.converter

import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.domain.menu.ShoppingList
import org.springframework.stereotype.Component

@Component
class MenuResponseToShoppingListConverter {
    fun convert(
        response: MenuResponse,
        userId: String,
    ): ShoppingList {
        return ShoppingList(id = userId, list = response.list!!) // It is safe to use !! here because the list is never null
    }
}
