package app.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name="users")
class User() {

    constructor(id: Long?, name: String): this() {
        this.id = id
        this.name = name
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    var id: Long? = null

    @Column(name="name")
    var name: String = ""

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_product",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "product_id", referencedColumnName = "id")])
    @JsonIgnoreProperties("users")
    lateinit var products: MutableSet<Product>
}