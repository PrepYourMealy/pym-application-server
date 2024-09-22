package app.prepmymealy.application.extractor

import app.prepmymealy.application.controller.payload.ShoppingListItemPayload
import app.prepmymealy.application.controller.payload.ShoppingListPayload
import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.menu.ShoppingListItem
import org.springframework.stereotype.Component

@Component
class ShoppingListExtractor : Extractor<ShoppingListPayload, ShoppingList> {
    override fun extract(input: ShoppingListPayload, id: String): ShoppingList {
        return ShoppingList(
            id = id,
            list = input.items.map { convertShoppingListItem(it) },
        )
    }

    private fun convertShoppingListItem(item: ShoppingListItemPayload): ShoppingListItem {
        return ShoppingListItem(
            name = item.name,
            quantity = item.quantity,
            unit = item.unit,
            price = item.price,
            origin = item.origin,
            bought = item.bought,
        )
    }
}