package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AuthUser
import com.example.data.LocalMarketplaceRepository
import com.example.data.MockDataProvider
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Seller
import com.example.model.SellerReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    AUTH_GATE,
    HOME,
    PRODUCT_DETAIL,
    SELLER_STOREFRONT,
    SELLER_UPLOAD,
    WISHLIST
}

data class DialUiState(
    val currentScreen: AppScreen = AppScreen.AUTH_GATE, // Starts at Login / Explore gate
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val searchQuery: String = "",
    val products: List<Product> = emptyList(), // Starts from ZERO items as requested!
    val soldProducts: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val selectedSeller: Seller? = null,
    val selectedSize: String = "",
    val wishlistedIds: Set<String> = emptySet(),
    val activeStorefrontTab: Int = 0, // 0: Active Listings, 1: Sold Items, 2: Buyer Reviews
    val showDialContactSheet: Boolean = false,
    val showMakeOfferDialog: Boolean = false,
    val showRateSellerSheet: Boolean = false,
    val sellerReviews: List<SellerReview> = emptyList(),
    val isRefreshing: Boolean = false,
    val lastRefreshTime: String = "Just now",
    val recentSearches: List<String> = emptyList(),
    val popularTags: List<String> = LocalMarketplaceRepository.POPULAR_TAGS,
    val isSearchDropdownVisible: Boolean = false,
    val currentUser: AuthUser? = null,
    val toastMessage: String? = null
)

class DialViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocalMarketplaceRepository(application)
    private val _uiState = MutableStateFlow(DialUiState())
    val uiState: StateFlow<DialUiState> = _uiState.asStateFlow()

    init {
        loadPersistedState()
    }

    private fun loadPersistedState() {
        val loadedProducts = repository.getProducts()
        val loadedSearches = repository.getRecentSearches()
        val loadedUser = repository.getAuthUser()

        _uiState.update {
            it.copy(
                products = loadedProducts,
                recentSearches = loadedSearches,
                currentUser = loadedUser,
                // If user was previously logged in or clicked explore, they can proceed directly
                currentScreen = if (loadedUser != null) AppScreen.HOME else AppScreen.AUTH_GATE
            )
        }
    }

    // --- Navigation & Auth Flow ---
    fun exploreAsGuest() {
        val guestUser = AuthUser(email = "guest@dialtheplug.com", username = "Guest Explorer", isGuest = true)
        repository.saveAuthUser(guestUser)
        _uiState.update {
            it.copy(
                currentUser = guestUser,
                currentScreen = AppScreen.HOME,
                toastMessage = "Welcome to Dial the Plug! Browsing as Guest."
            )
        }
    }

    fun loginUser(email: String, username: String) {
        val user = AuthUser(email = email, username = username, isGuest = false, authProvider = "EMAIL")
        repository.saveAuthUser(user)
        _uiState.update {
            it.copy(
                currentUser = user,
                currentScreen = AppScreen.HOME,
                toastMessage = "Welcome, @$username! Logged into Plug Concierge."
            )
        }
    }

    fun socialLogin(provider: String) {
        val email = when (provider.uppercase()) {
            "GOOGLE" -> "alex.grails@gmail.com"
            "FACEBOOK" -> "alex.plug@facebook.com"
            else -> "user@dialtheplug.com"
        }
        val username = when (provider.uppercase()) {
            "GOOGLE" -> "AlexGrails"
            "FACEBOOK" -> "AlexPlugFB"
            else -> "VerifiedMember"
        }
        val user = AuthUser(email = email, username = username, isGuest = false, authProvider = provider.uppercase())
        repository.saveAuthUser(user)
        _uiState.update {
            it.copy(
                currentUser = user,
                currentScreen = AppScreen.HOME,
                toastMessage = "⚡ Connected via ${provider.lowercase().replaceFirstChar { c -> c.uppercase() }} as @$username!"
            )
        }
    }

    fun openAuthGate() {
        _uiState.update { it.copy(currentScreen = AppScreen.AUTH_GATE) }
    }

    fun logout() {
        repository.saveAuthUser(null)
        _uiState.update {
            it.copy(
                currentUser = null,
                currentScreen = AppScreen.AUTH_GATE,
                toastMessage = "Logged out successfully."
            )
        }
    }

    // --- Search & Persistent History ---
    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                isSearchDropdownVisible = true
            )
        }
    }

    fun executeSearch(query: String) {
        if (query.isNotBlank()) {
            repository.saveRecentSearch(query)
            _uiState.update {
                it.copy(
                    searchQuery = query,
                    recentSearches = repository.getRecentSearches(),
                    isSearchDropdownVisible = false
                )
            }
        }
    }

    fun setSearchDropdownVisible(visible: Boolean) {
        _uiState.update { it.copy(isSearchDropdownVisible = visible) }
    }

    fun removeRecentSearch(query: String) {
        repository.removeRecentSearch(query)
        _uiState.update { it.copy(recentSearches = repository.getRecentSearches()) }
    }

    fun clearAllRecentSearches() {
        repository.clearRecentSearches()
        _uiState.update { it.copy(recentSearches = emptyList()) }
    }

    fun clearSearchQuery() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                isSearchDropdownVisible = false
            )
        }
    }

    // --- Marketplace Listings & Seller Upload ---
    fun openSellerUpload() {
        _uiState.update { it.copy(currentScreen = AppScreen.SELLER_UPLOAD) }
    }

    fun publishProduct(newProduct: Product) {
        repository.addProduct(newProduct)
        _uiState.update {
            it.copy(
                products = repository.getProducts(),
                currentScreen = AppScreen.HOME,
                toastMessage = "🔥 Drop '${newProduct.title}' published live to the marketplace!"
            )
        }
    }

    fun clearAllMarketplaceItems() {
        repository.clearAllProducts()
        _uiState.update {
            it.copy(
                products = emptyList(),
                selectedProduct = null,
                toastMessage = "Marketplace cleared! Starting clean with 0 items."
            )
        }
    }

    fun loadSampleItems() {
        val samples = repository.loadSampleProducts()
        _uiState.update {
            it.copy(
                products = samples,
                toastMessage = "Loaded sample vault drops for preview."
            )
        }
    }

    fun onCategorySelected(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openProductDetail(product: Product) {
        val reviews = repository.getSellerReviews(product.seller.id)
        _uiState.update {
            it.copy(
                selectedProduct = product,
                selectedSeller = product.seller,
                selectedSize = product.selectedSize,
                sellerReviews = reviews,
                currentScreen = AppScreen.PRODUCT_DETAIL
            )
        }
    }

    fun openSellerStorefront(seller: Seller) {
        val reviews = repository.getSellerReviews(seller.id)
        _uiState.update {
            it.copy(
                selectedSeller = seller,
                sellerReviews = reviews,
                activeStorefrontTab = 0,
                currentScreen = AppScreen.SELLER_STOREFRONT
            )
        }
    }

    fun openWishlist() {
        _uiState.update { it.copy(currentScreen = AppScreen.WISHLIST) }
    }

    fun navigateBack(): Boolean {
        val current = _uiState.value.currentScreen
        if (current != AppScreen.HOME && current != AppScreen.AUTH_GATE) {
            _uiState.update { it.copy(currentScreen = AppScreen.HOME) }
            return true
        }
        return false
    }

    fun toggleWishlist(productId: String) {
        _uiState.update { current ->
            val set = current.wishlistedIds.toMutableSet()
            val added: Boolean
            if (set.contains(productId)) {
                set.remove(productId)
                added = false
            } else {
                set.add(productId)
                added = true
            }
            current.copy(
                wishlistedIds = set,
                toastMessage = if (added) "Saved to your Grail Wishlist" else "Removed from Wishlist"
            )
        }
    }

    fun onSizeSelected(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
    }

    fun setStorefrontTab(tabIndex: Int) {
        _uiState.update { it.copy(activeStorefrontTab = tabIndex) }
    }

    fun showDialContactSheet(show: Boolean) {
        _uiState.update { it.copy(showDialContactSheet = show) }
    }

    fun showMakeOfferDialog(show: Boolean) {
        _uiState.update { it.copy(showMakeOfferDialog = show) }
    }

    fun submitOffer(offerAmount: Int, notes: String) {
        val product = _uiState.value.selectedProduct
        val sellerName = product?.seller?.name ?: "The Plug"
        _uiState.update {
            it.copy(
                showMakeOfferDialog = false,
                toastMessage = "Offer of $$offerAmount sent to $sellerName! Check WhatsApp for reply."
            )
        }
    }

    fun refreshMarketplace() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            kotlinx.coroutines.delay(1000)
            val currentList = repository.getProducts()
            val refreshedList = if (currentList.size > 1) {
                listOf(currentList.last()) + currentList.dropLast(1)
            } else {
                currentList
            }
            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    products = refreshedList,
                    lastRefreshTime = "Just now",
                    toastMessage = "⚡ Plugs synced! Marketplace up to date."
                )
            }
        }
    }

    fun openRateSellerSheet(show: Boolean = true) {
        _uiState.update { it.copy(showRateSellerSheet = show) }
    }

    fun loadSellerReviews(sellerId: String) {
        val reviews = repository.getSellerReviews(sellerId)
        _uiState.update { it.copy(sellerReviews = reviews) }
    }

    fun submitSellerRating(sellerId: String, stars: Int, comment: String, praiseTags: List<String>) {
        val currentBuyerName = _uiState.value.currentUser?.let { "@${it.username}" } ?: "@verified_buyer"
        val review = SellerReview(
            id = "rev_${System.currentTimeMillis()}",
            sellerId = sellerId,
            buyerName = currentBuyerName,
            rating = stars,
            comment = comment,
            praiseTags = praiseTags,
            timestamp = "Just now",
            isVerifiedPurchase = true
        )
        repository.saveSellerReview(review)
        val refreshedProducts = repository.getProducts()
        val refreshedReviews = repository.getSellerReviews(sellerId)

        val updatedSeller = refreshedProducts.find { it.seller.id == sellerId }?.seller
            ?: _uiState.value.selectedSeller?.let { prev ->
                val newAvg = refreshedReviews.map { it.rating }.average().toFloat()
                prev.copy(rating = newAvg, totalDeals = prev.totalDeals + 1)
            }

        _uiState.update {
            it.copy(
                showRateSellerSheet = false,
                products = refreshedProducts,
                sellerReviews = refreshedReviews,
                selectedSeller = updatedSeller ?: it.selectedSeller,
                selectedProduct = it.selectedProduct?.let { prod ->
                    if (prod.seller.id == sellerId && updatedSeller != null) prod.copy(seller = updatedSeller) else prod
                },
                toastMessage = "★ Rated $stars Stars! Trust score updated across Dial the Plug."
            )
        }
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
