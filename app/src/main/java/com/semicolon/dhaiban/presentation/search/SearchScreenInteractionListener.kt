package com.semicolon.dhaiban.presentation.search

interface SearchScreenInteractionListener {
    fun onClickBackButton()
    fun onChangeSearchValue(queryText : String)
    fun onClickProduct(productId: Int)
    fun onClickNotification()
    fun searchForProductByImage(byteArray: ByteArray?)
    fun searchForProduct(query: String?, imageByteArray: ByteArray?)
}