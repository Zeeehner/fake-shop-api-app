package de.syntax_institut.jetpack.a04_05_online_shopper.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax_institut.jetpack.a04_05_online_shopper.data.api.ShopAPI
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val MAX_CART_QUANTITY = 10

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
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
                val products = ShopAPI.retrofitService.getArticles()
                _productList.value = products
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is java.net.UnknownHostException -> "Keine Internetverbindung"
                    is retrofit2.HttpException -> "Serverfehler: ${e.code()}"
                    else -> "Unerwarteter Fehler: ${e.localizedMessage}"
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
     * Produkte erneut abrufen
     */
    fun retryFetchProducts() {
        fetchProducts()
    }
}
