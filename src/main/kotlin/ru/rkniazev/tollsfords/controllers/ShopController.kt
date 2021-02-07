package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

import ru.rkniazev.tollsfords.models.Shop
import ru.rkniazev.tollsfords.models.ShopRepository


@RestController
@RequestMapping("/shop")
class ShopController(@Autowired val rep: ShopRepository) {

    @PostMapping("")
    fun add(@RequestBody shop: Shop?): String? {
        shop?.let {
            rep.saveAndFlush(shop)
            return shop.toJson()
        }
        return ""
    }

    @GetMapping("")
    fun get(): String? {
        var result = "["
        rep.findAll().forEach {
            result += "${it.toJson()},"
        }
        result = result.dropLast(1) + "]"
        return result
    }

    @GetMapping("/{inputId}")
    fun getById(@PathVariable inputId: String):String{
        val id = inputId.toLong()
        val shop = rep.findById(id).get()
        return "${shop.toJson()}"
    }

    @DeleteMapping("/{inputId}")
    fun delById(@PathVariable inputId: String) : String {
        val id = inputId.toLong()
        return if (rep.existsById(id)){
            rep.deleteById(id)
            "Success"
        } else {
            "Id don't fine"
        }
    }

    @PutMapping("")
    fun put(@RequestBody shop: Shop?)  {
        shop?.let {
            rep.saveAndFlush(shop)
        }
    }
}