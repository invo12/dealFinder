package app.services.caches

import app.dto.ProductDTO

interface CacheService {

    fun getProducts(offset: Int = 0, limit: Int = 10, id: Long): List<ProductDTO>
    fun getProduct(url: String, id: Long): ProductDTO

    fun removeProduct(url: String, id: Long): Boolean
}