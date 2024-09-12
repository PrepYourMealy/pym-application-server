package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListItem
    @JsonCreator
    constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("price") val price: Double,
        @JsonProperty("quantity") val quantity: Int,
        @JsonProperty("unit") val unit: String,
        @JsonProperty("origin") val origin: String,
    )
