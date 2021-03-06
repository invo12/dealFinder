package app.repositories

import app.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo: JpaRepository<Product, Long> {
    fun findFirstByUrl(url: String): Product?
}