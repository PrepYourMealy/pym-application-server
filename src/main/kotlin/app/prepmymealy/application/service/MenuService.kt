package app.prepmymealy.application.service

import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.repository.MenuRepository
import org.springframework.stereotype.Service

@Service
class MenuService(private val menuRepository: MenuRepository) {
    fun updateUserMenu(menu: Menu) = menuRepository.save(menu)
}