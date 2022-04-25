package crawlers

import products.Product

interface Crawler {

    fun getProduct(url: String): Product
}