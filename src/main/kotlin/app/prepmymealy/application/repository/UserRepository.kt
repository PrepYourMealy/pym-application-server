package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>
