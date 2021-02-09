package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import ru.rkniazev.tollsfords.models.Shop
import ru.rkniazev.tollsfords.models.ShopRepository


@RestController
@RequestMapping("/shop")
class ShopController(@Autowired val rep: ShopRepository) {

    @PostMapping("")
    fun add(@RequestBody shop: Shop) = rep.saveAndFlush(shop)

    @GetMapping("")
    fun get() = rep.findAll()

    @GetMapping("/{inputId}")
    fun getById(@PathVariable inputId: Long) = rep.findById(inputId).get()

    @DeleteMapping("/{inputId}")
    fun delById(@PathVariable inputId: Long) = rep.deleteById(inputId)

    @PutMapping("")
    fun put(@RequestBody shop: Shop) = rep.saveAndFlush(shop)
}