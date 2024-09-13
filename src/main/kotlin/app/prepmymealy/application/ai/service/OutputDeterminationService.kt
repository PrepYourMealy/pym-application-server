package app.prepmymealy.application.ai.service

import app.prepmymealy.application.domain.settings.DailySettings
import app.prepmymealy.application.domain.settings.Settings
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Service

@Service
class OutputDeterminationService {
    companion object {
        private val DAY_KEYS = listOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")
    }

    fun generateSchemaForSettings(settings: Settings): JSONObject {
        return generateMenuSchema(settings)
    }

    private fun generateMenuSchema(settings: Settings): JSONObject {
        val menuSchema = JSONObject()
        val definitions = JSONObject()
        definitions.put("recipe", generateRecipeSchema())
        menuSchema.put("definitions", definitions)
        menuSchema.put("type", "object")
        val properties = JSONObject().put("list", generateShoppingListItemsSchema())
        settings.overallWeeklySettings?.let {
            for (dayKey in DAY_KEYS) {
                properties.put(dayKey, generateDayMenuSchema(it.overall))
            }
        }
        settings.weeklySettings?.let {
            properties.put("mon", generateDayMenuSchema(it.monday))
            properties.put("tue", generateDayMenuSchema(it.tuesday))
            properties.put("wed", generateDayMenuSchema(it.wednesday))
            properties.put("thu", generateDayMenuSchema(it.thursday))
            properties.put("fri", generateDayMenuSchema(it.friday))
            properties.put("sat", generateDayMenuSchema(it.saturday))
            properties.put("sun", generateDayMenuSchema(it.sunday))
        }
        menuSchema.put("properties", properties)
        menuSchema.put("required", getRequiredKeys(properties))
        menuSchema.put("additionalProperties", false)
        return menuSchema
    }

    private fun generateDayMenuSchema(settings: DailySettings?): JSONObject {
        val dayMenuSchema = JSONObject()
        dayMenuSchema.put("type", "object")
        val properties = JSONObject()
        settings?.breakfast?.let {
            if (it.isActive) {
                properties.put("breakfast", generateRecipeRef())
            } else {
                properties.remove("breakfast")
            }
        }
        settings?.lunch?.let {
            if (it.isActive) {
                properties.put("lunch", generateRecipeRef())
            } else {
                properties.remove("lunch")
            }
        }
        settings?.dinner?.let {
            if (it.isActive) {
                properties.put("dinner", generateRecipeRef())
            } else {
                properties.remove("dinner")
            }
        }
        dayMenuSchema.put("properties", properties)
        dayMenuSchema.put("required", getRequiredKeys(properties))
        dayMenuSchema.put("additionalProperties", false)
        return dayMenuSchema
    }

    private fun generateRecipeSchema(): JSONObject {
        val recipeSchema = JSONObject()
        recipeSchema.put("type", "object")
        val properties = JSONObject()
        properties
            .put(
                "name",
                JSONObject()
                    .put("type", "string"),
            )
            .put(
                "description",
                JSONObject()
                    .put("type", "string"),
            )
            .put("ingredients", generateIngredientsSchema())
            .put("steps", generateStepsSchema())
            .put(
                "tags",
                JSONObject()
                    .put("type", "array")
                    .put(
                        "items",
                        JSONObject()
                            .put("type", "string"),
                    ),
            )
            .put(
                "prepTime",
                JSONObject()
                    .put("type", "integer"),
            )
            .put(
                "cookTime",
                JSONObject()
                    .put("type", "integer"),
            )
            .put(
                "servings",
                JSONObject()
                    .put("type", "integer"),
            )
        recipeSchema.put("properties", properties)
        recipeSchema.put("required", getRequiredKeys(properties))
        recipeSchema.put("additionalProperties", false)
        return recipeSchema
    }

    private fun generateIngredientsSchema(): JSONObject {
        val ingredientsSchema = JSONObject()
        ingredientsSchema.put("type", "array")
        ingredientsSchema.put("items", generateIngredientSchema())
        return ingredientsSchema
    }

    private fun generateIngredientSchema(): JSONObject {
        val ingredientSchema = JSONObject()
        ingredientSchema.put("type", "object")
        val properties =
            JSONObject()
                .put(
                    "name",
                    JSONObject()
                        .put("type", "string"),
                )
                .put(
                    "quantity",
                    JSONObject()
                        .put("type", "integer"),
                )
                .put(
                    "unit",
                    JSONObject()
                        .put("type", "string"),
                )
        ingredientSchema.put("properties", properties)
        ingredientSchema.put("required", getRequiredKeys(properties))
        ingredientSchema.put("additionalProperties", false)
        return ingredientSchema
    }

    private fun generateStepsSchema(): JSONObject {
        val stepsSchema = JSONObject()
        stepsSchema.put("type", "array")
        stepsSchema.put("items", generateStepSchema())
        return stepsSchema
    }

    private fun generateStepSchema(): JSONObject {
        val stepSchema = JSONObject()
        stepSchema.put("type", "object")
        val properties =
            JSONObject()
                .put(
                    "description",
                    JSONObject()
                        .put("type", "string"),
                )
                .put(
                    "durationInMin",
                    JSONObject()
                        .put("type", "integer"),
                )
        stepSchema.put("properties", properties)
        stepSchema.put("required", getRequiredKeys(properties))
        stepSchema.put("additionalProperties", false)
        return stepSchema
    }

    private fun generateShoppingListItemsSchema(): JSONObject {
        val shoppingListItemsSchema = JSONObject()
        shoppingListItemsSchema.put("type", "array")
        shoppingListItemsSchema.put("items", generateShoppingListItemSchema())
        return shoppingListItemsSchema
    }

    private fun generateShoppingListItemSchema(): JSONObject {
        val shoppingListItemSchema = JSONObject()
        shoppingListItemSchema.put("type", "object")
        val properties =
            JSONObject()
                .put(
                    "name",
                    JSONObject()
                        .put("type", "string"),
                )
                .put(
                    "price",
                    JSONObject()
                        .put("type", "number"),
                )
                .put(
                    "quantity",
                    JSONObject()
                        .put("type", "integer"),
                )
                .put(
                    "unit",
                    JSONObject()
                        .put("type", "string"),
                )
                .put(
                    "origin",
                    JSONObject()
                        .put("type", "string"),
                )
        shoppingListItemSchema.put("properties", properties)
        shoppingListItemSchema.put("required", getRequiredKeys(properties))
        shoppingListItemSchema.put("additionalProperties", false)
        return shoppingListItemSchema
    }

    private fun generateRecipeRef(): JSONObject {
        val recipeRef = JSONObject()
        recipeRef.put("\$ref", "#/definitions/recipe")
        return recipeRef
    }

    private fun getRequiredKeys(properties: JSONObject): JSONArray = JSONArray(properties.keys().asSequence().toList())
}
