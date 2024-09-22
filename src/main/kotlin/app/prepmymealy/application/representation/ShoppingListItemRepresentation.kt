package app.prepmymealy.application.representation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListItemRepresentation @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("price") val price: Double,
    @JsonProperty("quantity") val quantity: Int,
    @JsonProperty("unit") val unit: String,
    @JsonProperty("origin") val origin: String,
    @JsonProperty("bought") val bought: Boolean,
){
    constructor() : this("", 0.0, 0, "", "", false)
}
