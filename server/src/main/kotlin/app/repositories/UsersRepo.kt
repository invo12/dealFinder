package app.repositories

import app.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepo: CrudRepository<User, Long> {

    fun existsByName(name: String): Boolean
    fun findFirstByName(name: String): User
}