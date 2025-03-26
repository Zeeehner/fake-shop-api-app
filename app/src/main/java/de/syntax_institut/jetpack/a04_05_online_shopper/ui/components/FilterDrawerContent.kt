package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ModalDrawerSheet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*


@Composable
fun FilterDrawerContent(
    categories: List<String>,
    formattedCategories: List<String>,
    selectedCategory: String?,
    viewModel: ProductViewModel,
    minPrice: String,
    maxPrice: String

) {
    val isGridView = viewModel.isGridView.collectAsStateWithLifecycle().value

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Filter",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.toggleViewMode()
                        }, modifier = Modifier.padding(8.dp)
                    ) {
                        Text(if (isGridView) "List" else "Grid")
                    }
                }

                Text("Categories", modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(categories.zip(formattedCategories)) { (apiCategory, displayCategory) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCategory == apiCategory,
                                onClick = { viewModel.updateCategory(apiCategory) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(displayCategory)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Price range", modifier = Modifier.padding(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = minPrice,
                        onValueChange = { viewModel.updatePriceRange(it, maxPrice) },
                        label = { Text("Min") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = maxPrice,
                        onValueChange = { viewModel.updatePriceRange(minPrice, it) },
                        label = { Text("Max") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.clearFilters()  }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset filter")
                }
            }
        }
    }
}

