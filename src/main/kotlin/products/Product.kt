package products

import java.time.LocalDate

data class Product(
    val url: String,
    val name: String,
    val oldPrice: Double,
    val price: Double,
    val currency: String,
    val timestamp: LocalDate
)