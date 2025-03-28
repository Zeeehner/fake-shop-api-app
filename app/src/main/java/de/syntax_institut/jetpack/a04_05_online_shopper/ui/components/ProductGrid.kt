package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun ProductGrid(viewModel: ProductViewModel) {
    val products by viewModel.filteredProductList.collectAsStateWithLifecycle()
    val itemLimit by viewModel.itemLimit.collectAsStateWithLifecycle()

    val limitedProducts = products.take(itemLimit)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(limitedProducts) { index, product ->
            AnimatedVisibility(visible = true) {
                ProductThumbnail(product = product)
            }
        }
    }
}