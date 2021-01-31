package ru.rkniazev.tollsfords.parsers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled;
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.StockRepository

@Component
class Parsers (@Autowired(required = false)
               var parsers: MutableList<BaseParser> = mutableListOf()){

    @Scheduled(cron = "0 0 12 * * ?")
    fun parseFromAllSource(){
        parsers.forEach{
            it.parseData()
        }
    }
}