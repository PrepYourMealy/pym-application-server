package app.prepmymealy.application_server.domain

data class User(val id: String, val stats: UserStats, val limits: UserLimits)
