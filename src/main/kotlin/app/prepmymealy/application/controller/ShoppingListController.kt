package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.converter.ShoppingListToShoppingListRepresentationConverter
import app.prepmymealy.application.representation.ApiErrorRepresentation
import app.prepmymealy.application.service.ShoppingListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConfig.API + AppConfig.API_VERSION + ShoppingListController.LIST_PATH)
class ShoppingListController(
    private val shoppingListService: ShoppingListService,
    private val converter: ShoppingListToShoppingListRepresentationConverter,
) {
    companion object {
        const val LIST_PATH = "/lists"
    }

    @GetMapping("/{id}", produces = ["application/json"])
    fun getShoppingList(
        @PathVariable id: String,
    ): ResponseEntity<Any> {
        val shoppingList = shoppingListService.getShoppingListById(id)
        return if (shoppingList.isPresent) {
            ResponseEntity.ok(converter.convert(shoppingList.get()))
        } else {
            val apiError =
                ApiErrorRepresentation(
                    message = "List not found for user: $id",
                    code = 404,
                )
            ResponseEntity.status(404).body(apiError)
        }
    }
}
