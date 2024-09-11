package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "menus")
data class Menu
    @JsonCreator
    constructor(
        @Id
        @JsonProperty(required = true)
        val id: String,
        @JsonProperty(required = true)
        val mon: DayMenu? = null,
        @JsonProperty(required = true)
        val tue: DayMenu? = null,
        @JsonProperty(required = true)
        val wed: DayMenu? = null,
        @JsonProperty(required = true)
        val thu: DayMenu? = null,
        @JsonProperty(required = true)
        val fri: DayMenu? = null,
        @JsonProperty(required = true)
        val sat: DayMenu? = null,
        @JsonProperty(required = true)
        val sun: DayMenu? = null,
        @JsonProperty(required = true)
        val list: List<ShoppingListItem>,
    )
