package ru.rkniazev.tollsfords.parsers

import ru.rkniazev.tollsfords.parsers.service.MatchSkuAndShopService
import ru.rkniazev.tollsfords.parsers.service.SavingStockService
import ru.rkniazev.tollsfords.parsers.service.ValidatingSkuService

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