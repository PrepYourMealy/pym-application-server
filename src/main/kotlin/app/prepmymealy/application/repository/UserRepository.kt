package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.user.User
import java.util.stream.Stream
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository : MongoRepository<User, String> {

    @Query("{}")
    fun findAllUsersAsStream(): Stream<User>

}
