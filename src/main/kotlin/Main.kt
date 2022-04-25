import products.ThomannCrawler

fun main() {
    val url = "https://www.thomann.de/ro/thomann_dp_26_set.htm"
    println(ThomannCrawler().getProduct(url))
}
