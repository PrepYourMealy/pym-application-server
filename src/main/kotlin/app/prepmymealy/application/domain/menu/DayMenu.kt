package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DayMenu @JsonCreator constructor(
    @JsonProperty("breakfast") val breakfast: Recipe? = null,
    @JsonProperty("lunch") val lunch: Recipe? = null,
    @JsonProperty("dinner") val dinner: Recipe? = null,
)
