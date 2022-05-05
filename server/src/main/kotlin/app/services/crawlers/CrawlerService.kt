package app.services.crawlers

import app.models.Product

interface CrawlerService {

    fun getProduct(url: String): Product
}