package ru.rkniazev.tollsfords.parsers.mk;

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.*
import ru.rkniazev.tollsfords.parsers.BaseParser
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import ru.rkniazev.tollsfords.parsers.SavingStockService
import ru.rkniazev.tollsfords.parsers.ValidatingSkuService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.max

@Service
class MkParser(@Autowired override val adaptingSkuRepository:AdaptingSkuRepository,
               @Autowired override val retailRepository: RetailRepository,
               @Autowired override val shopRepository: ShopRepository,
               @Autowired override val validatingSkuService: ValidatingSkuService,
               @Autowired override val savingStockService: SavingStockService
               ) : BaseParser {

    override val urlBase = "https://www.allkalyans.ru"
    override val listUrlSku = mutableListOf<String>()
    override val retail = retailRepository.findByName("МирКальянов").first()

    override fun parseData() {
        updateListUrlSku()
        findStocks()
    }


    override fun updateListUrlSku(){
        listUrlSku.clear()
        val urlCatalog = "catalog"
        Jsoup.connect("$urlBase/$urlCatalog").get()
                .getElementsByClass("catalog-sections__tags-item")
                .map { it.getElementsByTag("a")
                        .first()
                        .attr("href") }
                .filter { it.contains("tabak") }
                .filter { validatingSkuService.validateSku(it) }
                .flatMap { allAdditionalPage(it) }
                .flatMap {
                    Jsoup.connect(it).get()
                            .getElementsByClass("catalog__item")
                }
                .flatMap {
                    it.getElementsByClass("option-select__item")
                            .flatMap { it.getElementsByTag("input") }

                }
                .map { urlBase + "/local/ajax/catalog.availability.php?id=" + it.attr("value") }
                .toCollection(listUrlSku)
    }

    private fun allAdditionalPage(url: String): List<String>{
        val result = mutableListOf<String>()
        val maxPage = Jsoup.connect("$urlBase$url").get()
                .getElementsByClass("pagination__list")
                .flatMap { it.getElementsByTag("span") }
                .map { it.text() }
                .max()?.toInt()

        maxPage?.let {
           for (page in 1..it){
               result.add("$urlBase$url?PAGEN_1=$page")
            }
            return result
        }

        return mutableListOf("$urlBase$url")
    }

    override fun findStocks() {
        val stocks = mutableListOf<Stock>()

        listUrlSku
                .map {
                    Jsoup.connect(it).get()
                }
                .forEach {
                    var name = it.getElementsByClass("popup__subtitle").text()
                    val weight = it
                            .getElementsByClass("option-select__item")
                            .filter { it.select("input").hasAttr("checked") }
                            .first()
                            .select("div")
                            .text()
                    name = "$weight $name"
                    val date = LocalDate.now()

                    try {
                        adaptingSkuRepository.findByName(name).first().sku?.let { sku ->
                            it.getElementsByClass("availability__shops-item")
                                    .filter { it.getElementsByClass("status").first().text().equals("В наличии") }
                                    .map { it.getElementsByClass("address").text() }
                                    .forEach { shopName ->
                                        val shop = shopRepository.findByNameAndRetail(shopName,retail).first()
                                        stocks.add(Stock(date,shop,sku,1))
                                    }
                        }
                    } catch (e:Exception){
                        println("SKU $name dont fine in DB")
                    }
                }
        savingStockService.saveResult(stocks)
    }
}