package app.prepmymealy.application.representation

data class UserRepresentation(
    val id: String,
    val isCreatingMenu: Boolean,
    val stats: UserStatsRepresentation?,
    val limits: UserLimitsRepresentation?,
)
