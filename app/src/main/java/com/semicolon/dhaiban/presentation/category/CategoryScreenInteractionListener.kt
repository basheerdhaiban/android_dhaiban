package com.semicolon.dhaiban.presentation.category

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface CategoryScreenInteractionListener : BaseInteractionListener {
    fun onClickBackButton()
    fun onClickCategory(id: Int, title: String)
    fun onClickNotification()
}