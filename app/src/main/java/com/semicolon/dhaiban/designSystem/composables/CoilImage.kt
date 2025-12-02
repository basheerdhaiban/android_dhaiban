package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.semicolon.dhaiban.R
import kotlinx.coroutines.launch

@Composable
fun CoilImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    errorImage: Int = R.drawable.image_error_icon,
    placeholderImage: Int = R.drawable.loading_img,
    scaleType: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(16.dp),
    zoomable: Boolean = false
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .placeholder(placeholderImage)
            .error(errorImage).build()
    )
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoomForGraphicLayer by rememberSaveable { mutableFloatStateOf(1f) }
    val zoom = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()
    if (zoomable) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { clickedOffset ->
                            zoomForGraphicLayer = if (zoomForGraphicLayer > 1f) 1f else 3f
                            coroutineScope.launch {
                                zoom.animateTo(if (zoom.value > 1f) 1f else 3f)
                            }
                            offset = calculateDoubleTapOffset(
                                zoomForGraphicLayer,
                                size,
                                clickedOffset
                            )
                        },
                        onLongPress = { clickedOffset ->
                            zoomForGraphicLayer = if (zoomForGraphicLayer > 1f) 1f else 3f
                            coroutineScope.launch {
                                zoom.animateTo(if (zoom.value > 1f) 1f else 3f)
                            }
                            offset = calculateDoubleTapOffset(
                                zoomForGraphicLayer,
                                size,
                                clickedOffset
                            )
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, gestureZoom, _ ->
                            offset = offset.calculateNewOffset(
                                centroid, pan, zoomForGraphicLayer, gestureZoom, size
                            )
                            coroutineScope.launch {
                                zoom.animateTo(maxOf(1f, zoomForGraphicLayer * gestureZoom))
                            }
                        }
                    )
                }
                .graphicsLayer {
                    translationX = -offset.x * zoom.value
                    translationY = -offset.y * zoom.value
                    scaleX = zoomForGraphicLayer
                    scaleY = zoomForGraphicLayer
                    transformOrigin = TransformOrigin(0f, 0f)
                },
            contentScale = scaleType,
        )

    } else {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier.clip(shape),
            contentScale = scaleType,
        )
    }
}


fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1.5f).coerceAtLeast(0f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1.5f).coerceAtLeast(0f))
    )
}