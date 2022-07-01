package app.models

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


}