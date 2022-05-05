package app.repositories

import app.models.Product
import org.springframework.stereotype.Service
import java.sql.*
import java.time.LocalDate

@Service
class ProductRepoImpl : ProductRepo {

    private val dbPath = "jdbc:sqlite:/home/invo/workspace/projects/dealFinder/server/deals.db"

    private fun connect(): Connection? {

        return try {
            Class.forName("org.sqlite.JDBC")
            DriverManager.getConnection(dbPath)
        } catch (e: SQLException) {
            println(e.message)
            null
        }
    }

    private fun executeDML(conn: Connection?, sql: String, errorMessage: String) {

        conn?.let {
            try {
                val stmt: Statement = it.createStatement()
                stmt.executeUpdate(sql)
                stmt.close()
            } catch (ex: Exception) {
                System.err.println(errorMessage + ex.message)
            } finally {
                it.close()
            }
        }
    }

    private fun createTableIfNotExists(conn: Connection?): Boolean {

        val sql =
            "CREATE TABLE IF NOT EXISTS products ( url TEXT, name TEXT, oldPrice REAL, price REAL, currency TEXT," +
                    "lastTime TEXT);"

        conn?.let {
            try {
                val stmt: Statement = it.createStatement()
                stmt.executeUpdate(sql)
                stmt.close()
                return true
            } catch (ex: Exception) {
                System.err.println("Can't create table")
            } finally {
                it.close()
            }
        }

        return false
    }

    override fun getProducts(offset: Int, limit: Int): List<Product> {

        connect()?.let {
            val stmt: Statement = it.createStatement()
            val rs: ResultSet = stmt.executeQuery("SELECT * FROM products LIMIT $limit OFFSET $offset;")
            return rs.use {
                generateSequence {
                    if (rs.next()) Product(
                        rs.getString("url"), rs.getString("name"),
                        rs.getDouble("oldPrice"), rs.getDouble("price"),
                        rs.getString("currency"), LocalDate.parse(rs.getString("lastTime"))
                    ) else null
                }.toList()
            }
        }
        return listOf()
    }

    override fun getProduct(url: String): Product? {

        connect()?.let {
            try {
                val stmt: Statement = it.createStatement()
                val rs: ResultSet = stmt.executeQuery("SELECT * FROM products WHERE url='$url';")
                if (rs.next()) {
                    return Product(
                        url, rs.getString("name"), rs.getDouble("oldPrice"),
                        rs.getDouble("price"), rs.getString("currency"),
                        LocalDate.parse(rs.getString("lastTime"))
                    )
                }

            } catch (e: SQLException) {
                if (e.message!!.contains("no such table: products")) {
                    if (createTableIfNotExists(it)) {
                        return getProduct(url)
                    }
                }
            } finally {
                it.close()
            }
        }

        return null
    }

    override fun addProduct(product: Product) {

        val conn = connect()
        val sql = "INSERT INTO products (url, name, oldPrice, price, currency, lastTime) " +
                "VALUES ('${product.url}', '${product.name}', ${product.oldPrice}, ${product.price}, " +
                "'${product.currency}', '${product.timestamp}');"
        executeDML(conn, sql, "Can't insert into table")
    }

    override fun updateProduct(oldProduct: Product, newProduct: Product) {

        val conn = connect()
        val sql = "UPDATE products SET oldPrice=${oldProduct.price}, price=${newProduct.price}, " +
                "lastTime='${newProduct.timestamp}' WHERE url='${oldProduct.url}';"
        executeDML(conn, sql, "Can't update table")
    }

    override fun deleteProduct(product: Product) {

        val conn = connect()
        val sql = "DELETE FROM products WHERE url='${product.url}';"
        executeDML(conn, sql, "Can't delete table")
    }
}