package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.AppScreen
import com.example.ui.DialViewModel
import com.example.ui.dialogs.DialContactSheet
import com.example.ui.dialogs.MakeOfferDialog
import com.example.ui.dialogs.RateSellerSheet
import com.example.ui.screens.AuthGateScreen
import com.example.ui.screens.HomeFeedScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.SellerStorefrontScreen
import com.example.ui.screens.SellerUploadScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DialThePlugTheme

@androidx.compose.material3.ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val viewModel: DialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialThePlugTheme {
                DialAppRoot(viewModel = viewModel)
            }
        }
    }
}

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun DialAppRoot(viewModel: DialViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Show toast message if emitted
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        when (uiState.currentScreen) {
            AppScreen.AUTH_GATE -> {
                AuthGateScreen(
                    onExploreAsGuest = { viewModel.exploreAsGuest() },
                    onLoginSuccess = { email, username -> viewModel.loginUser(email, username) },
                    onSocialLogin = { provider -> viewModel.socialLogin(provider) }
                )
            }

            AppScreen.HOME -> {
                HomeFeedScreen(
                    products = uiState.products,
                    selectedCategory = uiState.selectedCategory,
                    searchQuery = uiState.searchQuery,
                    wishlistedIds = uiState.wishlistedIds,
                    isRefreshing = uiState.isRefreshing,
                    recentSearches = uiState.recentSearches,
                    popularTags = uiState.popularTags,
                    isSearchDropdownVisible = uiState.isSearchDropdownVisible,
                    currentUser = uiState.currentUser,
                    onRefresh = { viewModel.refreshMarketplace() },
                    onCategorySelected = { viewModel.onCategorySelected(it) },
                    onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    onExecuteSearch = { viewModel.executeSearch(it) },
                    onRemoveRecentSearch = { viewModel.removeRecentSearch(it) },
                    onClearAllRecentSearches = { viewModel.clearAllRecentSearches() },
                    onDismissSearchDropdown = { viewModel.setSearchDropdownVisible(false) },
                    onClearSearch = { viewModel.clearSearchQuery() },
                    onProductClick = { viewModel.openProductDetail(it) },
                    onSellerClick = { viewModel.openSellerStorefront(it) },
                    onQuickDialClick = { product ->
                        viewModel.openProductDetail(product)
                        viewModel.showDialContactSheet(true)
                    },
                    onToggleWishlist = { viewModel.toggleWishlist(it) },
                    onOpenWishlist = { viewModel.openWishlist() },
                    onOpenSellerUpload = { viewModel.openSellerUpload() },
                    onOpenAuthGate = { viewModel.openAuthGate() },
                    onClearAllItems = { viewModel.clearAllMarketplaceItems() },
                    onLoadSampleItems = { viewModel.loadSampleItems() }
                )
            }

            AppScreen.SELLER_UPLOAD -> {
                SellerUploadScreen(
                    onBackClick = { viewModel.navigateBack() },
                    onProductPublished = { newProduct -> viewModel.publishProduct(newProduct) }
                )
            }

            AppScreen.PRODUCT_DETAIL -> {
                uiState.selectedProduct?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        selectedSize = uiState.selectedSize,
                        isWishlisted = uiState.wishlistedIds.contains(product.id),
                        onBackClick = { viewModel.navigateBack() },
                        onSizeSelected = { viewModel.onSizeSelected(it) },
                        onSellerClick = { viewModel.openSellerStorefront(it) },
                        onDialPlugClick = { viewModel.showDialContactSheet(true) },
                        onMakeOfferClick = { viewModel.showMakeOfferDialog(true) },
                        onToggleWishlist = { viewModel.toggleWishlist(product.id) },
                        onRatePlugClick = { viewModel.openRateSellerSheet(true) }
                    )
                } ?: run {
                    viewModel.navigateBack()
                }
            }

            AppScreen.SELLER_STOREFRONT -> {
                val seller = uiState.selectedSeller ?: uiState.selectedProduct?.seller
                if (seller != null) {
                    val activeListings = uiState.products.filter { it.seller.id == seller.id && !it.isSold }
                    val soldListings = uiState.soldProducts.filter { it.seller.id == seller.id }
                    SellerStorefrontScreen(
                        seller = seller,
                        activeListings = activeListings,
                        soldListings = soldListings,
                        sellerReviews = uiState.sellerReviews,
                        activeTab = uiState.activeStorefrontTab,
                        onTabSelected = { viewModel.setStorefrontTab(it) },
                        onBackClick = { viewModel.navigateBack() },
                        onProductClick = { viewModel.openProductDetail(it) },
                        onQuickDialClick = { product ->
                            viewModel.openProductDetail(product)
                            viewModel.showDialContactSheet(true)
                        },
                        onRatePlugClick = { viewModel.openRateSellerSheet(true) }
                    )
                } else {
                    viewModel.navigateBack()
                }
            }

            AppScreen.WISHLIST -> {
                val wishlisted = uiState.products.filter { uiState.wishlistedIds.contains(it.id) }
                WishlistScreen(
                    wishlistedProducts = wishlisted,
                    onBackClick = { viewModel.navigateBack() },
                    onProductClick = { viewModel.openProductDetail(it) },
                    onRemoveFromWishlist = { viewModel.toggleWishlist(it) },
                    onQuickDialClick = { product ->
                        viewModel.openProductDetail(product)
                        viewModel.showDialContactSheet(true)
                    }
                )
            }
        }

        // Contact & Dial Sheet
        if (uiState.showDialContactSheet) {
            val product = uiState.selectedProduct
            val seller = product?.seller ?: uiState.selectedSeller
            if (seller != null) {
                DialContactSheet(
                    product = product,
                    seller = seller,
                    selectedSize = uiState.selectedSize,
                    onDismiss = { viewModel.showDialContactSheet(false) },
                    onMakeOfferClick = {
                        viewModel.showDialContactSheet(false)
                        viewModel.showMakeOfferDialog(true)
                    }
                )
            }
        }

        // Make Offer Dialog
        if (uiState.showMakeOfferDialog && uiState.selectedProduct != null) {
            MakeOfferDialog(
                product = uiState.selectedProduct!!,
                selectedSize = uiState.selectedSize,
                onDismiss = { viewModel.showMakeOfferDialog(false) },
                onSubmitOffer = { amount, notes ->
                    viewModel.submitOffer(amount, notes)
                }
            )
        }

        // Rate Seller Bottom Sheet
        if (uiState.showRateSellerSheet) {
            val seller = uiState.selectedSeller ?: uiState.selectedProduct?.seller
            if (seller != null) {
                RateSellerSheet(
                    seller = seller,
                    onDismiss = { viewModel.openRateSellerSheet(false) },
                    onSubmitReview = { rating, comment, praiseTags ->
                        viewModel.submitSellerRating(seller.id, rating, comment, praiseTags)
                    }
                )
            }
        }
    }
}
