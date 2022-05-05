package app.services.caches

import app.models.Product
import app.repositories.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CacheServiceImpl: CacheService {

    @Autowired
    private lateinit var productRepoImpl: ProductRepo

    override fun getProducts(offset: Int, limit: Int): List<Product> {

        return productRepoImpl.getProducts(offset, limit)
    }
}