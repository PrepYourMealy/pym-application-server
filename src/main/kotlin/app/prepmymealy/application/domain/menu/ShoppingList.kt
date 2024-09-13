package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "shoppingLists")
data class ShoppingList
    @JsonCreator
    constructor(
        val id: String,
        val list: List<ShoppingListItem>,
    )
