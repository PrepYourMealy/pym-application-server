package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Recipe
    @JsonCreator
    constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("ingredients") val ingredients: List<Ingredient>,
        @JsonProperty("steps") val steps: List<Step>,
        @JsonProperty("tags") val tags: List<String>,
        @JsonProperty("prepTime") val prepTime: Int,
        @JsonProperty("cookTime") val cookTime: Int,
        @JsonProperty("servings") val servings: Int,
    )
