package app.prepmymealy.application_server.representation

data class ApiErrorRepresentation(val code: Int, val message: String, val errors: Map<String, String>? = null)
