package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(),
    searchQuery: String,
    onBackPressed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadCatImages(searchQuery)
    }

    val catList by viewModel.catList.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Cat Images") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (catList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.scale(1.5f))
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(catList) { catImage ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RectangleShape),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = catImage.url,
                            contentDescription = "Cat Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(catImage.width.toFloat() / catImage.height.toFloat()),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        Modifier.scale(0.5f)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
