package products

data class Product(
    val url: String,
    val name: String,
    val oldPrice: Double,
    val price: Double,
    val currency: String
)