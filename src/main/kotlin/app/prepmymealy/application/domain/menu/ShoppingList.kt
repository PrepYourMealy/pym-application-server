package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "shoppingLists")
data class ShoppingList
    @JsonCreator
    constructor(
        @JsonProperty(required = true)
        val id: String,
        @JsonProperty(required = true)
        val list: List<ShoppingListItem>,
    )
