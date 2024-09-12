package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.stream.Stream

interface UserRepository : MongoRepository<User, String> {
    @Query("{}")
    fun findAllUsersAsStream(): Stream<User>
}
