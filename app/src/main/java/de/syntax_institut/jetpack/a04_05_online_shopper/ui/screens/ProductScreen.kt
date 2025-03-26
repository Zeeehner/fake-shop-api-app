package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.ErrorDialog
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.FilterDrawerContent
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.ProductGrid
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.ProductList
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.SearchBarAndFilters
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(viewModel: ProductViewModel) {

    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle().value
    val selectedCategory = viewModel.selectedCategory.collectAsStateWithLifecycle().value
    val minPrice = viewModel.minPrice.collectAsStateWithLifecycle().value
    val maxPrice = viewModel.maxPrice.collectAsStateWithLifecycle().value
    val filteredProductList = viewModel.filteredProductList.collectAsStateWithLifecycle().value
    val errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle().value
    val isGridView = viewModel.isGridView.collectAsStateWithLifecycle().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val categories = listOf("men's clothing", "women's clothing", "jewelery", "electronics")
    val formattedCategories = categories.map {
        it.split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        FilterDrawerContent(
            categories = categories,
            formattedCategories = formattedCategories,
            selectedCategory = selectedCategory,
            viewModel = viewModel,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }, content = {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "FakeShop.api",
                            style = MaterialTheme.typography.displayLarge,
                            fontStyle = Italic,
                            fontWeight = ExtraBold
                        )
                    }
                )
            }, content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    SearchBarAndFilters(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                        onClearSearch = { viewModel.updateSearchQuery(TextFieldValue("")) },
                        onOpenDrawer = { coroutineScope.launch { drawerState.open() } })

                    ErrorDialog(
                        errorMessage = errorMessage,
                        filteredProductList = filteredProductList,
                        onRetry = { viewModel.retryFetchProducts() })

                    if (filteredProductList.isNotEmpty()) {
                        if (isGridView) {
                            ProductGrid(
                                viewModel = viewModel
                            )
                        } else {
                            ProductList(products = filteredProductList, viewModel = viewModel)
                        }
                    }
                }
            }
        )
    })
}

