package app.prepmymealy.application.controller

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.service.DiscountService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConfig.API_VERSION + DiscountController.DISCOUNTS_PATH)
class DiscountController(private val discountService: DiscountService) {
    companion object {
        const val DISCOUNTS_PATH = "/discounts"
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllDiscounts(): ResponseEntity<Any> {
        val discounts = discountService.getAllDiscounts()
        return ResponseEntity.ok(discounts)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun removeAndInsertDiscounts(
        @RequestBody discounts: List<Discount>,
    ): ResponseEntity<Any> {
        discountService.removeAndInsertDiscounts(discounts)
        return ResponseEntity.ok().build()
    }
}
