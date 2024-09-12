package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Ingredient
    @JsonCreator
    constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("quantity") val quantity: Int,
        @JsonProperty("unit") val unit: String,
    )
