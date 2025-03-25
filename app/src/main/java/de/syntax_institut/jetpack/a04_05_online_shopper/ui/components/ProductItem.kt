package de.syntax_institut.jetpack.a04_05_online_shopper.ui.components

import android.R.attr.label
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@Composable
fun ProductItem(
    product: Product,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    viewModel: ProductViewModel
) {
    var quantity by remember { mutableStateOf(1) }

    val cardElevation by animateDpAsState(
        targetValue = if (isExpanded) 12.dp else 4.dp,
        label = "Card Elevation"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = cardElevation),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onExpandClick() }
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "${product.price}€", style = MaterialTheme.typography.bodyMedium)

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (quantity > 1) quantity-- }) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$quantity", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { quantity++ }) {
                            Text("+")
                        }
                    }
                    Button(onClick = { viewModel.addToCart(product, quantity) }) {
                        Text("Add to cart")
                    }
                }
            }
        }
    }
}