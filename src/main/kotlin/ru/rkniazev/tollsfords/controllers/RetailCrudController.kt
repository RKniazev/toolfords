package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.rkniazev.tollsfords.models.Retail
import ru.rkniazev.tollsfords.models.RetailRepository


@RestController
@RequestMapping("/retail")
class RetailCrudController(@Autowired val rep: RetailRepository) {

    @PostMapping("")
    fun add(@RequestBody retail: Retail) = rep.saveAndFlush(retail)

    @GetMapping("")
    fun get() = rep.findAll()

    @GetMapping("/{inputId}")
    fun getById(@PathVariable inputId: Long) = rep.findById(inputId).get()

    @DeleteMapping("/{inputId}")
    fun delById(@PathVariable inputId: Long) = rep.deleteById(inputId)

    @PutMapping("")
    fun put(@RequestBody retail: Retail) = rep.saveAndFlush(retail)
}