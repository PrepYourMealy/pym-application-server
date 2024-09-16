package app.prepmymealy.application.converter

import app.prepmymealy.application.domain.user.User
import app.prepmymealy.application.domain.user.UserLimits
import app.prepmymealy.application.domain.user.UserStats
import app.prepmymealy.application.representation.UserLimitsRepresentation
import app.prepmymealy.application.representation.UserRepresentation
import app.prepmymealy.application.representation.UserStatsRepresentation
import org.springframework.stereotype.Component

@Component
class UserToUserRepresentationConverter : Converter<User, UserRepresentation> {
    override fun convert(input: User): UserRepresentation {
        return UserRepresentation(
            id = input.id,
            stats = convertStats(input.stats),
            limits = convertLimits(input.limits)
        )
    }

    private fun convertStats(stats: UserStats?): UserStatsRepresentation? {
        return stats?.let {
            UserStatsRepresentation(
                weeklyRegenerateRequest = it.weeklyRegenerateRequest
            )
        }
    }

    private fun convertLimits(limits: UserLimits?): UserLimitsRepresentation? {
        return limits?.let {
            UserLimitsRepresentation(
                regenerateRequestsPerWeek = it.regenerateRequestsPerWeek
            )
        }
    }


}