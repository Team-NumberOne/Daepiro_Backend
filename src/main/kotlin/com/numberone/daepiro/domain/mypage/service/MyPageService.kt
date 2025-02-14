package com.numberone.daepiro.domain.mypage.service

import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.verified.UserAddressVerifiedRepository
import com.numberone.daepiro.domain.file.entity.FileDocumentType
import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.domain.mypage.dto.request.CreateAnnouncementRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditAddressesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditDisasterTypesRequest
import com.numberone.daepiro.domain.mypage.dto.request.EditProfileRequest
import com.numberone.daepiro.domain.mypage.dto.request.GetMyArticleRequest
import com.numberone.daepiro.domain.mypage.dto.request.InquireRequest
import com.numberone.daepiro.domain.mypage.dto.response.AnnouncementResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyAddressesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyDisasterTypesResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyNotificationResponse
import com.numberone.daepiro.domain.mypage.dto.response.MyProfileResponse
import com.numberone.daepiro.domain.mypage.entity.Inquiry
import com.numberone.daepiro.domain.mypage.repository.InquiryRepository
import com.numberone.daepiro.domain.user.repository.UserLikeRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findAllLikedArticleId
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.domain.user.service.UserService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_NOTIFICATION_TYPE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MyPageService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val userService: UserService,
    private val inquiryRepository: InquiryRepository,
    private val fileRepository: FileRepository,
    private val userLikeRepository: UserLikeRepository,
    private val userAddressVerifyRepository: UserAddressVerifiedRepository
) {
    fun getMyProfile(userId: Long): ApiResult<MyProfileResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyProfileResponse.of(user))
    }

    fun getMyNotification(userId: Long): ApiResult<MyNotificationResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyNotificationResponse.of(user))
    }

    fun getMyAddresses(userId: Long): ApiResult<MyAddressesResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyAddressesResponse.of(user))
    }

    fun getMyDisasterTypes(userId: Long): ApiResult<MyDisasterTypesResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(MyDisasterTypesResponse.of(user))
    }

    //ArticleService.getMulti와 중복되는 코드 많음
    fun getMyArticles(userId: Long, request: GetMyArticleRequest): ApiResult<Slice<ArticleListResponse>> {
        val slice = articleRepository.getMyArticles(userId, request)
        val files = fileRepository.findAllByDocumentTypeAndDocumentIdIn(
            documentType = FileDocumentType.ARTICLE,
            documentIds = slice.map { it.id }.distinct(),
        )

        val likedArticleIdSet = userLikeRepository.findAllLikedArticleId(userId)

        val authorMapOfArticleId = slice.content.associate { it.id to it.authorUser }
        val verifiedAddressIdMapOfAuthorId =
            userAddressVerifyRepository.findAllByUserIdIn(authorMapOfArticleId.mapNotNull { it.value?.userId })
                .groupBy { it.userId }
                .mapValues { it -> it.value.map { it.addressId } }

        slice.forEach { article ->
            val relatedFile = files.find { it.documentId == article.id }
            relatedFile?.let {
                article.updatePreviewImageUrl(it.path)
            }
            if (likedArticleIdSet.contains(article.id)) {
                article.updateIsLiked(true)
            }
            val author = article.authorUser
            val articleAddress = article.address

            if (author != null && articleAddress != null) {
                val verifiedAuthorAddresses = verifiedAddressIdMapOfAuthorId[author.userId]
                author.isVerified = verifiedAuthorAddresses?.contains(articleAddress.addressId) ?: false
            }
        }
        return ApiResult.ok(slice)
    }

    @Transactional
    fun updateMyProfile(userId: Long, request: EditProfileRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        user.initName(request.realname, request.nickname)
    }

    @Transactional
    fun updateMyNotification(userId: Long, type: String) {
        val user = userRepository.findByIdOrThrow(userId)
        when (type) {
            "community" -> user.isCommunityNotificationEnabled = !user.isCommunityNotificationEnabled
            "disaster" -> user.isDisasterNotificationEnabled = !user.isDisasterNotificationEnabled
            else -> throw CustomException(NOT_FOUND_NOTIFICATION_TYPE)
        }
    }

    @Transactional
    fun updateMyAddresses(userId: Long, request: EditAddressesRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        userService.handleOnboardingAddress(request.addresses, user)
    }

    @Transactional
    fun updateMyDisasterTypes(userId: Long, request: EditDisasterTypesRequest) {
        val user = userRepository.findByIdOrThrow(userId)
        userService.handleOnboardingDisasterType(request.disasterTypes, user)
    }

    @Transactional
    fun inquire(userId: Long, request: InquireRequest) {
        inquiryRepository.save(Inquiry.of(request.type, request.content, request.email, userId))
    }

    fun getAnnouncements(): ApiResult<List<AnnouncementResponse>> {
        val result = articleRepository.findAllAnnouncement()
        return ApiResult.ok(result.map { AnnouncementResponse.of(it) })
    }

    fun getAnnouncement(id: Long): ApiResult<AnnouncementResponse> {
        val result = articleRepository.findAnnouncementById(id)
        return ApiResult.ok(AnnouncementResponse.of(result, articleRepository.findNextAnnouncement(result.id!!)))
    }

    @Transactional
    fun createAnnouncement(request: CreateAnnouncementRequest, userId: Long) {
        val user = userRepository.findByIdOrThrow(userId)
        articleRepository.save(Article.ofAnnouncement(request.title, request.body, user))
    }
}
