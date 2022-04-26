package app.repositories

import app.models.Product

interface Database {

    fun getProduct(url: String): Product?
    fun addProduct(product: Product)
    fun updateProduct(oldProduct: Product, newProduct: Product)
    fun deleteProduct(product: Product)
}