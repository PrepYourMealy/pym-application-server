package app.prepmymealy.application.representation

data class DayMenuRepresentation(
    val breakfast: RecipeRepresentation? = null,
    val lunch: RecipeRepresentation? = null,
    val dinner: RecipeRepresentation? = null,
)
