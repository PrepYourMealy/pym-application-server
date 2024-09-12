package app.prepmymealy.application.service

import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.repository.ShoppingListRepository
import org.springframework.stereotype.Service

@Service
class ShoppingListService(private val shoppingListRepository: ShoppingListRepository) {
    fun updateShoppingList(shoppingList: ShoppingList) = shoppingListRepository.save(shoppingList)
}
