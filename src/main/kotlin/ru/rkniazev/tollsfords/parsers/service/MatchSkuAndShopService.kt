package ru.rkniazev.tollsfords.parsers.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.rkniazev.tollsfords.models.*
import ru.rkniazev.tollsfords.parsers.model.NotMatchNameEntity
import ru.rkniazev.tollsfords.parsers.model.NotMatchNameRepository
import java.time.LocalDate

@Service
class MatchSkuAndShopService(@Autowired val adaptingSkuRepository: AdaptingSkuRepository,
                             @Autowired val shopRepository: ShopRepository,
                             @Autowired val retailRepository: RetailRepository,
                             @Autowired val notMatchNameRepository: NotMatchNameRepository
) {

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
            notMatchNameRepository.saveAndFlush(NotMatchNameEntity(name = name_inp))
        }
        return null
    }
}