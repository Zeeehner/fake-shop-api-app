package de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.jetpack.a04_05_online_shopper.data.api.CatApi
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.Cat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatViewModel : ViewModel() {

    private val TAG = "CatViewModel"
    val catApi = CatApi.retrofitService

    private val _catList = MutableStateFlow<List<Cat>>(listOf())
    val catList = _catList.asStateFlow()

    fun loadCatImages(searchQuery: String) {
        viewModelScope.launch {
            try {
                _catList.value = catApi.getCatImagesWithHeader(20)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading cat images: $e")
            }
        }
    }
}
