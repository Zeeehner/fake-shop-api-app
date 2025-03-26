package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.CartBottomBar
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.CartItemsList
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.EmptyCartContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: ProductViewModel = viewModel()) {
    val cart by viewModel.cart.collectAsState()
    val totalPrice = cart.map { (product, quantity) -> product.price * quantity }.sum()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    "Shopping cart",
                    style = MaterialTheme.typography.displayLarge,
                    fontStyle = Italic,
                    fontWeight = ExtraBold
                )
            })
    }, bottomBar = {
        AnimatedVisibility(
            visible = cart.isNotEmpty(), exit = slideOutVertically(
                targetOffsetY = { it }, animationSpec = tween(durationMillis = 300)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            CartBottomBar(
                totalPrice = totalPrice, onCheckout = {
                    // placeholder
                })
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (cart.isEmpty()) {
                EmptyCartContent(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CartItemsList(
                    cart = cart,
                    modifier = Modifier.fillMaxSize(),
                    onIncrease = { product, quantity ->
                        viewModel.updateCartItem(
                            product, quantity + 1
                        )
                    },
                    onDecrease = { product, quantity ->
                        viewModel.updateCartItem(
                            product, quantity - 1
                        )
                    },
                    onRemove = { product -> viewModel.removeFromCart(product) })
            }
        }
    }
}
