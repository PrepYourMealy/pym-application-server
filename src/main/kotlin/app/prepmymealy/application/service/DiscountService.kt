package app.prepmymealy.application.service

import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.repository.DiscountRepository
import org.springframework.stereotype.Service

@Service
class DiscountService(private val discountRepository: DiscountRepository) {
    fun removeAndInsertDiscounts(discounts: List<Discount>) {
        discountRepository.deleteAll()
        discountRepository.saveAll(discounts)
    }

    fun getAllDiscounts(): MutableList<Discount> = discountRepository.findAll()
}
