package com.semicolon.domain.entity.productdetails

data class ProductDetailsModel(
    val advancedProduct: String,
    val brand: ProductDetailsBrand,
    val choiceOptions: List<ChoiceOptionModel>,
    val colors: List<ColorModel>,
    val country: String,
    val currentStock: Int,
    val description: String,
    val discount: Double,
    val discountType: String,
    val id: Int,
    val labelColor: String,
    val labelText: String,
    val measuringUnit: String,
    val owner: String,
    val photo: String,
    val photos: List<PhotoModel>,
    val seller :String,
    val priceHigher: Double,
    val priceLower: Double,
    val rating: Double,
    val ratingCount: Int,
    val shortDescription: String,
    val tags: List<String>,
    val title: String,
    val userFavorite: Boolean,
    val variantProduct: Int,
    val properties: Map<String,String>,
    val cart: List<CartItemModel>
){
    val afterDiscount: Double = if (discountType == "amount") {
        priceHigher - discount.toDouble()
    } else {
        priceHigher - priceHigher * discount.toDouble().div(100.0)
    }
}