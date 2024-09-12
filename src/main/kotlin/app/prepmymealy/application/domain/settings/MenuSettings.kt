package app.prepmymealy.application.domain.settings

data class MenuSettings(
    val isActive: Boolean = true,
    val people: Int = 2,
) {
    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() = Builder(isActive, people)

    data class Builder(
        var isActive: Boolean = true,
        var people: Int = 2
    ) {
        fun isActive(isActive: Boolean) = apply { this.isActive = isActive }
        fun people(people: Int) = apply { this.people = people }
        fun build() = MenuSettings(isActive, people)
    }
}
