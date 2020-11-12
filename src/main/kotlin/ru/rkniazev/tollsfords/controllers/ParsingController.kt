package ru.rkniazev.tollsfords.controllers


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.mk.MkParser
import ru.rkniazev.tollsfords.parsers.s2b.S2BParser
import ru.rkniazev.tollsfords.parsers.pitersmoke.PiterSmokeParser

@RestController
@RequestMapping("/parsing")
class ParsingController(@Autowired val repoAdapt: AdaptingSkuRepository,
                        @Autowired val repoStockRepository: StockRepository){

    @GetMapping("/ps_update")
    fun ps_update(){
        PiterSmokeParser(repoAdapt,repoStockRepository).parseData()
    }

    @GetMapping("/s2b_update")
    fun s2b_update(){
        S2BParser(repoAdapt,repoStockRepository).parseData()
    }

    @GetMapping("/mk_update")
    fun mk_update(){
        MkParser(repoAdapt,repoStockRepository).parseData()
    }
}