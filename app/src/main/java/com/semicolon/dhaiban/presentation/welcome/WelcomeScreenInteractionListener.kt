package com.semicolon.dhaiban.presentation.welcome

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface WelcomeScreenInteractionListener : BaseInteractionListener {
    fun onClickLogin()
    fun onClickSignUp()
   suspend fun onClickSkip()
}