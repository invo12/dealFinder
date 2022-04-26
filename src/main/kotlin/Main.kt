import crawlers.ThomannCrawler
import databases.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

suspend fun main() = runBlocking(Dispatchers.Default) {

    val urls =
        listOf(
            "https://www.thomann.de/ro/thomann_dp_26.htm",
            "https://www.thomann.de/ro/startone_cg_851_1_4_pink.htm",
            "https://www.thomann.de/ro/la_mancha_rubinito_cm_47.htm",
            "https://www.thomann.de/ro/la_mancha_rubinito_lsm_47.htm",
        )
    val db = SQLiteDatabase()
    val th = ThomannCrawler()

    val urlProductPairs = urls.map { Pair(it, db.getProduct(it)) }
    val validProducts = urlProductPairs.filter { it.second != null }.map { it.second }
    val invalidProducts = urlProductPairs.filter { it.second == null }
    println(invalidProducts.size)

    val deferredList = invalidProducts.map {
        async {
            val product = th.getProduct(it.first)
            db.addProduct(product)
            product
        }
    }
    println(validProducts + deferredList.awaitAll())
}
