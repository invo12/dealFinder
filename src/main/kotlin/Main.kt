import crawlers.Crawler
import crawlers.ThomannCrawler
import databases.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import products.Product
import java.time.LocalDate

fun getProducts(url: String, crawler: Crawler, dbOperation: (Product) -> Unit): Product {
    val product = crawler.getProduct(url)
    dbOperation(product)
    return product
}


fun main() = runBlocking(Dispatchers.Default) {

    val now = LocalDate.now()
    val urls =
        listOf(
            "https://www.thomann.de/ro/thomann_dp_26.htm",
            "https://www.thomann.de/ro/startone_cg_851_1_4_pink.htm",
            "https://www.thomann.de/ro/la_mancha_rubinito_cm_47.htm",
            "https://www.thomann.de/ro/la_mancha_rubinito_lsm_47.htm",
            "https://www.thomann.de/ro/millenium_mps_850_drum_module.htm"
        )
    val db = SQLiteDatabase()
    val th = ThomannCrawler()

    val urlProductPairs = urls.map { Pair(it, db.getProduct(it)) }
    val nonNullProducts = urlProductPairs.filter { it.second != null }
    val validProducts = nonNullProducts.filter { now == it.second?.timestamp }
    val expiredProducts = nonNullProducts.filter { now != it.second?.timestamp }

    val inexistentProducts = urlProductPairs.filter { it.second == null }

    val newProducts = inexistentProducts.map {
        async { getProducts(it.first, th) { product: Product -> db.addProduct(product) } }
    }
    val updatedProducts = expiredProducts.map {
        async { getProducts(it.first, th) { product: Product -> db.updateProduct(it.second!!, product) } }
    }

    println("Valid products: $validProducts")
    println("New products: ${newProducts.awaitAll()}")
    println("Updated products: ${updatedProducts.awaitAll()}")
}
