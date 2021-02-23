package ru.rkniazev.tollsfords.parsers.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.models.StockRepository

@Service
class SavingStockService(@Autowired private val stockRepository: StockRepository){
    fun saveResult(results:List<Stock>){
        stockRepository.saveAll(results)
        stockRepository.flush()
    }
}
