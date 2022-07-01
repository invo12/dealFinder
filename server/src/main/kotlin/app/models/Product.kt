package app.models

import java.time.LocalDate

data class Product(
    val url: String = "",
    val name: String = "",
    val oldPrice: Double = 0.0,
    val price: Double = 0.0,
    val currency: String = "",
    val timestamp: LocalDate = LocalDate.now()
)