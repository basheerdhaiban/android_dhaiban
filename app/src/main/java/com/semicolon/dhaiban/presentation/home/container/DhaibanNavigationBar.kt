package com.semicolon.dhaiban.presentation.home.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun DhaibanNavigationBar(
    modifier: Modifier = Modifier,
    navigationBarHeight: Dp = 64.dp,
    backgroundColor: Color = Theme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = 12.dp,
        modifier = modifier,
        shape = RoundedCornerShape(topEnd = 8.dp, topStart = 12.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(navigationBarHeight)
                .selectableGroup(),
            horizontalArrangement = horizontalArrangement,
            content = content
        )
    }
}

@Composable
fun RowScope.DhaibanNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (tint: Color) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable ((style: TextStyle) -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {

    val styledLabel = @Composable {
        val textColor by animateColorAsState(
            if (selected) Theme.colors.primary else Theme.colors.black38, label = "",
        )
        val style =
            Theme.typography.caption.copy(color = textColor)
        label?.let {
            it(style)
        }
    }

    Box(
        modifier
            .selectable(
                indication = null,
                interactionSource = interactionSource,
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
            )
            .selectableGroup()
            .fillMaxHeight()
            .weight(1f)
    ) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon(Theme.colors.primary)
            AnimatedVisibility((selected && label != null)) {
                styledLabel()
            }
        }
    }
}