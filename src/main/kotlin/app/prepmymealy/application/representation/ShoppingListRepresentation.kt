package app.prepmymealy.application.representation

import com.fasterxml.jackson.annotation.JsonCreator

data class ShoppingListRepresentation @JsonCreator constructor(
    val total: Double,
    val items: List<ShoppingListItemRepresentation>,
)
