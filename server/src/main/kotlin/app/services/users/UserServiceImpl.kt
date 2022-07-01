package app.services.users

import app.models.User
import app.repositories.UsersRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService{

    @Autowired
    lateinit var usersRepo: UsersRepo

    override fun getUser(userName: String): Long {

        if (usersRepo.existsByName(userName)) {
            return usersRepo.findFirstByName(userName).id!!
        }
        val user = User(0, userName)
        user.products = mutableSetOf()
        return usersRepo.save(user).id!!
    }

}