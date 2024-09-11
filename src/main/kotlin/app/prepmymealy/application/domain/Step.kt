package app.prepmymealy.application.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class Step
    @JsonCreator
    constructor(
        val description: String,
        val durationInMin: Int,
    )
