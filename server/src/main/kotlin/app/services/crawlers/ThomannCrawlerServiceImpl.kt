package app.services.crawlers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import app.models.Product
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ThomannCrawlerServiceImpl : CrawlerService {

    override fun getProduct(url: String): Product {

        val doc = Jsoup.connect(url).get()

        val name = extractName(doc)
        val fullPrice = extractPrice(doc)
        val (price, currency) = fullPrice.split(" ", limit = 2)

        return Product(0 ,url, name, 0.0, price.toDouble(), currency, LocalDate.now())
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