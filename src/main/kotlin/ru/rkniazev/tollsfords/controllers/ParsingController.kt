package ru.rkniazev.tollsfords.controllers


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.mk.MkParser
import ru.rkniazev.tollsfords.parsers.pitersmoke.PiterSmokeParser
import ru.rkniazev.tollsfords.parsers.s2b.S2BParser

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

    @GetMapping("/mk")
    fun mk_update(){
        parsers
            .filter { it is MkParser }
            .forEach { it.parseData() }
    }

    @GetMapping("/ps")
    fun ps_update(){
        parsers
            .filter { it is PiterSmokeParser }
            .forEach { it.parseData() }
    }

    @GetMapping("/s2b")
    fun s2b_update(){
        parsers
            .filter { it is S2BParser }
            .forEach { it.parseData() }
    }
}