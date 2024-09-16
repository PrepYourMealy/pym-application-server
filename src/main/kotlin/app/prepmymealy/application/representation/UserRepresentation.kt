package app.prepmymealy.application.representation

data class UserRepresentation(
    val id: String,
    val stats: UserStatsRepresentation?,
    val limits: UserLimitsRepresentation?,
)
