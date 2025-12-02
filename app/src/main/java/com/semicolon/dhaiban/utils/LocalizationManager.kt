package com.semicolon.dhaiban.utils

import androidx.compose.ui.unit.LayoutDirection

object LocalizationManager {
    fun getLayoutDirection(direction: String): LayoutDirection {
        return when (direction) {
            "rtl" -> LayoutDirection.Rtl
            else -> LayoutDirection.Ltr
        }
    }
}