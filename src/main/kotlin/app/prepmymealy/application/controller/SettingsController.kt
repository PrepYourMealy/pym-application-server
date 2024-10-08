package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.converter.SettingsToSettingsRepresentationConverter
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.representation.ApiErrorRepresentation
import app.prepmymealy.application.service.SettingsService
import app.prepmymealy.application.service.SettingsUpdateService
import app.prepmymealy.application.service.SettingsValidationService
import app.prepmymealy.application.service.UserInitializationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Objects

@RestController
@RequestMapping(AppConfig.API + AppConfig.API_VERSION + SettingsController.SETTINGS_PATH)
class SettingsController(
    private val settingsService: SettingsService,
    private val converter: SettingsToSettingsRepresentationConverter,
    private val userInitializationService: UserInitializationService,
    private val settingsUpdateService: SettingsUpdateService,
    private val validationService: SettingsValidationService,
) {
    companion object {
        const val SETTINGS_PATH = "/settings"
    }

    @GetMapping("/{id}")
    fun getSettingsForId(
        @PathVariable("id") id: String,
    ): ResponseEntity<Any> {
        val settings = settingsService.getSettingsById(id)
        return if (settings.isPresent) {
            val settingsRepresentation = converter.convert(settings.get())
            ResponseEntity.ok(settingsRepresentation)
        } else {
            val apiError =
                ApiErrorRepresentation(
                    message = "Settings not found for id: $id",
                    code = 404,
                )
            ResponseEntity.status(404).body(apiError)
        }
    }

    @PostMapping("/init/{id}")
    fun initSettings(@PathVariable id: String): ResponseEntity<Any> {
        userInitializationService.initializeUser(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createSettings(
        @RequestBody settings: Settings?,
    ): ResponseEntity<Any> {
        if (settings == null) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Settings cannot be null",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        val errorMap = validationService.validateSettings(settings)
        if (errorMap.isPresent) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Validation error",
                    code = 400,
                    errors = errorMap.get(),
                )
            return ResponseEntity.status(400).body(apiError)
        }
        val result = settingsUpdateService.createSettings(settings)
        if (!result) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Settings already created",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateSettings(
        @PathVariable("id") id: String,
        @RequestBody settings: Settings?,
    ): ResponseEntity<Any> {
        if (settings == null) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Settings cannot be null",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        if (!Objects.equals(settings.id, id)) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Identifier did not match",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        val errorMap = validationService.validateSettings(settings)
        if (errorMap.isPresent) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Validation error",
                    code = 400,
                    errors = errorMap.get(),
                )
            return ResponseEntity.status(400).body(apiError)
        }
        val result = settingsUpdateService.updateSettings(settings)
        if (!result) {
            val apiError =
                ApiErrorRepresentation(
                    message = "Settings do not exist need to be created first",
                    code = 400,
                )
            return ResponseEntity.status(400).body(apiError)
        }
        return ResponseEntity.ok().build()
    }
}
