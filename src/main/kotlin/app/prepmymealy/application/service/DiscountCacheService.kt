package app.prepmymealy.application.service

import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.repository.DiscountRepository
import org.springframework.stereotype.Service

@Service
class DiscountCacheService(
    private val discountRepository: DiscountRepository,
) {
    private var discountCache: List<Discount> = emptyList()

    fun getCachedDiscounts(): List<Discount> {
        if (discountCache.isEmpty()) {
            discountCache = discountRepository.findAll()
        }
        return discountCache
    }

    fun reloadCache() {
        discountCache = discountRepository.findAll()
    }
}
