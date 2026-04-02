package com.atiq.quickwhatsapp.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atiq.quickwhatsapp.model.MessageTemplate

@Composable
fun MessageTemplateRow(
    templates: List<MessageTemplate>,
    selected: MessageTemplate?,
    onSelect: (MessageTemplate?) -> Unit,
    onDelete: (MessageTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        templates.forEach { template ->
            FilterChip(
                selected = selected == template,
                onClick = {
                    onSelect(if (selected == template) null else template)
                },
                label = { Text(template.text, maxLines = 1) },
                trailingIcon = {
                    IconButton(onClick = { onDelete(template) }) {
                        Icon(Icons.Default.Close, contentDescription = "Delete")
                    }
                }
            )
        }
    }
}