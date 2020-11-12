package ru.rkniazev.tollsfords.models

import org.springframework.data.jpa.repository.JpaRepository
import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.time.LocalDate

interface SkuRepository : JpaRepository<SKU, Long> {
    fun findByCategory(category:Category): Iterable<SKU>
    fun findByName(name: String): Iterable<SKU>
}

interface CategoriesRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Category
}

interface AdaptingSkuRepository : JpaRepository<AdaptingSku, Long> {
    fun findByName(name: String): Iterable<AdaptingSku>
}

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByDate(date: LocalDate): Iterable<Stock>
    fun findByDateAndRetail(date: LocalDate, retailname: RETAILNAME): Iterable<Stock>
}