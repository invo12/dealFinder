package app.controllers

import app.models.Product
import app.services.users.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class UserController {

    @Autowired
    lateinit var userServiceImpl: UserServiceImpl

    @RequestMapping(value = ["/users"], method = [RequestMethod.GET])
    @CrossOrigin(origins = ["http://localhost:4200"])
    @ResponseBody
    fun getUserId(
        @RequestParam username: String
    ): Long {
        return if(username.isNotEmpty()) {
            userServiceImpl.getUser(username)
        } else {
            -1
        }
    }
}