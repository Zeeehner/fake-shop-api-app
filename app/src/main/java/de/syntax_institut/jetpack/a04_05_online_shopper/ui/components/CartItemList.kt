package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product

@Composable
fun CartItemsList(
    cart: Map<Product, Int>,
    modifier: Modifier = Modifier,
    onIncrease: (Product, Int) -> Unit,
    onDecrease: (Product, Int) -> Unit,
    onRemove: (Product) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cart.toList()) { (product, quantity) ->
            CartItemCard(
                product = product,
                quantity = quantity,
                onIncrease = { onIncrease(product, quantity) },
                onDecrease = { onDecrease(product, quantity) },
                onRemove = { onRemove(product) }
            )
        }
    }
}
