package com.semicolon.dhaiban.presentation.utils

import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.paging.compose.LazyPagingItems
import cafe.adriel.voyager.navigator.Navigator
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.main.MainActivity

@RequiresApi(Build.VERSION_CODES.S)
fun MainActivity.applySplashScreenWithAnimation() {
    installSplashScreen()
    splashScreen.setOnExitAnimationListener { splashScreenView ->
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = 500L
        slideUp.doOnEnd { splashScreenView.remove() }
        slideUp.start()
    }
}

fun MainActivity.applyEdgeToEdge() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(
            resources.getColor(R.color.transparent, theme),
            resources.getColor(R.color.black, theme)
        ),
        navigationBarStyle = SystemBarStyle.light(
            resources.getColor(R.color.transparent, theme),
            resources.getColor(R.color.black, theme)
        )
    )
}

val Navigator.root: Navigator?
    get() {
        var root: Navigator? = null
        repeat(level) {
            root = root?.parent ?: parent
        }
        return root
    }

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = { it.hashCode() }) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    Box(
                        modifier = Modifier.weight(1F, fill = true),
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
    }
}

fun <T : Any> LazyListScope.gridPagingItems(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    data: LazyPagingItems<T>,
    columnCount: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val rows = if (data.itemCount == 0) 50 else 1 + (data.itemCount - 1) / columnCount
    if (isLoading && rows == 50) {
        showLoadingIndicator()
    }
    if (data.itemCount == 0 && isLoading.not()) {
        showPlaceholder()
    } else {
        items(rows) { rowIndex ->
            AnimatedContent(targetState = rows != 50, label = "") {
                if (it) {
                    Row(modifier = modifier, horizontalArrangement = horizontalArrangement) {
                        for (columnIndex in 0 until columnCount) {
                            val itemIndex = rowIndex * columnCount + columnIndex
                            if (itemIndex < data.itemCount) {
                                val item = data[itemIndex]
                                if (item != null) {
                                    key(key?.invoke(item)) {
                                        Box(
                                            modifier = Modifier.weight(1f, fill = true),
                                            propagateMinConstraints = true
                                        ) {
                                            itemContent.invoke(this, item)
                                        }
                                    }
                                }
                            } else {
                                Spacer(Modifier.weight(1f, fill = true))
                            }
                        }
                    }
                } else {
                    Row(modifier = modifier, horizontalArrangement = horizontalArrangement) {
                        for (columnIndex in 0 until rows) {
                            val itemIndex = rowIndex * 30 + columnIndex
                            if (itemIndex < data.itemCount) {
                                val item = data[itemIndex]
                                if (item != null) {
                                    key(key?.invoke(item)) {
                                        Box(
                                            modifier = Modifier.weight(1f, fill = true),
                                            propagateMinConstraints = true
                                        ) {
                                            itemContent.invoke(this, item)
                                        }
                                    }
                                }
                            } else {
                                Spacer(Modifier.weight(1f, fill = true))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.showPlaceholder() {
    item {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_error_icon),
                contentDescription = ""
            )
        }
    }
}

private fun LazyListScope.showLoadingIndicator() {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = Theme.colors.primary)
        }
    }
}

