package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListItem
    @JsonCreator
    constructor(
        @JsonProperty(required = true)
        val name: String,
        @JsonProperty(required = true)
        val price: Double,
        @JsonProperty(required = true)
        val quantity: Int,
        @JsonProperty(required = true)
        val unit: String,
        @JsonProperty(required = true)
        val origin: String,
    )
