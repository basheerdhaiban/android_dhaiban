package com.semicolon.dhaiban.designSystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import com.semicolon.dhaiban.presentation.app.SecondStringResources
import com.semicolon.dhaiban.presentation.app.StringResources
import com.semicolon.dhaiban.utils.LocalizationManager

private val localColorScheme = staticCompositionLocalOf { LightColors }
private val localRadius = staticCompositionLocalOf { Radius() }
private val localTypography = staticCompositionLocalOf { DhaibanTypography() }
private val localStringResources = staticCompositionLocalOf<StringResources> { StringResources() }
private val localSecondStringResources = staticCompositionLocalOf<SecondStringResources> { SecondStringResources() }


@Composable
fun DhaibanTheme(
    layoutDirection:String = "ltr",
    stringResources: StringResources = StringResources(),
    SecondStringResources: SecondStringResources = SecondStringResources(),
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColors
    val typography = DhaibanTypography(
        headline = headline(),
        titleLarge = titleLarge(),
        title = title(),
        body = body(),
        caption = caption(),
        header = header(),
        otherHeading = otherHeading()
    )

    CompositionLocalProvider(
        localStringResources provides stringResources,
        localSecondStringResources provides SecondStringResources,
        LocalLayoutDirection provides LocalizationManager.getLayoutDirection(layoutDirection),
        localColorScheme provides colorScheme,
        localTypography provides typography,
        localRadius provides Radius(),
    ) {
        content()
    }
}

object Theme {
    val colors: DhaibanColor
        @Composable
        @ReadOnlyComposable
        get() = localColorScheme.current

    val typography: DhaibanTypography
        @Composable
        @ReadOnlyComposable
        get() = localTypography.current

    val radius: Radius
        @Composable
        @ReadOnlyComposable
        get() = localRadius.current

    val strings: StringResources
        @Composable
        @ReadOnlyComposable
        get() = localStringResources.current
    val SecondStrings: SecondStringResources
        @Composable
        @ReadOnlyComposable
        get() = localSecondStringResources.current
}