package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.settings.Settings
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.stream.Stream

interface SettingsRepository : MongoRepository<Settings, String> {
    @Query("{}")
    fun findAllSettingsAsStream(): Stream<Settings>
}
