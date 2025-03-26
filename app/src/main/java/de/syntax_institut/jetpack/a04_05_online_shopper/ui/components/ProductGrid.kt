package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun ProductGrid(viewModel: ProductViewModel) {

    val products = viewModel.filteredProductList.collectAsState(initial = emptyList()).value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(products) { index, product ->
            ProductThumbnail(product = product)
        }
    }
}
