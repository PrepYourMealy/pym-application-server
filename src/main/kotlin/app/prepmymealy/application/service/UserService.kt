package app.prepmymealy.application.service

import app.prepmymealy.application.domain.user.User
import app.prepmymealy.application.domain.user.UserStats
import app.prepmymealy.application.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {


    fun getUserById(id: String) = userRepository.findById(id)

    fun isUserAllowedToRecreateMenu(id: String) : Boolean {
        val user = userRepository.findById(id)
        if (user.isEmpty) {
            return false // TODO add something here to init user
        }
        return isUserAllowedToRecreateMenu(user.get())
    }

    fun incrementUserRegenerateRequestAndSave(user: User) {
        val builder = user.toBuilder()
        if (user.stats == null) {
            builder.stats(UserStats.builder()
                .weeklyRegenerateRequest(1)
                .build())
        } else {
            builder.stats(
                user.stats!!.toBuilder()
                .weeklyRegenerateRequest(user.stats!!.weeklyRegenerateRequest + 1)
                .build())
        }
        userRepository.save(builder.build())
    }

    fun isUserAllowedToRecreateMenu(user: User) : Boolean {
        if (user.limits == null || user.stats == null) {
            return false
        }
        // TODO: maybe add default here as well
        return user.limits!!.regenerateRequestsPerWeek > user.stats!!.weeklyRegenerateRequest
    }

}