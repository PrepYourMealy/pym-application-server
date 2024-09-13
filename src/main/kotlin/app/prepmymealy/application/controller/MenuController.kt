package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.converter.MenuToMenuRepresentationConverter
import app.prepmymealy.application.representation.ApiErrorRepresentation
import app.prepmymealy.application.service.MenuCreationService
import app.prepmymealy.application.service.MenuService
import app.prepmymealy.application.service.UserInitializationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConfig.API + AppConfig.API_VERSION + MenuController.MENU_PATH)
class MenuController(
    private val menuCreationService: MenuCreationService,
    private val userInitializationService: UserInitializationService,
    private val menuService: MenuService,
    private val converter: MenuToMenuRepresentationConverter,
) {
    companion object {
        const val MENU_PATH = "/menus"
    }

    @PutMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recreateMenu(
        @PathVariable id: String,
    ): ResponseEntity<Any> {
        userInitializationService.initializeUser(id) // TODO: maybe make this better
        val result = menuCreationService.recreateMenuForUser(id)
        if (result) {
            return ResponseEntity.ok().build()
        }
        val apiError =
            ApiErrorRepresentation(
                message = "Limit exceeded for user: $id",
                code = 403,
            )
        return ResponseEntity.status(403).body(apiError)
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
