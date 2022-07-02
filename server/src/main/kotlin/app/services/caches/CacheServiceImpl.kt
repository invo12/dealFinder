package app.services.caches

import app.dto.ProductDTO
import app.models.Product
import app.repositories.ProductRepo
import app.repositories.UsersRepo
import app.services.crawlers.CrawlerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
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
    private lateinit var userRepo: UsersRepo

    @Autowired
    private lateinit var thomannCrawlerServiceImpl: CrawlerService

    @Transactional
    override fun getProducts(offset: Int, limit: Int, id: Long): List<ProductDTO> = runBlocking {

        val now = LocalDate.now()

        val user = withContext(Dispatchers.IO) {
            try {
                userRepo.findById(id).get()
            } catch (e: Exception) {
                null
            }
        } ?: return@runBlocking listOf()

        var changed = false

        user.products = user.products.map {
            async(Dispatchers.Default) {
                if (now != it.timestamp) {
                    changed = true
                    val product = thomannCrawlerServiceImpl.getProduct(it.url)
                    it.oldPrice = product.oldPrice
                    it.price = product.price
                    it.timestamp = product.timestamp
                }
                it
            }
        }.map { it.await() }.toMutableSet()
        if(changed) {
            withContext(Dispatchers.IO) { userRepo.save(user) }
        }
        user.products.map { it.toDTO() }.toList()
    }

    @Transactional
    override fun getProduct(url: String, id: Long): ProductDTO = runBlocking {

        val now = LocalDate.now()
        val user = withContext(Dispatchers.IO) {
            try {
                userRepo.findById(id).get()
            } catch (e: Exception) {
                null
            }
        } ?: return@runBlocking Product().toDTO()

        val product = withContext(Dispatchers.IO) { productRepo.findFirstByUrl(url) }

        if (product != null) {

            if (!product.users.map { it.id }.contains(user.id)) {
                product.users.add(user)
                withContext(Dispatchers.IO) { productRepo.save(product) }
            }
            if (now == product.timestamp) {
                return@runBlocking product.toDTO()
            } else {
                val webProduct = try {
                    thomannCrawlerServiceImpl.getProduct(url)
                } catch (e: Exception) {
                    return@runBlocking Product().toDTO()
                }
                product.oldPrice = webProduct.oldPrice
                product.price = webProduct.price
                withContext(Dispatchers.IO) { productRepo.save(product) }
                return@runBlocking product.toDTO()
            }
        } else {

            val webProduct = try {
                thomannCrawlerServiceImpl.getProduct(url)
            } catch (e: Exception) {
                return@runBlocking Product().toDTO()
            }

            webProduct.users = mutableSetOf(user)
            withContext(Dispatchers.IO) { productRepo.save(webProduct) }
            return@runBlocking webProduct.toDTO()
        }
    }

    override fun removeProduct(url: String, id: Long): Boolean = runBlocking {

        withContext(Dispatchers.IO) {
            try {
                userRepo.findById(id).get()
            } catch (e: Exception) {
                null
            }
        } ?: return@runBlocking false

        val product = withContext(Dispatchers.IO) { productRepo.findFirstByUrl(url) } ?: return@runBlocking false

        val length = product.users.size
        product.users.removeIf { it.id == id }
        if (length != product.users.size) {
            withContext(Dispatchers.IO) { productRepo.save(product) }
            return@runBlocking true
        }
        return@runBlocking false
    }
}