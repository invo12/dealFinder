package app.repositories

import app.models.Product

interface ProductRepo {

    fun getProducts(offset: Int, limit: Int): List<Product>
    fun getProduct(url: String): Product?
    fun addProduct(product: Product)
    fun updateProduct(oldProduct: Product, newProduct: Product)
    fun deleteProduct(product: Product)
}