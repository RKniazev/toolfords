package ru.rkniazev.tollsfords.parsers

import ru.rkniazev.tollsfords.models.AdaptingSkuRepository
import ru.rkniazev.tollsfords.models.StockRepository
import java.time.LocalDate
import java.util.*

interface BaseParser {
    val adaptingSkuRepository: AdaptingSkuRepository
    val validatingSkuService: ValidatingSkuService
    val savingStockService: SavingStockService
    val retail: RETAILNAME
    val urlBase: String

    val listUrlSku: MutableList<String>

    fun parseData()
    fun updateListUrlSku()
    fun findStocks()
}