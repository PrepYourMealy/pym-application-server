package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Step
    @JsonCreator
    constructor(
        @JsonProperty(required = true)
        val description: String,
        @JsonProperty(required = true)
        val durationInMin: Int,
    )
