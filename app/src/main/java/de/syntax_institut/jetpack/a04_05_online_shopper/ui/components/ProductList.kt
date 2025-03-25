package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun ProductList(products: List<Product>, viewModel: ProductViewModel) {
    var expandedProduct by remember { mutableStateOf<Product?>(null) }

    LazyColumn {
        items(products) { product ->
            ProductItem(
                product = product,
                isExpanded = expandedProduct == product,
                onExpandClick = {
                    expandedProduct = if (expandedProduct == product) null else product
                },
                viewModel = viewModel
            )
        }
    }
}
