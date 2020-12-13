package ru.rkniazev.tollsfords.parsers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled;
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.mk.MkParser
import ru.rkniazev.tollsfords.parsers.s2b.S2BParser
import ru.rkniazev.tollsfords.parsers.pitersmoke.PiterSmokeParser

@Component
class Parsers (@Autowired val repoAdapt: AdaptingSkuRepository,
               @Autowired val repoStockRepository: StockRepository,
               @Autowired(required = false)
               var parsers: MutableList<BaseParser> = mutableListOf()){

    @Scheduled(cron = "0 0 12 * * *")
    fun parseFromAllSource(){
        parsers.forEach{
            it.parseData()
        }
    }
}