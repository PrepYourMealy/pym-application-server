package app.prepmymealy.application.converter

import app.prepmymealy.application.domain.menu.DayMenu
import app.prepmymealy.application.domain.menu.Ingredient
import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.domain.menu.Recipe
import app.prepmymealy.application.domain.menu.Step
import app.prepmymealy.application.representation.DayMenuRepresentation
import app.prepmymealy.application.representation.IngredientRepresentation
import app.prepmymealy.application.representation.MenuRepresentation
import app.prepmymealy.application.representation.RecipeRepresentation
import app.prepmymealy.application.representation.StepRepresentation
import org.springframework.stereotype.Component

@Component
class MenuToMenuRepresentationConverter : Converter<Menu, MenuRepresentation> {
    override fun convert(input: Menu): MenuRepresentation {
        return MenuRepresentation(
            mon = convertDayMenu(input.mon),
            tue = convertDayMenu(input.tue),
            wed = convertDayMenu(input.wed),
            thu = convertDayMenu(input.thu),
            fri = convertDayMenu(input.fri),
            sat = convertDayMenu(input.sat),
            sun = convertDayMenu(input.sun),
        )
    }

    private fun convertDayMenu(dayMenu: DayMenu?): DayMenuRepresentation? {
        return dayMenu?.let {
            DayMenuRepresentation(
                breakfast = dayMenu.breakfast?.let { convertRecipe(it) },
                lunch = dayMenu.lunch?.let { convertRecipe(it) },
                dinner = dayMenu.dinner?.let { convertRecipe(it) },
            )
        }
    }

    private fun convertRecipe(recipe: Recipe): RecipeRepresentation {
        return RecipeRepresentation(
            name = recipe.name,
            description = recipe.description,
            ingredients = recipe.ingredients.map { convertIngredient(it) },
            steps = recipe.steps.map { convertStep(it) },
            tags = recipe.tags,
            prepTime = recipe.prepTime,
            cookTime = recipe.cookTime,
            servings = recipe.servings,
        )
    }

    private fun convertStep(step: Step): StepRepresentation {
        return StepRepresentation(
            description = step.description,
            duration = step.durationInMin,
        )
    }

    private fun convertIngredient(ingredient: Ingredient): IngredientRepresentation {
        return IngredientRepresentation(
            name = ingredient.name,
            quantity = ingredient.quantity,
            unit = ingredient.unit,
        )
    }
}
