package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.models.CategoriesRepository
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.SkuRepository

@RestController
class SkuController(@Autowired val repoSku: SkuRepository,
                    @Autowired val repoCat: CategoriesRepository) {

    @PostMapping("/sku_add")
    fun addSku(@RequestParam("name") nameInput: String,
               @RequestParam("code") codeInput: String?,
               @RequestParam("weight") weightInput: Int?,
               @RequestParam("category_id")catIdInput: Long):String{

        val sku = SKU()

        sku.name = nameInput
        codeInput?.let { sku.code = it }
        weightInput?.let { sku.weight = it }
        repoCat.findById(catIdInput).let { sku.category = it.get()}

        return repoSku.saveAndFlush(sku).toString()

    }

    @GetMapping("/sku_get",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllSku(@RequestParam("category_id")catIdInput: Long?,
                  @RequestParam("name")nameInput: String?):String{
        var result= "["
        when(catIdInput){
            null -> {
                if (nameInput!=null){
                    repoSku.findByName(nameInput).forEach{result += "${it.toJson()},"}
                }else{
                    repoSku.findAll().forEach{result += "${it.toJson()},"}
                }
            }
            else -> {
                repoSku.findByCategory(repoCat.getOne(catIdInput)).forEach{result += "${it.toJson()},"}
            }
        }

        result = result.dropLast(1)
        result += "]"
        return result
    }
}