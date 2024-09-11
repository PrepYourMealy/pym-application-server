package app.prepmymealy.application.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class Recipe
    @JsonCreator
    constructor(
        val name: String,
        val description: String,
        val ingredients: List<Ingredient>,
        val steps: List<Step>,
        val tags: List<String>,
        val prepTime: Int,
        val cookTime: Int,
        val servings: Int,
    )
