package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarAndFilters(
    searchQuery: TextFieldValue,
    onSearchQueryChanged: (TextFieldValue) -> Unit,
    onClearSearch: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = onOpenDrawer) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Filter",
                modifier = Modifier.width(70.dp).height(70.dp)
            )
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text("Search for products") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            trailingIcon = {
                if (searchQuery.text.isNotBlank()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(Icons.Default.Clear, contentDescription = "clear search")
                    }
                }
            }
        )
    }
}
