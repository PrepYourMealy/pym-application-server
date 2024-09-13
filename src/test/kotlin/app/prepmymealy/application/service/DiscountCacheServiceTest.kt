package app.prepmymealy.application.service

import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.repository.DiscountRepository
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.testng.annotations.Test

class DiscountCacheServiceTest {
    private val discountRepository: DiscountRepository = mock()

    private val discounts: List<Discount> = mock()

    private val discountCacheService = DiscountCacheService(discountRepository)

    @Test
    fun `should load cache when empty`() {
        // given
        whenever(discountRepository.findAll()).thenReturn(discounts)
        // when
        val cachedDiscounts = discountCacheService.getCachedDiscounts()
        // then
        assertEquals(cachedDiscounts, discounts)
    }
}
