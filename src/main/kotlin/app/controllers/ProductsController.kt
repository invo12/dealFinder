package app.controllers

import app.models.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import app.services.Crawler

@Controller
class ProductsController {

    @Autowired
    private lateinit var crawler: Crawler

    @RequestMapping(value=["/products"], method = [RequestMethod.GET])
    @ResponseBody
    fun getProduct(@RequestBody url: String): Product {

       return crawler.getProduct(url)
    }
}