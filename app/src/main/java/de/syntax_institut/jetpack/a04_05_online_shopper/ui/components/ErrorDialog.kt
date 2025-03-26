package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product

@Composable
fun ErrorDialog(
    errorMessage: String?,
    filteredProductList: List<Product>,
    onRetry: () -> Unit
) {
    if (filteredProductList.isEmpty() && errorMessage != null) {
        AlertDialog(
            onDismissRequest = {
                // Placeholder
            },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Placeholder
                }) {
                    Text("Dismiss")
                }
            }
        )
    }
}
