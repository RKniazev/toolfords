package ru.rkniazev.tollsfords.parsers

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