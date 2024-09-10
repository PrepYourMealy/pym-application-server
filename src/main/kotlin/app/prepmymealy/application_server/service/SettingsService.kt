package app.prepmymealy.application_server.service

import app.prepmymealy.application_server.domain.Settings
import app.prepmymealy.application_server.repository.SettingsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SettingsService(private val settingsRepository: SettingsRepository) {

    fun getSettingsById(id: String): Optional<Settings> {
        return settingsRepository.findById(id)
    }
}