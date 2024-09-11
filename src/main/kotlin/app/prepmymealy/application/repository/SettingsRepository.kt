package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.settings.Settings
import org.springframework.data.mongodb.repository.MongoRepository

interface SettingsRepository : MongoRepository<Settings, String>
