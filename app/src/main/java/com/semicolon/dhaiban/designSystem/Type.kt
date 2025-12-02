package com.semicolon.dhaiban.designSystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class DhaibanTypography(
    val headline: TextStyle = TextStyle(),
    val titleLarge: TextStyle = TextStyle(),
    val title: TextStyle = TextStyle(),
    val body: TextStyle = TextStyle(),
    val caption: TextStyle = TextStyle(),
    val header: TextStyle = TextStyle() ,
    val otherHeading: TextStyle = TextStyle()
)

@Composable
fun headline(): TextStyle {
    return TextStyle(
        fontSize = 22.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun header(): TextStyle {
    return TextStyle(
        fontSize = 20.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W600,
    )
}

@Composable
fun titleLarge(): TextStyle {
    return TextStyle(
        fontSize = 18.sp,
        lineHeight = 25.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W500,
    )
}


@Composable
fun title(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W500,
    )
}

@Composable
fun body(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W400,
    )
}


@Composable
fun caption(): TextStyle {
    return TextStyle(
        fontSize = 12.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W400,
    )
}
@Composable
fun otherHeading(): TextStyle {
    return TextStyle(
        fontSize = 12.sp,
        fontFamily = poppins,
        fontWeight = FontWeight.W500,
    )
}