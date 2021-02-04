package ru.rkniazev.tollsfords.parsers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.*
import java.time.LocalDate

@Service
class MatchSkuAndShopService(@Autowired val adaptingSkuRepository: AdaptingSkuRepository,
                             @Autowired val shopRepository: ShopRepository,
                             @Autowired val retailRepository: RetailRepository) {

    fun match(retail_inp: String?=null,
              shop_inp: String,
              name_inp: String,
              count:Int = 1):Stock?{
        val date = LocalDate.now()
        var shop: Shop?
        try {
            val sku = adaptingSkuRepository.findByName(name_inp).first().sku
            retail_inp?.let {
                val retail = retailRepository.findByName(it).first()
                shop = shopRepository.findByNameAndRetail(shop_inp,retail).first()
                return Stock(date,shop,sku,count)
            }
        }catch (e:Exception) {
            println("SKU $name_inp dont fine in DB")
        }
        return null
    }
}