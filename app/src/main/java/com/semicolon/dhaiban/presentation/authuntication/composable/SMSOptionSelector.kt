//@file:OptIn(ExperimentalMaterial3Api::class)
package com.semicolon.dhaiban.presentation.authuntication.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.presentation.authuntication.SMSOption
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun SMSOptionSelector(
    modifier: Modifier = Modifier,
    selectedOption: Int = SMSOption.WhatsApp.value,
    onOptionSelected: (Int) -> Unit = {},
) {
    var currentSelection by rememberSaveable { mutableIntStateOf(selectedOption) }

    // Title
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.choose_sms_option),
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(Modifier.height(12.dp))

        // Three evenly-spaced tiles (great on phones)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OptionTile(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.whatsapp),
                icon = { OptionIcon(SMSOption.WhatsApp.value) },
                selected = currentSelection == SMSOption.WhatsApp.value,
                onClick = {
                    if (currentSelection != SMSOption.WhatsApp.value) {
                        currentSelection = SMSOption.WhatsApp.value
                        onOptionSelected(currentSelection)
                    }
                }
            )

            OptionTile(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.email),
                icon = { OptionIcon(SMSOption.Email.value) },
                selected = currentSelection == SMSOption.Email.value,
                onClick = {
                    if (currentSelection != SMSOption.Email.value) {
                        currentSelection = SMSOption.Email.value
                        onOptionSelected(currentSelection)
                    }
                }
            )

            OptionTile(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.SMS),
                icon = { OptionIcon(SMSOption.SMS.value) },
                selected = currentSelection == SMSOption.SMS.value,
                onClick = {
                    if (currentSelection != SMSOption.SMS.value) {
                        currentSelection = SMSOption.SMS.value
                        onOptionSelected(currentSelection)
                    }
                }
            )
        }
    }
}

@Composable
private fun OptionIcon(optionId: Int, modifier: Modifier = Modifier) {
    // Use your own WhatsApp drawable. Fallbacks for email/SMS use Material icons.
    val painter: Painter = when (optionId) {
        SMSOption.WhatsApp.value -> painterResource(id = R.drawable.whatsapp_icon) // add this asset
        SMSOption.Email.value -> painterResource(id = R.drawable.icon_email)
        SMSOption.SMS.value -> painterResource(id = R.drawable.sms)
        else -> painterResource(id = R.drawable.icon_email)
    }
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = modifier.size(26.dp),
        tint = Theme.colors.primary
    )
}

@Composable
private fun OptionTile(
    label: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        label = ""
    )
    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = ""
    )
    val borderWidth by animateDpAsState(if (selected) 2.dp else 1.dp, label = "")
    val borderColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.outlineVariant,
        label = ""
    )

    Surface(
        modifier = modifier
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(14.dp))
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = if (selected) 3.dp else 0.dp,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                color = Theme.colors.primary,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun SMSOptionSelectorPreview() {
    SMSOptionSelector(
        selectedOption = SMSOption.WhatsApp.value,
        onOptionSelected = { selected -> }
    )

}
