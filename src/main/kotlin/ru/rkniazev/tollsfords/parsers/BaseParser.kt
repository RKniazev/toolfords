package ru.rkniazev.tollsfords.parsers

import org.springframework.beans.factory.annotation.Autowired
import ru.rkniazev.tollsfords.models.*
import java.time.LocalDate
import java.util.*

interface BaseParser {
    val matcherStock: MatchSkuAndShopService
    val validatingSkuService: ValidatingSkuService
    val savingStockService: SavingStockService
    val retail: String
    val urlBase: String

    val listUrlSku: MutableList<String>

    fun parseData()
    fun updateListUrlSku()
    fun findStocks()
}