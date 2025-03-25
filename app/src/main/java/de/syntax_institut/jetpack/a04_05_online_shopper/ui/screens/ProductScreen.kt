package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.ProductItem
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(viewModel: ProductViewModel) {

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val minPrice by viewModel.minPrice.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val filteredProductList by viewModel.filteredProductList.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val categories = listOf("men's clothing", "women's clothing", "jewelery", "electronics")
    val formattedCategories = categories.map {
        it.split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Filter",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )

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
                                        onClick = { viewModel.updateCategory(apiCategory) }
                                    )
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

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Apply filter")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.clearFilters()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reset filter")
                        }
                    }
                }
            }
        },
        content = {
            Scaffold { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Filter")
                        }

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            label = { Text("Search for products") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            trailingIcon = {
                                if (searchQuery.text.isNotBlank()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery(TextFieldValue("")) }) {
                                        Icon(Icons.Default.Clear, contentDescription = "clear search")
                                    }
                                }
                            }
                        )
                    }

                    if (filteredProductList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredProductList, key = { it.id }) { product ->
                                var isExpanded by remember { mutableStateOf(false) }
                                ProductItem(
                                    product = product,
                                    viewModel = viewModel,
                                    isExpanded = isExpanded,
                                    onExpandClick = { isExpanded = !isExpanded }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
