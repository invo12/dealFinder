package app.services.caches

import app.models.Product
import app.repositories.ProductRepo
import app.services.crawlers.CrawlerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

@Service
class CacheServiceImpl : CacheService {

    private final val SEARCH_URL_MATCH = ExampleMatcher
        .matching()
        .withMatcher("url", exact())

    @Autowired
    private lateinit var productRepo: ProductRepo

    @Autowired
    private lateinit var thomannCrawlerServiceImpl: CrawlerService

    @Transactional
    override fun getProducts(offset: Int, limit: Int): List<Product> = runBlocking {

        val now = LocalDate.now()
        val products = withContext(Dispatchers.IO) {
            productRepo.findAll()
        }

        products.map {
            async(Dispatchers.Default) {
                if (now != it.timestamp) {
                    val product = thomannCrawlerServiceImpl.getProduct(it.url)
                    it.oldPrice = product.oldPrice
                    it.price = product.price
                    it.timestamp = product.timestamp
                    withContext(Dispatchers.IO) {
                        productRepo.save(it)
                    }
                } else {
                    it
                }
            }
        }.map { it.await() }.toList()
    }

    @Transactional
    override fun getProduct(url: String): Product = runBlocking {

        val now = LocalDate.now()
        val p = Product()
        p.url = url
        val productSearch: Example<Product> = Example.of(p, SEARCH_URL_MATCH)

        var product = productRepo.findOne(productSearch).orElse(null)

        if (product == null || now != product.timestamp) {
            val webProduct = try {
                    thomannCrawlerServiceImpl.getProduct(url)
                } catch (e: Exception) {
                    Product()
                }
            if (webProduct.name.isNotEmpty()) {
                if (product != null) {
                    product.price = webProduct.price
                    product.oldPrice = webProduct.oldPrice
                    product.timestamp = webProduct.timestamp
                } else {
                    product = webProduct
                }
                withContext(Dispatchers.IO) {
                    productRepo.save(product)
                }
            }
            webProduct
        } else {
            product
        }
    }
}