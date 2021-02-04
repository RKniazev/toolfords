package ru.rkniazev.tollsfords.parsers.pitersmoke

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.*
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.MatchSkuAndShopService
import ru.rkniazev.tollsfords.parsers.SavingStockService
import ru.rkniazev.tollsfords.parsers.ValidatingSkuService
import java.time.LocalDate

@Service
class PiterSmokeParser(@Autowired override val matcherStock: MatchSkuAndShopService,
                       @Autowired override val validatingSkuService: ValidatingSkuService,
                       @Autowired override val savingStockService: SavingStockService
) : BaseParser {
    override val urlBase = "https://pitersmoke.su"
    override val listUrlSku = mutableListOf<String>()
    override val retail = "PiterSmoke"


    override fun parseData() {
        updateListUrlSku()
        findStocks()
    }



    override fun updateListUrlSku(){
        listUrlSku.clear()
        Jsoup.connect("$urlBase").get()
                .getElementById("categories")
                .getElementsByTag("li")
                .map{
                    "https://pitersmoke.su" + it.getElementsByTag("a").attr("href")
                }
                .filter { it.contains("tabak") }
                .filter { validatingSkuService.validateSku(it) }
                .flatMap { allAdditionalPage(it) }
                .flatMap { Jsoup.connect(it).get().getElementsByClass("product-item")}
                .map { urlBase + it.getElementsByTag("a").attr("href") }
                .toCollection(listUrlSku)
    }

    private fun allAdditionalPage(url: String): List<String>{
        val result = mutableListOf<String>()
        val maxPage = Jsoup.connect(url).get()
                .getElementsByClass("pagination")
                .flatMap { it.getElementsByTag("a") }
                .map { it.text() }
                .dropLast(1)
                .max()?.toInt()

        maxPage?.let {
            for (page in 1..it){
                result.add("$url?page=$page")
            }
            return result
        }

        return mutableListOf("$url")
    }

    override fun findStocks() {
        val stocks = mutableListOf<Stock>()
        listUrlSku
                .map {
                    Jsoup.connect(it).get()
                }
                .forEach {
                    var name = it.getElementsByTag("h1").text()
                    try {
                        it.getElementById("nav-stocks")
                            .getElementsByClass("row stock-sku-row")
                            .forEach {
                                val shopName = it.getElementsByClass("stock-name").first()
                                    .text()
                                    .replace(" \\(([\\s\\S]+?)\\)".toRegex(),"")

                                var count = it.getElementsByClass("stock-value").first()
                                    .text()
                                    .replace(" шт.","")

                                if (count.equals("Много")){
                                    count = "5"
                                }
                                val stock = matcherStock.match(retail,shopName,name,count.toInt())
                                stock?.let {
                                    stocks.add(it)
                                }
                            }
                    }catch (e:Exception){
                        println("Exeption on $ name")
                    }

                }
        savingStockService.saveResult(stocks)
    }
}