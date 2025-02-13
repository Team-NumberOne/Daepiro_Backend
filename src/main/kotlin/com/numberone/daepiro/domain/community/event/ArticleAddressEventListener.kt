package com.numberone.daepiro.domain.community.event

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.GeoLocationConverter
import com.numberone.daepiro.domain.address.repository.findChildWithMe
import com.numberone.daepiro.domain.address.repository.findParentWithMe
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.global.utils.TransactionUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ArticleAddressEventListener(
    private val geocodingRepository: GeoLocationConverter,
    private val articleRepository: ArticleRepository,
    private val addressRepository: AddressRepository,
) {

    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onEvent(event: ArticleAddressMappingEvent) {
        val article = articleRepository.findByIdOrThrow(event.articleId)
        val address = geocodingRepository.findByLongitudeAndLatitudeOrThrow(
            event.longitude,
            event.latitude
        )

        TransactionUtils.writable {
            val relatedAddress = addressRepository.findParentWithMe(address)
                .find { it.depth == 3 } // si_gun_gu 까지만 세분화

            relatedAddress?.let {
                // map address with article
                articleRepository.save(article.updateAddress(it))
                //addressRepository.save(it)
            }
        }
    }
}

data class ArticleAddressMappingEvent(
    val articleId: Long,
    val longitude: Double,
    val latitude: Double,
)
