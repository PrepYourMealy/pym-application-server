package app.prepmymealy.application.ai.response

import app.prepmymealy.application.domain.menu.DayMenu
import app.prepmymealy.application.domain.menu.ShoppingListItem
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class MenuResponse @JsonCreator constructor(
    @JsonProperty("mon") val mon: DayMenu? = null,
    @JsonProperty("tue") val tue: DayMenu? = null,
    @JsonProperty("wed") val wed: DayMenu? = null,
    @JsonProperty("thu") val thu: DayMenu? = null,
    @JsonProperty("fri") val fri: DayMenu? = null,
    @JsonProperty("sat") val sat: DayMenu? = null,
    @JsonProperty("sun") val sun: DayMenu? = null,
    @JsonProperty("list") val list: List<ShoppingListItem>? = null,
)
