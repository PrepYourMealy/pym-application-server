package app.prepmymealy.application.representation

data class ShoppingListItemRepresentation(
    val name: String,
    val price: Double,
    val quantity: Int,
    val unit: String,
    val origin: String,
    val bought: Boolean,
)
