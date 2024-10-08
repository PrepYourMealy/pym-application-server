package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.controller.payload.RecreateMenuPrompt
import app.prepmymealy.application.converter.MenuToMenuRepresentationConverter
import app.prepmymealy.application.representation.ApiErrorRepresentation
import app.prepmymealy.application.service.MenuCreationService
import app.prepmymealy.application.service.MenuService
import app.prepmymealy.application.service.UserInitializationService
import app.prepmymealy.application.service.UserPromptValidationService
import app.prepmymealy.application.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConfig.API + AppConfig.API_VERSION + MenuController.MENU_PATH)
class MenuController(
    private val menuCreationService: MenuCreationService,
    private val userInitializationService: UserInitializationService,
    private val userService: UserService,
    private val menuService: MenuService,
    private val userPromptValidationService: UserPromptValidationService,
    private val converter: MenuToMenuRepresentationConverter,
) {
    companion object {
        const val MENU_PATH = "/menus"
    }

    @PutMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recreateMenu(
        @PathVariable id: String,
    ): ResponseEntity<Any> {
        userInitializationService.initializeUser(id)
        if (!userService.isUserAllowedToRecreateMenu(id)) {
            val apiError =
                ApiErrorRepresentation(
                    message = "User not allowed to recreate menu: $id",
                    code = 403,
                )
            return ResponseEntity.status(403).body(apiError)
        }
        menuCreationService.recreateMenuForUser(id)
        return ResponseEntity.ok().build()
    }



    @PutMapping("/{id}/prompt", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recreateWithPrompt(@PathVariable id: String,
                           @RequestBody body: RecreateMenuPrompt): ResponseEntity<Any> {
        userInitializationService.initializeUser(id)
        if (!userService.isUserAllowedToRecreateMenu(id)) {
            val apiError =
                ApiErrorRepresentation(
                    message = "User not allowed to recreate menu: $id",
                    code = 403,
                )
            ResponseEntity.status(403).body(apiError)
        }
        if (!userPromptValidationService.validateUserPrompt(body.msg)) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Invalid prompt or prompt too long",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        menuCreationService.recreateMenuForUser(id, body.msg)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getMenu(
        @PathVariable id: String,
    ): ResponseEntity<Any> {
        val menu = menuService.getMenuById(id)
        return if (menu.isPresent) {
            ResponseEntity.ok(converter.convert(menu.get()))
        } else {
            val apiError =
                ApiErrorRepresentation(
                    message = "Menu not found for user: $id",
                    code = 404,
                )
            ResponseEntity.status(404).body(apiError)
        }
    }
}
