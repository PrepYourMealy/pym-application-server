package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonProperty

data class DayMenu(
    @JsonProperty(required = true)
    val breakfast: Recipe? = null,
    @JsonProperty(required = true)
    val lunch: Recipe? = null,
    @JsonProperty(required = true)
    val dinner: Recipe? = null,
)
