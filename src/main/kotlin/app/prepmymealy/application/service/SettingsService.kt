package app.prepmymealy.application.service

import app.prepmymealy.application.repository.SettingsRepository
import org.springframework.stereotype.Service

@Service
class SettingsService(private val settingsRepository: SettingsRepository) {
    fun getSettingsById(id: String) = settingsRepository.findById(id)

    fun getAllSettingsAsStream() = settingsRepository.findAllSettingsAsStream()
}
