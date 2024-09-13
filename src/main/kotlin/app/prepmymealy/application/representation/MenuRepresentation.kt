package app.prepmymealy.application.representation

data class MenuRepresentation(
    val mon: DayMenuRepresentation? = null,
    val tue: DayMenuRepresentation? = null,
    val wed: DayMenuRepresentation? = null,
    val thu: DayMenuRepresentation? = null,
    val fri: DayMenuRepresentation? = null,
    val sat: DayMenuRepresentation? = null,
    val sun: DayMenuRepresentation? = null,
)
