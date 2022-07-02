package app.controllers

import app.dto.ProductDTO
import app.services.caches.CacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class ProductsController {

    @Autowired
    private lateinit var cacheServiceImpl: CacheService

    @RequestMapping(value = ["/products/users/{id}"], method = [RequestMethod.GET])
    @CrossOrigin(origins = ["http://localhost:4200"])
    @ResponseBody
    fun getProducts(
        @RequestParam(required = false, defaultValue = "0") offset: Int,
        @RequestParam(required = false, defaultValue = "10") limit: Int,
        @PathVariable id: Long
    ): List<ProductDTO> {

        return cacheServiceImpl.getProducts(offset, limit, id)
    }
    @RequestMapping(value = ["/products/users/{id}"], method = [RequestMethod.POST])
    @CrossOrigin(origins = ["http://localhost:4200"])
    @ResponseBody
    fun addProduct(@RequestParam websiteLink: String, @PathVariable id: Long): ProductDTO {

        return cacheServiceImpl.getProduct(websiteLink, id)
    }

    @RequestMapping(value = ["/products/users/{id}"], method = [RequestMethod.DELETE])
    @CrossOrigin(origins = ["http://localhost:4200"])
    @ResponseBody
    fun removeProduct(@RequestParam websiteLink: String, @PathVariable id: Long): Boolean {

        return cacheServiceImpl.removeProduct(websiteLink, id)
    }
}