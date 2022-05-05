package app.services.caches

import app.models.Product
import app.repositories.ProductRepo
import app.services.crawlers.CrawlerService
import app.services.crawlers.ThomannCrawlerServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CacheServiceImpl : CacheService {

    @Autowired
    private lateinit var productRepoImpl: ProductRepo

    @Autowired
    private lateinit var thomannCrawlerServiceImpl: CrawlerService

    override fun getProducts(offset: Int, limit: Int): List<Product> = runBlocking {

        val now = LocalDate.now()
        val products = productRepoImpl.getProducts(offset, limit)

        products.map {
            async(Dispatchers.Default) {
                if (now != it.timestamp) {
                    val product = thomannCrawlerServiceImpl.getProduct(it.url)
                    productRepoImpl.updateProduct(it, product)
                    product
                } else {
                    it
                }
            }
        }.map { it.await() }.toList()
    }
}