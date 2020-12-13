package ru.rkniazev.tollsfords.parsers.mk;

import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.SKU
import ru.rkniazev.tollsfords.models.Stock
import ru.rkniazev.tollsfords.models.StockRepository
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.time.LocalDate

@Service
class MkParser(val addingAdaptingSku:AdaptingSkuRepository,
                val repoStockRepository:StockRepository)  : BaseParser {

    val urlStart = "https://www.allkalyans.ru"
    val urlSearch = "search/?q="

    val html= Jsoup.connect(urlStart)
    var allProductList:List<String> = mutableListOf<String>()
    val report = mutableMapOf<String, MutableList<SKU>>()
    val listAllSku = mutableSetOf<String>()
    val ourProduct = listOf("dark side","daily hookah","zomo")


    override fun parseData() {
        val dateParse = LocalDate.now()
        ourProduct.forEach { findAllLinksByQuery(it) }

        allProductList.map { "https://www.allkalyans.ru/local/ajax/catalog.availability.php?id=$it"}
                .forEach { findStockForProduckt(it) }

        report.forEach{
            val shop = it.key

            it.value.forEach {
                val stock = Stock(dateParse, RETAILNAME.MK ,shop,it,1)
                repoStockRepository.save(stock)
            }
        }
        repoStockRepository.flush()

    }


    fun findAllLinksByQuery(query: String, page:Int = 1) {
        val html = Jsoup.connect("$urlStart/$urlSearch$query&PAGEN_1=$page").get()
        val listResult = html.getElementsByClass("option-select__item").select("input").eachAttr("value")

        allProductList += listResult

        try {
            val pageNow = html.getElementsByClass("pagination__item_current").select("span").text().toInt()
            val pageMax = html.getElementsByClass("pagination__link").select("span").map { it.text() }.max()?.toInt()

            if( pageMax != null && pageMax > pageNow ) {
                findAllLinksByQuery(query, pageNow+1)
            }
        } catch (e:Exception){

        }

    }

    fun findStockForProduckt(url: String){
        val page = Jsoup.connect(url).get()
        var name = page.getElementsByClass("popup__subtitle").text()
        val weight = page
                .getElementsByClass("option-select__item")
                .filter { it.select("input").hasAttr("checked") }
                .first()
                .select("div")
                .text()
        name = "$weight $name"

        val listStock = page
                .getElementsByClass("availability__shops-item")
                .filter{ it.getElementsByClass("status").first().text().equals("В наличии") }
                .map { it.getElementsByClass("address").text() }
                .forEach{shop ->
                    if (!report.containsKey(shop)) { report[shop] = mutableListOf<SKU>() }
                    try {
                        addingAdaptingSku.findByName(name).first().sku?.let { report[shop]?.add(it) }
                    } catch (e:Exception){
                        println("SKU $name dont fine in DB")
                    }
                }
        }
}
