package ru.rkniazev.tollsfords.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.service.StockService


@RestController
@RequestMapping("/stock")
class StockController(@Autowired val service: StockService) {

    @GetMapping("/get_one")
    fun getStockWithParam(@RequestParam("date") date: String?,
                          @RequestParam("shopId") shopId: Long): List<Stock> {
        return service.getStock(date, shopId)
    }

    @GetMapping("/get_available_for_retail_by_date")
    fun get(@RequestParam("shopId") shopId: Long,
            @RequestParam("dateFrom") dateFrom: String,
            @RequestParam("dateTo") dateTo: String): Iterable<Map<String,String>> {
        return service.findListAvailableStockForShopsByDatesAndRetail(dateFrom, dateTo, shopId)
    }
}