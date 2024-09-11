package app.prepmymealy.application.domain.menu

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Recipe
    @JsonCreator
    constructor(
        @JsonProperty(required = true)
        val name: String,
        @JsonProperty(required = true)
        val description: String,
        @JsonProperty(required = true)
        val ingredients: List<Ingredient>,
        @JsonProperty(required = true)
        val steps: List<Step>,
        @JsonProperty(required = true)
        val tags: List<String>,
        @JsonProperty(required = true)
        val prepTime: Int,
        @JsonProperty(required = true)
        val cookTime: Int,
        @JsonProperty(required = true)
        val servings: Int,
    )
