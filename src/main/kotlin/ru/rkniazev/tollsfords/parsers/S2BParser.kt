package ru.rkniazev.tollsfords.parsers;

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.*
import ru.rkniazev.tollsfords.parsers.service.MatchSkuAndShopService
import ru.rkniazev.tollsfords.parsers.service.SavingStockService
import ru.rkniazev.tollsfords.parsers.service.ValidatingSkuService
import java.time.LocalDate

@Service
class S2BParser(@Autowired override val matcherStock: MatchSkuAndShopService,
                @Autowired override val validatingSkuService: ValidatingSkuService,
                @Autowired override val savingStockService: SavingStockService
)  : BaseParser {

    override val urlBase = "https://s2b-rf.ru"
    override val listUrlSku = mutableListOf<String>()
    override val retail = "S2B"


    override fun parseData() {
        updateListUrlSku()
        findStocks()
    }

    override fun updateListUrlSku(){
        listUrlSku.clear()

        Jsoup.connect(urlBase).get()
                .getElementsByClass("submenu__entry")
                .map{ urlBase + it.getElementsByTag("a").attr("href")}
                .filter { it.contains("tabak") }
                .filter { validatingSkuService.validateSku(it) }
                .flatMap { allAdditionalPage(it)}
                .flatMap { Jsoup.connect(it).get().getElementsByClass("list__entry_item")}
                .map { urlBase + it.attr("data-url") }
                .toCollection(listUrlSku)
    }

    private fun allAdditionalPage(url: String): List<String>{
        val result = mutableListOf<String>()
        val maxPage = Jsoup.connect(url).get()
                .getElementById("catalog-pagenav")
                .getElementsByTag("a")
                .map{
                    it.attr("data-page")
                }
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
        val date = LocalDate.now()

        listUrlSku
                .map {
                    Jsoup.connect(it).get()
                }
                .forEach{
                    val name = it.getElementsByTag("h1").text()
                    it.getElementsByClass("address_list")
                        .flatMap { it.getElementsByClass("list__entry") }
                        .filter{
                            it.getElementsByClass("card__status")
                                    .first()
                                    .text()
                                    .equals("В наличии")
                        }
                        .map { it.getElementsByClass("metro__inner")
                                .text()
                                .replace(" \\(([\\s\\S]+?)\\)".toRegex(),"")
                        }
                        .forEach { shopName ->
                            val stock = matcherStock.match(retail,shopName,name)
                            stock?.let {
                                stocks.add(it)
                            }
                        }

                }

        savingStockService.saveResult(stocks)
    }
}
