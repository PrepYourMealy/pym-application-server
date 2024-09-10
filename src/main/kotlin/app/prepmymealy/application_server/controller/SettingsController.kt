package app.prepmymealy.application_server.controller

import app.prepmymealy.application_server.converter.SettingsToSettingsRepresentationConverter
import app.prepmymealy.application_server.representation.ApiErrorRepresentation
import app.prepmymealy.application_server.service.SettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/settings")
class SettingsController(
    private val settingsService: SettingsService,
    private val converter: SettingsToSettingsRepresentationConverter
) {

    @GetMapping("/{id}")
    fun getSettingsForId(@PathVariable("id") id: String): ResponseEntity<Any> {
        val settings = settingsService.getSettingsById(id)
        return if (settings.isPresent) {
            val settingsRepresentation = converter.convert(settings.get())
            ResponseEntity.ok(settingsRepresentation)
        } else {
            val apiError = ApiErrorRepresentation(
                message = "Settings not found for id: $id",
                code = 404
            )
            ResponseEntity.status(404).body(apiError)
        }
    }
}
