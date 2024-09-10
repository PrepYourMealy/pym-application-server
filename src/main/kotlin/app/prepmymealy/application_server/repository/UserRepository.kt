package app.prepmymealy.application_server.repository

import app.prepmymealy.application_server.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
}