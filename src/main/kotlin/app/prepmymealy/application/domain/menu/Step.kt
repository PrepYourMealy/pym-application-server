package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Step
@JsonCreator
constructor(
    @JsonProperty("description") val description: String,
    @JsonProperty("durationInMin") val durationInMin: Int,
)
