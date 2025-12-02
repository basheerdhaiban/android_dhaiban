package com.semicolon.dhaiban.presentation.home

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface HomeScreenInteractionListener : BaseInteractionListener {
    fun onClickNotificationIcon()
    fun onClickBackToHome()
    fun onClickTrackOrder(orderId: Int)
    fun onClickViewAllCategories()
    fun onClickViewAllBrands()
    fun onClickViewAllSaleProducts()

    fun onClickNewProducts()

    fun onClickProductType(id: Int)

    fun onClickProduct(productId: Int)

    fun onClickCategory(categoryId: Int, categoryTitle: String)

    fun onClickProductFavorite(productId: Int, isSaleProduct: Boolean, isFavorite: Boolean)

    fun onClickBrand(brandId: Int, brandTitle: String)

    fun onChangeSearchValue(queryText : String)

    fun cancelSearch()

    fun onClickProfile()

    fun onClickSearch()

    fun onRefresh()

    fun onClickBack()

    fun onDismissExit()
}