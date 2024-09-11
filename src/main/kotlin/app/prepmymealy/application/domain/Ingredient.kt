package app.prepmymealy.application.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class Ingredient
    @JsonCreator
    constructor(
        val name: String,
        val quantity: Int,
        val unit: String,
    )
