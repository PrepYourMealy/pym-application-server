package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.settings.Settings
import java.util.stream.Stream
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface SettingsRepository : MongoRepository<Settings, String> {
    @Query("{}")
    fun findAllSettingsAsStream(): Stream<Settings>
}
