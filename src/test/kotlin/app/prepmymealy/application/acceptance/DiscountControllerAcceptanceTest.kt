package app.prepmymealy.application.acceptance

import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.repository.DiscountRepository
import app.prepmymealy.application.service.DiscountCacheService
import app.prepmymealy.application.testsupport.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class DiscountControllerAcceptanceTest : AbstractSpringTest() {
    @Autowired
    private lateinit var discountCacheService: DiscountCacheService

    @Autowired
    private lateinit var discountRepository: DiscountRepository

    @BeforeMethod
    fun setUp() {
        discountRepository.deleteAll()
        discountCacheService.reloadCache()
    }

    @Test
    fun `should return empty list when no discounts are available`() {
        // given
        // when
        val response = api.getDiscounts()

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo("[]")
    }

    @Test
    fun `should return discounts`() {
        // given
        val discount = discountRepository.save(Discount(name = "discount1", price = "10"))

        // when
        val response = api.getDiscounts()

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(
            """
            [{"id":"${discount.id}","name":"discount1","price":"10"}]
            """.trimIndent(),
        )
    }

    @Test
    fun `should remove old discounts and add new ones`() {
        // given
        discountRepository.save(Discount(name = "oldDiscount", price = "5"))

        val discounts =
            listOf(
                Discount(name = "discount1", price = "10"),
                Discount(name = "discount2", price = "20"),
            )

        // when
        val response = api.postDiscounts(discounts)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(discountRepository.findAll().size).isEqualTo(2)
    }
}
