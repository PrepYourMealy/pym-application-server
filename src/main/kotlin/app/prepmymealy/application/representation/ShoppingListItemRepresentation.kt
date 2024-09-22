package app.prepmymealy.application.representation

import com.fasterxml.jackson.annotation.JsonCreator

data class ShoppingListItemRepresentation @JsonCreator constructor(
    val name: String,
    val price: Double,
    val quantity: Int,
    val unit: String,
    val origin: String,
    val bought: Boolean,
)
