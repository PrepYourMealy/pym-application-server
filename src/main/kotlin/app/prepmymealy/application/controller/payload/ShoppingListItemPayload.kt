package app.prepmymealy.application.controller.payload

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListItemPayload @JsonCreator constructor(
    @JsonProperty("name") var name: String,
    @JsonProperty("price") var price: Double,
    @JsonProperty("quantity") var quantity: Int,
    @JsonProperty("unit") var unit: String,
    @JsonProperty("origin") var origin: String,
    @JsonProperty("bought") var bought: Boolean,
)
