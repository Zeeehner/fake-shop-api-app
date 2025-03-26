package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product

@Composable
fun ProductListWithGridSwitch(
    products: List<Product>,
    viewModel: ProductViewModel
) {
    var isGridView by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { isGridView != isGridView }
            ) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.List else Icons.Default.ArrowDropDown,
                    contentDescription = "Switch View",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isGridView) {
            ProductGrid(viewModel = viewModel)
        } else {
            ProductList(products = products, viewModel = viewModel)
        }
    }
}
