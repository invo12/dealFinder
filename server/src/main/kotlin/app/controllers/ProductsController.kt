package app.controllers

import app.models.Product
import app.services.caches.CacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ProductsController {

    @Autowired
    private lateinit var cacheServiceImpl: CacheService

    @RequestMapping(value = ["/products"], method = [RequestMethod.GET])
    @ResponseBody
    fun getProduct(
        @RequestBody url: String,
        @RequestParam(required = false, defaultValue = "0") offset: Int,
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): List<Product> {

        return cacheServiceImpl.getProducts(offset, limit)
    }
}