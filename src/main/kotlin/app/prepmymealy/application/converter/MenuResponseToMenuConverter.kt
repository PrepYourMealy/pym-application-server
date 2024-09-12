package app.prepmymealy.application.converter

import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.domain.menu.Menu
import org.springframework.stereotype.Component

@Component
class MenuResponseToMenuConverter {

    fun convert(response: MenuResponse, userId: String): Menu {
        return Menu(
            id = userId,
            mon = response.mon,
            tue = response.tue,
            wed = response.wed,
            thu = response.thu,
            fri = response.fri,
            sat = response.sat,
            sun = response.sun
        )
    }
}