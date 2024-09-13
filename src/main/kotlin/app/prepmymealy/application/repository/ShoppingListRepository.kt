package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.menu.ShoppingList
import org.springframework.data.mongodb.repository.MongoRepository

interface ShoppingListRepository : MongoRepository<ShoppingList, String>
