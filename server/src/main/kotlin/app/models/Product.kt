package app.models

import app.dto.ProductDTO
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "products")
class Product() {

    constructor(id: Long?, url: String,
        name: String,
        oldPrice: Double,
        price: Double,
        currency: String,
        timestamp: LocalDate
    ) : this() {
        this.id = id
        this.url = url
        this.name = name
        this.oldPrice = oldPrice
        this.price = price
        this.currency = currency
        this.timestamp = timestamp
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "url")
    var url: String = ""

    @Column(name = "name")
    var name: String = ""

    @Column(name = "oldPrice")
    var oldPrice: Double = 0.0

    @Column(name = "price")
    var price: Double = 0.0

    @Column(name = "currency")
    var currency: String = ""

    @Column(name = "timestamp")
    var timestamp: LocalDate = LocalDate.now()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_product",
        joinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    @JsonIgnoreProperties("products")
    lateinit var users: MutableSet<User>

    override fun toString(): String {
        return "$id, $url, $name, $oldPrice, $price, $currency $timestamp"
    }

    fun toDTO(): ProductDTO {
        return ProductDTO(url, name, oldPrice, price, currency, timestamp)
    }
}