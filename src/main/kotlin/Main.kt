import crawlers.ThomannCrawler
import databases.SQLiteDatabase

fun main() {

    val db = SQLiteDatabase()
    val product = db.getProduct("https://www.thomann.de/ro/thomann_dp_26.htm")
    println(product)
    if (product == null) {
        println("Got from crawler")
        val webProduct = ThomannCrawler().getProduct("https://www.thomann.de/ro/thomann_dp_26.htm")
        db.addProduct(webProduct)
    } else {
        println("Got from db")
    }
}
