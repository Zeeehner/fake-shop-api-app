package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.syntax_institut.jetpack.a04_05_online_shopper.ui.components.ProductItem
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel) {

    val productList by viewModel.productList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            AlertDialog(
                onDismissRequest = { viewModel.errorMessage },
                title = { Text("Error") },
                text = { Text(errorMessage ?: "An unknown error occurred") },
                confirmButton = {
                    TextButton(onClick = { viewModel.retryFetchProducts() }) {
                        Text("Retry")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.errorMessage }) {
                        Text("Dismiss")
                    }
                }
            )
        }
        productList.isNotEmpty() -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productList, key = { it.id }) { product ->
                    ProductItem(product)
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products available",
                    color = Color.Gray
                )
            }
        }
    }
}

