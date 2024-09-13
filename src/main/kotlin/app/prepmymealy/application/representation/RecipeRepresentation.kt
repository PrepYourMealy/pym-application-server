package app.prepmymealy.application.representation

data class RecipeRepresentation(
    val name: String,
    val description: String,
    val ingredients: List<IngredientRepresentation>,
    val steps: List<StepRepresentation>,
    val tags: List<String>,
    val prepTime: Int,
    val cookTime: Int,
    val servings: Int,
)
