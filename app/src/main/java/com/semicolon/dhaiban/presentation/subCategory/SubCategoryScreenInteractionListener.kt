package com.semicolon.dhaiban.presentation.subCategory

interface SubCategoryScreenInteractionListener {

    fun onClickBackButton()

    fun onClickSubCategory(id: Int)

    fun onClickFilter(visibility: Boolean)

    fun onClickProduct(productId: Int)

    fun onClickApplyFilter(
        categorySelected: Boolean,
        categoryId: Int,
        sortById: Int,
        colorId: Int,
        minPrice: Int,
        maxPrice: Int,
        attributes: Map<Int, List<String>>
    )

    fun onClickProductFavorite(productId: Int, isFavorite: Boolean)

    fun onClickSearch()
    fun onClickNotification()
    fun updateFirstItemState(visible: Boolean)
}