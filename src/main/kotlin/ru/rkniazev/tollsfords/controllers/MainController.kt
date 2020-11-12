package ru.rkniazev.tollsfords.controllers

import AddingAdaptingSku
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.rkniazev.tollsfords.actions.HandlerOrder
import ru.rkniazev.tollsfords.models.*
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.io.File


@RestController()
class MainController(@Autowired val repoSku: SkuRepository,
                     @Autowired val repoAdapt: AdaptingSkuRepository,
                     @Autowired val repoStockRepository: StockRepository) {

    @PostMapping("/order_from_file",produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun fileToOrder(@RequestParam("file") file: MultipartFile): String {
        val cache = File("cache")
        cache.writeBytes(file.inputStream.readAllBytes())
        val result = HandlerOrder(cache.path)
        result.start()
        cache.delete()
        return result.order.toJson()
    }

    @PostMapping("/adding_client_sku_from_file",produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun fileToAdding(@RequestParam("file") file: MultipartFile): String {
        val cache = File("cache")
        cache.writeBytes(file.inputStream.readAllBytes())
        val result = AddingAdaptingSku(cache.path,repoSku,repoAdapt)
        result.start()
        cache.delete()
        return "ok"
    }

    @PostMapping("/adding_stock",produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun addStock(@RequestParam("name") name: String): String {
        repoStockRepository.saveAndFlush( Stock(retail = RETAILNAME.S2B,shop = "test",count = 12, sku = repoSku.findByName(name).first()) )
        return "ok"
    }
}