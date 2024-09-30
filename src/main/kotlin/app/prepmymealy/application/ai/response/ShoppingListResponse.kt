package app.prepmymealy.application.ai.response

import app.prepmymealy.application.domain.menu.ShoppingListItem
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListResponse @JsonCreator constructor(
    @JsonProperty("list") val list: List<ShoppingListItem>? = null
)