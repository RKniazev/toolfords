package ru.rkniazev.tollsfords.models

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("" +
            "SELECT * FROM history_stock WHERE " +
            "(date = :date)" +
            "and (shop_id_id = :shopId)"
        , nativeQuery = true)
    fun findByDateAndShopId(
        @Param("date")date: LocalDate,
        @Param("shopId")shopId: Long): Iterable<Stock>


    @Query("" +
            "SELECT distinct history_stock.date, shop.id as shop_id , shop.name as shop_name ,retail.name as retail_name FROM history_stock " +
            "left join shop on history_stock.shop_id_id=shop.id " +
            "left join retail on shop.retail_id=retail.id " +
            "WHERE shop.retail_id = :shopId " +
            "and date BETWEEN :dateFrom AND :dateTo " +
            "order by history_stock.date;"
        , nativeQuery = true)
    fun findListAvailableStockForShopsByDatesAndRetail(
        @Param("dateFrom")dateFrom: LocalDate,
        @Param("dateTo")dateTo: LocalDate,
        @Param("shopId")shopId: Long): Iterable<Map<String,String>>

}

interface RetailRepository : JpaRepository<Retail, Long> {
    fun findByName(name: String): Iterable<Retail>
}

interface ShopRepository : JpaRepository<Shop, Long> {
    fun findByNameAndRetail(name: String,retail: Retail): Iterable<Shop>
}