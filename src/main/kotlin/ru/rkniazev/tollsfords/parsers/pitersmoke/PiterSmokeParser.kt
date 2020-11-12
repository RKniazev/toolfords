package ru.rkniazev.tollsfords.parsers.pitersmoke

import org.jsoup.Jsoup
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.time.LocalDate

class PiterSmokeParser(val addingAdaptingSku: AdaptingSkuRepository,
                       val repoStockRepository: StockRepository):BaseParser {
    val urlStart = "https://pitersmoke.su"
    val html= Jsoup.connect(urlStart)
    var allProductList:List<String> = mutableListOf<String>()
    val report = mutableMapOf<String, MutableList<SKU>>()


    override fun parseData() {
        val dateParse = LocalDate.now();
        findAllLinks()
        allProductList.forEach{
            findStockForProduckt(it)
        }

        report.forEach{
            val shop = it.key

            it.value.forEach {
                val stock = Stock(dateParse, RETAILNAME.PITERSMOKE,shop,it,1)
                repoStockRepository.save(stock)
            }
        }

        repoStockRepository.flush()
    }

    fun findAllLinks(){
        allProductList = html.get()
                .select("a")
                .filter{ itsOurProduct(it.toString()) }
                .map {
                    val res = mutableListOf<String>()
                    for (x in 1..10){
                        res.add(urlStart + it.attr("href") + "?page=" + x.toString())
                    }
                    res }
                .flatMap {it.toList()}
                .map { html.url(it).get().select("a")
                        .filter{ itsOurProduct(it.toString()) }
                        .map { urlStart + it.attr("href") } }
                .flatMap {it.toList()}
                .filter { it.contains("product") }
                .distinct()
                .sorted()
    }

    fun itsOurProduct(inp: String):Boolean{
        val ourProduct = listOf("darkside","dailyhookah","zomo")
        val clearInp = inp.replace("-","").replace(" ","")
        for (x in ourProduct){
            if (clearInp.contains(x,true))
                return true
        }
        return false
    }

    fun findStockForProduckt(url: String){
        val page = Jsoup.connect(url).get()
        val name = page.select("h1").text()
        val listStock = page
                .getElementsByClass("stock-name")
                .map { it.text().replace(" \\(([\\s\\S]+?)\\)".toRegex(),"")}
                .forEach{shop ->
                    if (!report.containsKey(shop)){
                        report[shop] = mutableListOf<SKU>()
                    }

                    try {
                        addingAdaptingSku.findByName(name).first().sku?.let { report[shop]?.add(it) }
                    } catch (e:Exception){
                        println("SKU $name dont fine in DB")
                    }
                }
    }
}