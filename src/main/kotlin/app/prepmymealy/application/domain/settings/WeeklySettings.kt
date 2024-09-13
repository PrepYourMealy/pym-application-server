package app.prepmymealy.application.domain.settings

data class WeeklySettings(
    val monday: DailySettings? = null,
    val tuesday: DailySettings? = null,
    val wednesday: DailySettings? = null,
    val thursday: DailySettings? = null,
    val friday: DailySettings? = null,
    val saturday: DailySettings? = null,
    val sunday: DailySettings? = null,
) {
    fun toPrompt(): String {
        val mondayPrompt = if (monday != null) "Montag: ${monday.toPrompt()}" else ""
        val tuesdayPrompt = if (tuesday != null) "Dienstag: ${tuesday.toPrompt()}" else ""
        val wednesdayPrompt = if (wednesday != null) "Mittwoch: ${wednesday.toPrompt()}" else ""
        val thursdayPrompt = if (thursday != null) "Donnerstag: ${thursday.toPrompt()}" else ""
        val fridayPrompt = if (friday != null) "Freitag: ${friday.toPrompt()}" else ""
        val saturdayPrompt = if (saturday != null) "Samstag: ${saturday.toPrompt()}" else ""
        val sundayPrompt = if (sunday != null) "Sonntag: ${sunday.toPrompt()}" else ""
        return "$mondayPrompt $tuesdayPrompt $wednesdayPrompt $thursdayPrompt $fridayPrompt $saturdayPrompt $sundayPrompt"
    }
}
