package app.services

import app.models.Product

interface Crawler {

    fun getProduct(url: String): Product
}