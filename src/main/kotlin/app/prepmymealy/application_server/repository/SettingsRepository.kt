package app.prepmymealy.application_server.repository

import app.prepmymealy.application_server.domain.Settings
import org.springframework.data.mongodb.repository.MongoRepository

interface SettingsRepository : MongoRepository<Settings, String> {
}