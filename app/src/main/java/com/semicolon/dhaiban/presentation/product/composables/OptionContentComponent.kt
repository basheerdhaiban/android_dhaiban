package com.semicolon.dhaiban.presentation.product.composables

import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.product.PropertiesUiState
import com.semicolon.dhaiban.presentation.product.ReviewUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
@Composable
fun OptionContentComponent(
    modifier: Modifier = Modifier,
    productDescription: String,
    productReviews: List<ReviewUiState>,
    properties: PropertiesUiState,
    optionType: OptionType
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
        AnimatedContent(targetState = optionType, label = "") {
            when (it) {
                OptionType.DESCRIPTION -> DescriptionComponent(productDescription)
                OptionType.REVIEWS -> ReviewsComponent(productReviews)
                OptionType.PROPERTIES -> PropertiesComponent(properties)
            }
        }
    }
}
@Composable
fun HTMLTable(htmlText: String) {
    val context = LocalContext.current
    val document: Document = Jsoup.parse(htmlText)
    val rows: Elements = document.select("tr")

    Column(modifier = Modifier.padding(16.dp)) {
        for (row in rows) {
            HtmlTableRow(row)
        }
    }
}

@Composable
fun HtmlTableRow(row: Element) {
    val cells: Elements = row.select("td")
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        cells.forEach { cell ->
            BasicText(
                text = cell.text(),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                modifier = Modifier.weight(1f).padding(4.dp)
            )
        }
    }
}

@Composable
fun TableCell(text: String) {
    Surface(
        modifier = Modifier

            .background(Color.LightGray)
            .padding(8.dp),
        color = Color.White,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            modifier = Modifier.padding(4.dp)
        )
    }
}
@Composable
private fun DescriptionComponent(description: String) {
    if (description.isNotEmpty()){
        if (description.startsWith("<table>\r")) {
        HTMLTable( description) }
        else{
            HtmlText(description)

        }
    }else{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "No Data", style = Theme.typography.body)
        }
    }
}

@Composable
private fun ReviewsComponent(reviews: List<ReviewUiState>) {
    if (reviews.isNotEmpty()){
        Column {
            reviews.forEach { review ->
                ReviewItem(review, reviews.last().id)
            }
        }
    }else{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "No Data", style = Theme.typography.body)
        }
    }

}

@Composable
private fun PropertiesComponent(properties: PropertiesUiState) {
    val entries = properties.properties.entries.toList()
    if (entries.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Theme.colors.whisperingBlue)
                .padding(vertical = 16.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                entries.forEachIndexed { index, entry ->
                    if (index < entries.lastIndex && index % 2 == 0) {
                        Row {
                            PropertyItem(
                                modifier = Modifier.weight(0.5f),
                                title = entry.key,
                                value = entry.value
                            )
                            PropertyItem(
                                modifier = Modifier.weight(0.5f),
                                title = entries[index + 1].key,
                                value = entries[index + 1].value
                            )
                        }
                    }
                }
                if (entries.size % 2 != 0) {
                    Row {
                        PropertyItem(
                            modifier = Modifier.weight(0.5f),
                            title = entries[entries.lastIndex].key,
                            value = entries[entries.lastIndex].value
                        )
                        PropertyItem(
                            modifier = Modifier.weight(0.5f),
                            title = "",
                            value = "",
                            visibility = false
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "No Data", style = Theme.typography.body)
        }
    }

}

@Composable
private fun PropertyItem(
    modifier: Modifier,
    title: String,
    value: String,
    visibility: Boolean = true
) {
    if (visibility) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, end = 8.dp, start = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Theme.colors.white),
            contentAlignment = Alignment.CenterStart
        ) {

            Text(
                text = "$title:  $value",
                style = Theme.typography.caption,
                modifier = Modifier.padding(12.dp)
            )
        }
    } else {
        Box(modifier.fillMaxWidth())
    }
}

@Composable
private fun ReviewItem(review: ReviewUiState, lastReviewId: Int) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.fillMaxWidth(0.7f)) {
                Image(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(CircleShape)
                        .size(50.dp),
                    painter = painterResource(id = R.drawable.on_boarding_third),
                    contentDescription = ""
                )
                Column(
                    modifier = Modifier.height(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = review.username, style = Theme.typography.body)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = review.createdDate)
                    }
                }
            }
            RatingBar( rating = review.averageRating.toInt())
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = review.review, style = Theme.typography.caption)
        if (review.id != lastReviewId) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            ) {

            }
        }
    }

}


@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}


@Preview
@Composable
fun ReviewItemPreview() {
    ReviewItem(
        ReviewUiState(
            1,
            5.0,
            "Bla",
            "",
            "",
            "Bla bla bla bla bla bla bla"
        ), 1
    )
}

@Preview
@Composable
fun PropertiesComponentPreview() {
    PropertiesComponent(
        PropertiesUiState(
            mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3",
                "key4" to "value4",
                "key5" to "value5",
            )
        )
    )
}