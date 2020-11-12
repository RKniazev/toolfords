package ru.rkniazev.tollsfords.models

import ru.rkniazev.tollsfords.parsers.RETAILNAME
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "sku")
class SKU(
        var name: String = "",
        var code: String? = null,
        var weight: Int? = null,
        @ManyToOne
        var category: Category? = null):BaseEntity<Long>(){

        fun toJson():String{
                return "{\"id\":\"${this.id}\"," +
                        "\"name\":\"${this.name.replace("\"","\\\"")}\"," +
                        "\"code\":\"${this.code}\"," +
                        "\"weight\":\"${this.weight}\"," +
                        "\"category_id\":\"${this.category?.id}\"}"
        }
}

@Entity
@Table(name = "category")
class Category(
        val name: String = "" ):BaseEntity<Long>()

@Entity
@Table(name = "adapting_sku")
class AdaptingSku(
        val name: String = "",
        @ManyToOne
        val sku: SKU? = null ):BaseEntity<Long>()

@Entity
@Table(name = "history_stock")
class Stock(
        val date: LocalDate = LocalDate.now(),
        val retail: RETAILNAME? = null,
        val shop: String = "",
        @ManyToOne
        val sku: SKU? = null,
        val count:Int = 0):BaseEntity<Long>(){

        fun toJson():String{
                return "{\"id\":\"${this.id}\"," +
                        "\"date\":\"${this.date}\"," +
                        "\"retail\":\"${this.retail}\"," +
                        "\"shop\":\"${this.shop}\"," +
                        "\"sku\":\"${this.sku?.name?.replace("\"","\\\"")}\"," +
                        "\"count\":\"${this.count}\"}"
        }
}


