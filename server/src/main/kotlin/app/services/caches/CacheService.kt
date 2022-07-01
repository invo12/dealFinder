package app.services.caches

import app.models.Product

interface CacheService {

    fun getProducts(offset: Int = 0, limit: Int = 10): List<Product>
    fun getProduct(url: String): Product
}