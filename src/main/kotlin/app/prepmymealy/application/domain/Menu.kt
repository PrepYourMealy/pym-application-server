package app.prepmymealy.application.domain

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "menus")
data class Menu
    @JsonCreator
    constructor(
        @Id
        val id: String,
        val mon: DayMenu? = null,
        val tue: DayMenu? = null,
        val wed: DayMenu? = null,
        val thu: DayMenu? = null,
        val fri: DayMenu? = null,
        val sat: DayMenu? = null,
        val sun: DayMenu? = null,
    )
