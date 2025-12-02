package com.semicolon.dhaiban.presentation.brand

interface BrandScreenInteractionListener {
    fun onClickBackButton()

    fun onClickBrand(brandId: Int, brandTitle: String)

    fun onClickNotification()
}