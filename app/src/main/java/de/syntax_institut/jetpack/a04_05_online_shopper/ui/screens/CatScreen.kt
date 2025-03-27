package de.syntax_institut.jetpack.a04_05_online_shopper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel.CatViewModel

@Composable
fun CatScreen(
    modifier: Modifier = Modifier,
    viewModel: CatViewModel = viewModel(),
    searchQuery: String
) {

    viewModel.loadCatImages(searchQuery)

    val catList by viewModel.catList.collectAsState()

    if (catList.isEmpty()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(catList) { catImage ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = catImage.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(catImage.width.toFloat() / catImage.height.toFloat()),
                    loading = {
                        CircularProgressIndicator(
                            Modifier.scale(0.5f)
                        )
                    }
                )
            }
        }
    }
}
