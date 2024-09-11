package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.menu.Menu
import org.springframework.data.mongodb.repository.MongoRepository

interface MenuRepository : MongoRepository<Menu, String>
