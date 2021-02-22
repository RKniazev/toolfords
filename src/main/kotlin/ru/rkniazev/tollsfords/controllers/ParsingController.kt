package ru.rkniazev.tollsfords.controllers


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rkniazev.tollsfords.parsers.ParsersService

@RestController
@RequestMapping("/parsing")
class ParsingController(@Autowired var parserService: ParsersService){

    @GetMapping("/all_update")
    fun all_update(){
        parserService.parseFromAllSource()
    }

    @GetMapping("/mk")
    fun mk(){
        parserService.parseMK()
    }

    @GetMapping("/ps")
    fun ps(){
        parserService.parsePS()
    }

    @GetMapping("/s2b")
    fun s2b(){
        parserService.parseS2B()
    }
}