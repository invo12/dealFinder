package app.controllers

import app.models.Product
import app.services.caches.CacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class ProductsController {

    @Autowired
    private lateinit var cacheServiceImpl: CacheService

    @RequestMapping(value = ["/products"], method = [RequestMethod.GET])
    @CrossOrigin(origins = ["http://localhost:4200"])
    @ResponseBody
    fun getProducts(
        @RequestParam(required = false, defaultValue = "0") offset: Int,
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): List<Product> {

        return cacheServiceImpl.getProducts(offset, limit)
    }
}