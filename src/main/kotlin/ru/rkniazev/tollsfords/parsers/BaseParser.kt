package ru.rkniazev.tollsfords.parsers

import org.springframework.beans.factory.annotation.Autowired
import ru.rkniazev.tollsfords.models.*
import java.time.LocalDate
import java.util.*

interface BaseParser {
    val adaptingSkuRepository: AdaptingSkuRepository
    val validatingSkuService: ValidatingSkuService
    val shopRepository: ShopRepository
    val savingStockService: SavingStockService
    val retailRepository: RetailRepository
    val retail: Retail
    val urlBase: String

    val listUrlSku: MutableList<String>

    fun parseData()
    fun updateListUrlSku()
    fun findStocks()
}