package crawlers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import products.Product
import java.time.LocalDate

class ThomannCrawler : Crawler {

    override fun getProduct(url: String): Product {
        val doc = Jsoup.connect(url).get()

        val name = extractName(doc)
        val fullPrice = extractPrice(doc)
        val (price, currency) = fullPrice.split(" ", limit = 2)

        return Product(url, name, 0.0, price.toDouble(), currency, LocalDate.now())
    }

    private fun extractName(document: Document): String {
        return document
            .getElementsByTag("head")[0]
            .getElementsByTag("title")
            .text().split("–")[0].trim()
    }

    private fun extractPrice(document: Document): String {

        val priceElement =
            document
                .select(".fx-content-product__sidebar")
                .select(".price-and-availability")
                .select(".price")

        val currency = "€"
        val price = priceElement.text().replace(" ", "").split(currency)[0]

        return "$price $currency"
    }
}