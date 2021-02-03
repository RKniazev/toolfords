package ru.rkniazev.tollsfords.controllers


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.RETAILNAME

//import ru.rkniazev.tollsfords.parsers.mk.MkParser
//import ru.rkniazev.tollsfords.parsers.s2b.S2BParser
//import ru.rkniazev.tollsfords.parsers.pitersmoke.PiterSmokeParser

@RestController
@RequestMapping("/parsing")
class ParsingController(@Autowired(required = false)
                        var parsers: MutableList<BaseParser> = mutableListOf()){

    @GetMapping("/all_update")
    fun all_update(){
        parsers.forEach {
            it.parseData()
        }
    }
}