package app.prepmymealy.application.representation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListRepresentation @JsonCreator constructor(
    @JsonProperty("total") val total: Double,
    @JsonProperty("items") val items: List<ShoppingListItemRepresentation>,
)
