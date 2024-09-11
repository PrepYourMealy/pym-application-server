package app.prepmymealy.application.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class ShoppingListItem
    @JsonCreator
    constructor(
        val name: String,
        val price: Double,
        val quantity: Int,
        val unit: String,
        val origin: String,
    )
