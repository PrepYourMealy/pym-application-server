package app.prepmymealy.application.domain

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
)
