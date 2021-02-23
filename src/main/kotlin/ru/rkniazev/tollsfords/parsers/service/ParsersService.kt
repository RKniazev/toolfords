package ru.rkniazev.tollsfords.parsers.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled;
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.MkParser
import ru.rkniazev.tollsfords.parsers.PiterSmokeParser
import ru.rkniazev.tollsfords.parsers.S2BParser

@Component
class ParsersService (@Autowired(required = false)
               var parsers: MutableList<BaseParser> = mutableListOf()){

    @Scheduled(cron = "0 0 12 * * ?")
    fun parseFromAllSource(){
        parsers.forEach{
            GlobalScope.async {
                it.parseData()
            }
        }
    }

    fun parseMK(){
        parsers
            .filter { it is MkParser }
            .forEach { it.parseData() }
    }

    fun parsePS(){
        parsers
            .filter { it is PiterSmokeParser }
            .forEach { it.parseData() }
    }

    fun parseS2B(){
        parsers
            .filter { it is S2BParser }
            .forEach { it.parseData() }
    }

}