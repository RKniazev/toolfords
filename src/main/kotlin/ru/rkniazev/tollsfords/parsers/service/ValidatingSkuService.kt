package ru.rkniazev.tollsfords.parsers.service

import org.springframework.stereotype.Service

@Service
class ValidatingSkuService {
    private val ourProduct = listOf(
            "darkside",
            "dailyhookah",
            "zomo")

    fun validateSku(sku:String):Boolean{
        ourProduct.forEach{
            if (sku.replace("-","")
                            .replace("_","")
                            .replace("_","")
                            .toLowerCase()
                            .contains(it)){
                return true
            }
        }
        return false
    }
}