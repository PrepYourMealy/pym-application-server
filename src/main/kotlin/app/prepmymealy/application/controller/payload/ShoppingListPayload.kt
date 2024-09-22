package app.prepmymealy.application.controller.payload

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingListPayload @JsonCreator constructor(
    @JsonProperty("items") var items : List<ShoppingListItemPayload>,
)
