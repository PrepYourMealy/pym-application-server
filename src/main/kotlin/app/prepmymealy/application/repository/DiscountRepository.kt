package app.prepmymealy.application.repository

import app.prepmymealy.application.domain.discount.Discount
import org.springframework.data.mongodb.repository.MongoRepository

interface DiscountRepository : MongoRepository<Discount, String>
