package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.SkuRepository
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.io.File
import java.time.LocalDate

@RestController()
@RequestMapping("/audit")
class AuditController(@Autowired val repoSku: SkuRepository,
                     @Autowired val repoAdapt: AdaptingSkuRepository,
                     @Autowired val repoStockRepository: StockRepository) {

    @GetMapping("/result",produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun auditResult(@RequestParam("date") date: String): String {
        var result = "{"
        val setShops = mutableSetOf<String>()
        val mapSku = mutableMapOf<SKU, MutableList<String>>()
        val data = repoStockRepository.findByDateAndRetail(LocalDate.parse(date),RETAILNAME.S2B).forEach { stock ->
            val shop = stock.shop
            setShops.add(shop)
            val sku = stock.sku
            sku?.let {
                if (!mapSku.containsKey(it)) {
                    mapSku[it] = mutableListOf<String>()
                }

                mapSku[it]?.add(shop)

            }

        }
        result += "\"shops\" : ["
        setShops.forEach{
            result += "\"$it\","

        }
        result = result.dropLast(1)
        result += "],"

        result += "\"stock\" :["
        mapSku.forEach{
            result += "{\"${it.key.name.replace("\"","\\\"")}\":["
            it.value.distinct().forEach {
                result += "\"$it\","
            }
            result = result.dropLast(1) + "]},"

        }
        result = result.dropLast(1)
        result+="]}"
        return result
    }
}