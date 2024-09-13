package app.prepmymealy.application.converter

import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.menu.ShoppingListItem
import app.prepmymealy.application.representation.ShoppingListItemRepresentation
import app.prepmymealy.application.representation.ShoppingListRepresentation
import org.springframework.stereotype.Component

@Component
class ShoppingListToShoppingListRepresentationConverter : Converter<ShoppingList, ShoppingListRepresentation> {
    override fun convert(input: ShoppingList): ShoppingListRepresentation {
        return ShoppingListRepresentation(
            total = input.list.sumOf { it.price },
            items = input.list.map { convertShoppingListItem(it) },
        )
    }

    private fun convertShoppingListItem(item: ShoppingListItem): ShoppingListItemRepresentation {
        return ShoppingListItemRepresentation(
            name = item.name,
            quantity = item.quantity,
            unit = item.unit,
            price = item.price,
            origin = item.origin,
        )
    }
}
