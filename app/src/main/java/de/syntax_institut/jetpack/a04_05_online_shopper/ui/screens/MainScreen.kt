package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.navigation.BottomNavigationBar
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun MainScreen(paddingValues: PaddingValues) {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "products",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("products") {
                ProductScreen(viewModel = productViewModel)
            }
            composable("cart") {
                CartScreen(viewModel = productViewModel)
            }
        }
    }
}