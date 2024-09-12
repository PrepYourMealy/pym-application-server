package app.prepmymealy.application.domain.discount

import app.prepmymealy.application.domain.menu.DataOrigin
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "discounts")
data class Discount(
    @Id
    val id: String? = null,
    val img: String? = null,
    val name: String? = null,
    val description: String? = null,
    val price: String? = null,
    val originalPrice: String? = null,
    val discount: String? = null,
    val packaging: String? = null,
    val availability: String? = null,
    val dataOrigin: DataOrigin? = null,
) {
    companion object {
        fun builder() = Builder()
    }

    fun toBuilder() =
        Builder(
            id,
            img,
            name,
            description,
            price,
            originalPrice,
            discount,
            packaging,
            availability,
            dataOrigin,
        )

    data class Builder(
        var id: String? = null,
        var img: String? = null,
        var name: String? = null,
        var description: String? = null,
        var price: String? = null,
        var originalPrice: String? = null,
        var discount: String? = null,
        var packaging: String? = null,
        var availability: String? = null,
        var dataOrigin: DataOrigin? = null,
    ) {
        fun id(id: String) = apply { this.id = id }

        fun img(img: String) = apply { this.img = img }

        fun name(name: String) = apply { this.name = name }

        fun description(description: String) = apply { this.description = description }

        fun price(price: String) = apply { this.price = price }

        fun originalPrice(originalPrice: String) = apply { this.originalPrice = originalPrice }

        fun discount(discount: String) = apply { this.discount = discount }

        fun packaging(packaging: String) = apply { this.packaging = packaging }

        fun availability(availability: String) = apply { this.availability = availability }

        fun dataOrigin(dataOrigin: DataOrigin) = apply { this.dataOrigin = dataOrigin }

        fun build() = Discount(id, img, name, description, price, originalPrice, discount, packaging, availability, dataOrigin)
    }
}
