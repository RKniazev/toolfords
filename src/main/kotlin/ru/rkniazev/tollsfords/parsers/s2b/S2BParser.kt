package ru.rkniazev.tollsfords.parsers.s2b;

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository;
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.models.StockRepository;
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.time.LocalDate

@Service
class S2BParser(val addingAdaptingSku:AdaptingSkuRepository,
                val repoStockRepository:StockRepository)  : BaseParser {

    val urlStart = "https://s2b-rf.ru"
    val urlSearch = "search/result?qsearch="

    val html= Jsoup.connect(urlStart)
    var allProductList:List<String> = mutableListOf<String>()
    val report = mutableMapOf<String, MutableList<SKU>>()
    val listAllSku = mutableSetOf<String>()
    val ourProduct = listOf("darkside","daily hookah","zomo")


    override fun parseData() {
        val dateParse = LocalDate.now()
        ourProduct.forEach { findAllLinksByQuery(it) }

        allProductList.forEach{
            findStockForProduckt(it)
        }

        report.forEach{
            val shop = it.key

            it.value.forEach {
                val stock = Stock(dateParse, RETAILNAME.S2B,shop,it,1)
                repoStockRepository.save(stock)
            }
        }
        repoStockRepository.flush()
    }


    fun findAllLinksByQuery(query: String, page:Int = 1){
        val html = Jsoup.connect("$urlStart/$urlSearch$query&page=$page").get()
        val listResult = html.getElementsByClass("entry__name").select("a").eachAttr("href").filter { it.contains("iteminfo") }.map{ urlStart+it }
        allProductList += listResult

        val pageNow = html.getElementsByClass("pagenav__button_current").attr("data-page").toInt()
        val pageNext = html.getElementsByClass("pagenav__button_next").attr("data-page").toInt()

        if( pageNext > pageNow ){
            findAllLinksByQuery(query,pageNext)
        }
    }

    fun findStockForProduckt(url: String){
        val page = Jsoup.connect(url).get()
        val name = page.select("h1").text()

        val listStock = page
                .getElementsByClass("address_list")
                .flatMap { it.getElementsByClass("list__entry") }
                .filter{
                    it.getElementsByClass("card__status").first().text().equals("В наличии")
                }
                .map { it.getElementsByClass("metro__inner").text().replace(" \\(([\\s\\S]+?)\\)".toRegex(),"")}
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
