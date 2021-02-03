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
}

interface RetailRepository : JpaRepository<Retail, Long> {
    fun findByName(name: String): Iterable<Retail>
}

interface ShopRepository : JpaRepository<Shop, Long> {
    fun findByNameAndRetail(name: String,retail: Retail): Iterable<Shop>
}