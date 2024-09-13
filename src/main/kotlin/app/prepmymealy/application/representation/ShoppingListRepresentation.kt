package app.prepmymealy.application.representation

data class ShoppingListRepresentation(
    val total: Double,
    val items: List<ShoppingListItemRepresentation>,
)
