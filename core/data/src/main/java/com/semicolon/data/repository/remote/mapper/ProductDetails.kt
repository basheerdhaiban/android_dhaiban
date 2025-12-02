package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.product.ReviewDto
import com.semicolon.data.repository.remote.model.product.ReviewUserDto
import com.semicolon.data.repository.remote.model.product.ReviewsData
import com.semicolon.data.repository.remote.model.product.TotalRate
import com.semicolon.data.repository.remote.model.productdetails.Brand
import com.semicolon.data.repository.remote.model.productdetails.ChoiceOption
import com.semicolon.data.repository.remote.model.productdetails.Color
import com.semicolon.data.repository.remote.model.productdetails.Option
import com.semicolon.data.repository.remote.model.productdetails.Photo
import com.semicolon.data.repository.remote.model.productdetails.ProductDetailsDto
import com.semicolon.domain.entity.productdetails.CartItemModel
import com.semicolon.domain.entity.productdetails.ChoiceOptionModel
import com.semicolon.domain.entity.productdetails.ColorModel
import com.semicolon.domain.entity.productdetails.OptionModel
import com.semicolon.domain.entity.productdetails.PhotoModel
import com.semicolon.domain.entity.productdetails.ProductDetailsBrand
import com.semicolon.domain.entity.productdetails.ProductDetailsModel
import com.semicolon.domain.entity.productdetails.ReviewModel
import com.semicolon.domain.entity.productdetails.ReviewUser
import com.semicolon.domain.entity.productdetails.ReviewsDataModel
import com.semicolon.domain.entity.productdetails.TotalRateModel


fun Brand.toProductDetailsBrand() = ProductDetailsBrand(
    logo = this.logo ?: "",
    name = this.name ?: ""
)

fun Option.toOptionModel() = OptionModel(
    id = this.id ?: 0,
    title = this.title ?: ""
)

fun ChoiceOption.toChoiceOptionModel() = ChoiceOptionModel(
    id = this.name ?: "",
    options = this.options?.map { it.toOptionModel() } ?: emptyList(),
    title = this.title ?: ""
)

fun Color?.toColorModel(): ColorModel = this?.let {
    ColorModel(
        colorCode = it.colorCode ?: "",
        id = it.id ?: 0
    )
} ?: ColorModel("", 0)

fun Photo.toPhotoModel() = PhotoModel(
    photoUrl = this.photoUrl ?: "",
    id = this.id ?: 0
)

fun ProductDetailsDto.toProductDetailsModel() =
    ProductDetailsModel(
        advancedProduct = this.advancedProduct ?: "",
        brand = this.brand?.toProductDetailsBrand() ?: ProductDetailsBrand("", ""),
        choiceOptions = this.choiceOptions?.map { it.toChoiceOptionModel() } ?: emptyList(),
        colors = this.colors?.map { it.toColorModel() } ?: emptyList(),
        country = this.country ?: "",
        currentStock = this.currentStock ?: 0,
        description = this.description ?: "",
        discount = this.discount ?: 0.0,
        discountType = this.discountType ?: "",
        id = this.id ?: 0,
        labelColor = this.labelColor ?: "",
        labelText = this.labelText ?: "",
        measuringUnit = this.measuringUnit ?: "",
        owner =  "",
        photo = this.photo ?: "",
        photos = this.photos?.map { it.toPhotoModel() } ?: emptyList(),
        priceHigher = this.priceHigher ?: 0.0,
        priceLower = this.priceLower ?: 0.0,
        rating = this.rating ?: 0.0,
        ratingCount = this.ratingCount ?: 0,
        shortDescription = this.shortDescription ?: "",
        tags = this.tags ?: emptyList(),
        title = this.title ?: "",
        userFavorite = this.userFavorite ?: false,
        seller =this.seller?:"Dhaiban" ,
        variantProduct = this.variantProduct ?: 0,
        properties = this.serializedSpecs
            ?.filter { it.key != null && it.value != null }
            ?.mapKeys { it.key!! }
            ?.mapValues { it.value!! }
            ?: emptyMap()        ,
        cart = this.cart.map { CartItemModel(it.identifier, it.quantity, it.variant?:"") }
    )

fun ReviewUserDto.toReviewUserModel() = ReviewUser(
    id, name
)

fun ReviewDto.toReviewModel() = ReviewModel(
    active = this.active ?: 0,
    createdAt = this.createdAt ?: "",
    reviewId = id ?: 0,
    productId = this.productId ?: 0,
    rate = this.rate ?: 0.0,
    review = this.review ?: "",
    updatedAt = this.updatedAt ?: "",
    reviewUser = this.user?.toReviewUserModel() ?: ReviewUser(0, ""),
    userId = this.userId ?: 0
)

fun TotalRate.toTotalRateModel() = TotalRateModel(
    avgRating.toInt(),
    ratingCount.toInt(),
    ratingCount1.toInt(),
    ratingCount2.toInt(),
    ratingCount3.toInt(),
    ratingCount4.toInt(),
    ratingCount5.toInt()
)

fun ReviewsData.toReviewsDataModel() = ReviewsDataModel(
    reviews = this.reviews?.map { it.toReviewModel() } ?: emptyList(),
    totalRate = this.totalRate?.toTotalRateModel() ?: TotalRateModel(
        0,
        0,
        0,
        0,
        0,
        0,
        0
    )
)