package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.converter.UserToUserRepresentationConverter
import app.prepmymealy.application.service.UserInitializationService
import app.prepmymealy.application.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConfig.API + AppConfig.API_VERSION + UserController.USER_PATH)
class UserController(
    private val userService: UserService,
    private val userInitializationService: UserInitializationService,
    private val converter: UserToUserRepresentationConverter
) {
    companion object {
        const val USER_PATH = "/users"
    }

    @GetMapping("/{id}", produces = ["application/json"])
    fun getUserById(@PathVariable id: String): ResponseEntity<Any> {
        userInitializationService.initializeUser(id)
        val user = userService.getUserById(id)
        return if (user.isPresent) {
            ResponseEntity.ok(converter.convert(user.get()))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}