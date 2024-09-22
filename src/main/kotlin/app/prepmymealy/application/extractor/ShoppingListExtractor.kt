package app.prepmymealy.application.extractor

import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.menu.ShoppingListItem
import app.prepmymealy.application.representation.ShoppingListItemRepresentation
import app.prepmymealy.application.representation.ShoppingListRepresentation
import org.springframework.stereotype.Component

@Component
class ShoppingListExtractor : Extractor<ShoppingListRepresentation, ShoppingList> {
    override fun extract(input: ShoppingListRepresentation, id: String): ShoppingList {
        return ShoppingList(
            id = id,
            list = input.items.map { convertShoppingListItem(it) },
        )
    }

    private fun convertShoppingListItem(item: ShoppingListItemRepresentation): ShoppingListItem {
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