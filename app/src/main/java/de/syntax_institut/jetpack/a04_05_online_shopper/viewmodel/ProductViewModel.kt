package de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import de.syntax_institut.jetpack.a04_05_online_shopper.data.api.CatApi
import de.syntax_institut.jetpack.a04_05_online_shopper.data.api.ShopAPI
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.Cat
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val MAX_CART_QUANTITY = 10
    private val TAG = "CatViewModel"
    val catApi = CatApi.retrofitService

    // Cat API
    private val _catList = MutableStateFlow<List<Cat>>(listOf())
    val catList = _catList.asStateFlow()

    // Slider value
    private val _sliderValue = MutableStateFlow(20.0)
    val sliderValue: StateFlow<Double> = _sliderValue.asStateFlow()

    // Limit Items
    private val _itemLimit = MutableStateFlow(20)
    val itemLimit: StateFlow<Int> = _itemLimit.asStateFlow()

    // Grid aktivieren
    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    // Produktliste
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList.asStateFlow()

    // Warenkorb
    private val _cart = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cart: StateFlow<Map<Product, Int>> = _cart.asStateFlow()

    // Lade- und Fehlerzustand
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    var errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Suchfeld
    private val _searchQuery = MutableStateFlow(TextFieldValue(""))
    val searchQuery: StateFlow<TextFieldValue> = _searchQuery.asStateFlow()

    // Kategorie-Filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Preis-Filter
    private val _minPrice = MutableStateFlow("")
    val minPrice: StateFlow<String> = _minPrice.asStateFlow()

    private val _maxPrice = MutableStateFlow("")
    val maxPrice: StateFlow<String> = _maxPrice.asStateFlow()

    // Kombinierte Filterung der Produktliste
    val filteredProductList: StateFlow<List<Product>> = combine(
        _productList, _searchQuery, _selectedCategory, _minPrice, _maxPrice
    ) { products, query, category, minP, maxP ->

        products.filter { product ->
            val matchesQuery =
                query.text.isBlank() || product.title.contains(query.text, ignoreCase = true)
            val matchesCategory = category == null || product.category == category
            val matchesMinPrice = minP.toFloatOrNull()?.let { product.price >= it } ?: true
            val matchesMaxPrice = maxP.toFloatOrNull()?.let { product.price <= it } ?: true

            matchesQuery && matchesCategory && matchesMinPrice && matchesMaxPrice
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchProducts()
    }

    /**
     * Produkte aus der API abrufen
     */
    private fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ShopAPI.retrofitService.getArticles()

                if (response.isSuccessful) {
                    _productList.value = response.body() ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = when (response.code()) {
                        400 -> "Bad Request: The request was invalid."
                        401 -> "Unauthorized: You don't have permission to access this resource."
                        403 -> "Forbidden: Access to this resource is forbidden."
                        404 -> "Not Found: The requested resource could not be found."
                        500 -> "Internal Server Error: A server error occurred."
                        else -> "Server error: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection"
                    is retrofit2.HttpException -> "Server error: ${e.code()}"
                    else -> "Unexpected error: ${e.localizedMessage}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Produkt zum Warenkorb hinzufügen
     */
    fun addToCart(product: Product, quantity: Int = 1) {
        _cart.value = _cart.value.toMutableMap().apply {
            val currentQuantity = this[product] ?: 0
            val newQuantity = (currentQuantity + quantity).coerceAtMost(MAX_CART_QUANTITY)
            if (currentQuantity != newQuantity) {
                this[product] = newQuantity
            }
        }
    }

    /**
     * Produkt aus dem Warenkorb entfernen
     */
    fun removeFromCart(product: Product) {
        _cart.value = _cart.value.toMutableMap().apply {
            remove(product)
        }
    }

    /**
     * Menge eines Produkts im Warenkorb aktualisieren
     */
    fun updateCartItem(product: Product, quantity: Int) {
        when {
            quantity <= 0 -> removeFromCart(product)
            quantity > MAX_CART_QUANTITY -> addToCart(product, MAX_CART_QUANTITY)
            else -> _cart.value = _cart.value.toMutableMap().apply {
                if (this[product] != quantity) {
                    this[product] = quantity
                }
            }
        }
    }

    /**
     * Suchanfrage aktualisieren
     */
    fun updateSearchQuery(newQuery: TextFieldValue) {
        _searchQuery.value = newQuery
    }

    /**
     * Suchanfrage löschen
     */
    fun clearSearchQuery() {
        _searchQuery.value = TextFieldValue("")
    }

    /**
     * Kategorie aktualisieren
     */
    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    /**
     * Preisbereich aktualisieren
     */
    fun updatePriceRange(min: String, max: String) {
        _minPrice.value = min
        _maxPrice.value = max
    }

    /**
     * Filter zurücksetzen
     */
    fun clearFilters() {
        _selectedCategory.value = null
        _minPrice.value = ""
        _maxPrice.value = ""
    }

    /**
     * Grid aktivieren
     */
    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    /**
     * SliderValue
     */
    fun updateSliderValue(a: Double) {
        _sliderValue.update { a }
    }

    /**
     * Produkte erneut abrufen
     */
    fun retryFetchProducts() {
        fetchProducts()
    }

    /**
     * Katzenbilder laden
     */
    fun loadCatImages(searchQuery: String) {
        viewModelScope.launch {
            try {
                _catList.value = catApi.getCatImagesWithHeader(500)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading cat images: $e")
            }
        }
    }
}
