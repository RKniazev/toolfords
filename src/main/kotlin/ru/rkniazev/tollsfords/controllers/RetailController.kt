package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.rkniazev.tollsfords.models.Retail
import ru.rkniazev.tollsfords.models.RetailRepository


@RestController
@RequestMapping("/retail")
class RetailController(@Autowired val rep: RetailRepository) {

    @PostMapping("")
    fun add(@RequestBody retail: Retail?): String? {
        retail?.let {
            rep.saveAndFlush(retail)
            return retail.toJson()
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
    fun put(@RequestBody retail: Retail?)  {
        retail?.let {
            rep.saveAndFlush(retail)
        }
    }
}