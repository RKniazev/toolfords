package ru.rkniazev.tollsfords.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.models.StockRepository
import java.time.LocalDate

@Service
class StockService(@Autowired val rep: StockRepository) {
    fun getStock(dateStr: String?,
                 shopId: Long?): List<Stock>{
        val result = mutableListOf<Stock>()
        val date = dateStr?.let { LocalDate.parse(it) }

        if (date != null && shopId != null){
            rep.findByDateAndShopId(date,shopId).toCollection(result)
        }

        return result
    }

    fun findListAvailableStockForShopsByDatesAndRetail(dateStrFrom: String,
                           dateStrTo: String,
                           shopId: Long): Iterable<Map<String,String>>{
        val dateFrom = LocalDate.parse(dateStrFrom)
        val dateTo = LocalDate.parse(dateStrTo)
        return rep.findListAvailableStockForShopsByDatesAndRetail(dateFrom, dateTo, shopId)
    }
}