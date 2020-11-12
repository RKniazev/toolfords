package ru.rkniazev.toolfords.Model

import ru.rkniazev.tollsfords.models.SKU

class Order() {
    private val map = mutableMapOf<SKU,Int>()

    fun add(sku: SKU, count: Int) = map.put(sku,count)


    fun toJson():String{

        var result= "["
        map.forEach{result += "{\"sku\":\"${it.key.name.replace("\"","\\\"")}\",\"amount\":${it.value}},"}
        result = result.dropLast(1)
        result += "]"
        return result
    }

    override fun toString():String {
        var result = ""
        map.forEach{result += "${it.key.name}\t${it.value}${System.lineSeparator()}"}
        return result
    }
}