package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AuthUser
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Seller
import com.example.ui.components.ConditionChip
import com.example.ui.components.LivePlugStatusPulse
import com.example.ui.components.SearchDropdownMenu
import com.example.ui.components.StreetwearPullToRefreshIndicator
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.Ghost
import com.example.ui.theme.GhostSecondary
import com.example.ui.theme.GhostWhite
import com.example.ui.theme.HypeOrange
import com.example.ui.theme.Persian
import com.example.ui.theme.PersianBlueGlow
import com.example.ui.theme.PersianBlueLight
import com.example.ui.theme.PersianBlueVibrant
import com.example.ui.theme.PersianContainer
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextLightGrey
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen(
    products: List<Product>,
    selectedCategory: ProductCategory,
    searchQuery: String,
    wishlistedIds: Set<String>,
    isRefreshing: Boolean,
    recentSearches: List<String>,
    popularTags: List<String>,
    isSearchDropdownVisible: Boolean,
    currentUser: AuthUser?,
    onRefresh: () -> Unit,
    onCategorySelected: (ProductCategory) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onExecuteSearch: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearAllRecentSearches: () -> Unit,
    onDismissSearchDropdown: () -> Unit,
    onClearSearch: () -> Unit,
    onProductClick: (Product) -> Unit,
    onSellerClick: (Seller) -> Unit,
    onQuickDialClick: (Product) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onOpenWishlist: () -> Unit,
    onOpenSellerUpload: () -> Unit,
    onOpenAuthGate: () -> Unit,
    onClearAllItems: () -> Unit,
    onLoadSampleItems: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val pullRefreshState = rememberPullToRefreshState()
    var showMenu by remember { mutableStateOf(false) }

    val refreshTransition = rememberInfiniteTransition(label = "refresh_spin")
    val spinAngle by refreshTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    // Filter products based on search query and category
    val filteredProducts = products.filter { product ->
        val matchesCategory = (selectedCategory == ProductCategory.ALL) || (product.category == selectedCategory)
        val matchesQuery = searchQuery.isBlank() ||
                product.title.contains(searchQuery, ignoreCase = true) ||
                product.brand.contains(searchQuery, ignoreCase = true) ||
                product.seller.name.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenSellerUpload,
                containerColor = Persian,
                contentColor = GhostWhite,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .navigationBarsPadding()
                    .border(1.5.dp, GhostWhite, RoundedCornerShape(16.dp))
                    .testTag("home_seller_upload_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Drop Item",
                        tint = GhostWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DROP ITEM",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GhostWhite
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            // Top App Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Persian)
                            .border(1.5.dp, GhostWhite, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_plug_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "DIAL THE PLUG",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            letterSpacing = (-0.5).sp,
                            color = GhostWhite
                        )
                        Text(
                            text = if (currentUser?.isGuest == true) "BROWSING AS GUEST" else "PLUG CONCIERGE LIVE",
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp,
                            color = PersianBlueGlow
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick "+ DROP" Header Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Persian)
                            .border(1.dp, GhostWhite.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable { onOpenSellerUpload() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("home_header_drop_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Drop Item",
                                tint = GhostWhite,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "SELL",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = GhostWhite
                            )
                        }
                    }

                    // Refresh Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.dp, if (isRefreshing) PersianBlueLight else DarkCardBorder, CircleShape)
                            .clickable { onRefresh() }
                            .padding(7.dp)
                            .testTag("home_refresh_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync",
                            tint = if (isRefreshing) GhostWhite else PersianBlueGlow,
                            modifier = Modifier
                                .size(18.dp)
                                .rotate(if (isRefreshing) spinAngle else 0f)
                        )
                    }

                    // Wishlist Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DarkCardBorder, CircleShape)
                            .clickable { onOpenWishlist() }
                            .padding(7.dp)
                            .testTag("home_wishlist_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Wishlist",
                            tint = if (wishlistedIds.isNotEmpty()) HypeOrange else GhostSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // More Options Menu (Zero-item reset & Auth gate)
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(34.dp).testTag("home_menu_options")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = GhostSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(DarkSurface)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = if (currentUser?.isGuest == true) "Sign In / Register" else "Account: ${currentUser?.username ?: "User"}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GhostWhite
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onOpenAuthGate()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Clear All Items (Reset to 0)",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFF5252)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onClearAllItems()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Load Sample Demo Grails",
                                        fontSize = 12.sp,
                                        color = PersianBlueGlow
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onLoadSampleItems()
                                }
                            )
                        }
                    }
                }
            }

            // Enhanced Search Bar with Persistent Dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                onSearchQueryChanged(searchQuery)
                            }
                        }
                        .testTag("home_search_bar"),
                    placeholder = {
                        Text(
                            text = "Search Kicks, Kits, Drip, Plugs...",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = PersianBlueGlow,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = onClearSearch) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = GhostSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        onExecuteSearch(searchQuery)
                        focusManager.clearFocus()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GhostWhite,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = GhostWhite,
                        unfocusedTextColor = GhostWhite,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Search Dropdown Overlay: Shows Recent Searches & Popular Tags
            SearchDropdownMenu(
                isVisible = isSearchDropdownVisible,
                recentSearches = recentSearches,
                popularTags = popularTags,
                onSelectQuery = { selectedTag ->
                    onExecuteSearch(selectedTag)
                    focusManager.clearFocus()
                },
                onRemoveRecentSearch = onRemoveRecentSearch,
                onClearAllRecentSearches = onClearAllRecentSearches,
                onDismiss = onDismissSearchDropdown
            )

            // Category Pills (Kicks, Kits, Drip, Accessories)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ProductCategory.values()) { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) {
                                    Brush.horizontalGradient(listOf(Persian, PersianBlueVibrant))
                                } else {
                                    Brush.linearGradient(listOf(DarkSurfaceVariant, DarkSurfaceVariant))
                                }
                            )
                            .border(
                                1.dp,
                                if (isSelected) GhostWhite else DarkCardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { onCategorySelected(category) }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                            .testTag("category_pill_${category.name.lowercase()}")
                    ) {
                        Text(
                            text = category.displayName.uppercase(),
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp,
                            color = if (isSelected) GhostWhite else GhostSecondary
                        )
                    }
                }
            }

            // Pull-To-Refresh Indicator Banner
            StreetwearPullToRefreshIndicator(
                isRefreshing = isRefreshing,
                onManualRefresh = onRefresh
            )

            // Pull-to-Refresh Container wrapping the Feed
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                state = pullRefreshState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("home_pull_to_refresh_box")
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Bottom padding for FAB
                ) {
                    // Editorial Hero Banner
                    item(span = { GridItemSpan(2) }) {
                        HeroEditorialBanner(
                            onBannerClick = { onOpenSellerUpload() }
                        )
                    }

                    // Headline Row
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (selectedCategory == ProductCategory.ALL) "CURATED GRAILS" else "${selectedCategory.displayName.uppercase()} VAULT",
                                    fontFamily = SyneFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    letterSpacing = 0.5.sp,
                                    color = GhostWhite
                                )
                                Text(
                                    text = "${filteredProducts.size} drops available in hand",
                                    fontSize = 11.sp,
                                    color = PersianBlueGlow
                                )
                            }
                            LivePlugStatusPulse(label = "DIRECT DIAL ON")
                        }
                    }

                    // ZERO-ITEMS STATE or Empty Filter Results
                    if (filteredProducts.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            ZeroItemsMarketplaceState(
                                isSearchFilterActive = searchQuery.isNotBlank() || selectedCategory != ProductCategory.ALL,
                                onUploadClick = onOpenSellerUpload,
                                onLoadSamplesClick = onLoadSampleItems,
                                onResetFilter = {
                                    onClearSearch()
                                    onCategorySelected(ProductCategory.ALL)
                                }
                            )
                        }
                    } else {
                        items(filteredProducts, key = { it.id }) { product ->
                            ProductGridCard(
                                product = product,
                                isWishlisted = wishlistedIds.contains(product.id),
                                onProductClick = { onProductClick(product) },
                                onQuickDialClick = { onQuickDialClick(product) },
                                onToggleWishlist = { onToggleWishlist(product.id) },
                                onSellerClick = { onSellerClick(product.seller) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZeroItemsMarketplaceState(
    isSearchFilterActive: Boolean,
    onUploadClick: () -> Unit,
    onLoadSamplesClick: () -> Unit,
    onResetFilter: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 8.dp)
            .testTag("zero_items_state_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PersianBlueLight.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(PersianContainer)
                    .border(1.5.dp, PersianBlueGlow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡",
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isSearchFilterActive) "NO MATCHING DROPS" else "THE VAULT IS CLEAN",
                fontFamily = SyneFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = GhostWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isSearchFilterActive) {
                    "No items match your active search filter. Reset filters to see all available drops."
                } else {
                    "Marketplace initialized with zero items. You are the plug! Drop your first streetwear or sneaker grail below."
                },
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = GhostSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isSearchFilterActive) {
                Button(
                    onClick = onResetFilter,
                    colors = ButtonDefaults.buttonColors(containerColor = Persian),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Reset Search Filters",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite
                    )
                }
            } else {
                Button(
                    onClick = onUploadClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("zero_state_upload_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Persian),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = GhostWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DROP YOUR FIRST GRAIL",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GhostWhite
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onLoadSamplesClick,
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PersianBlueGlow)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = PersianBlueGlow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Load Sample Demo Items",
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PersianBlueGlow
                    )
                }
            }
        }
    }
}

@Composable
fun HeroEditorialBanner(
    onBannerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .clickable { onBannerClick() }
            .testTag("hero_editorial_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PersianBlueLight.copy(alpha = 0.6f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Drop Radar Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // High contrast gradient overlay infused with Persian Night
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x9927187E),
                                Color(0xF50C091C)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Persian)
                            .border(1.dp, GhostWhite, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "DROP RADAR",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = GhostWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "● 18 PLUGS ONLINE NOW",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "LIMITED ARCHIVE VAULT",
                    fontFamily = SyneFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = GhostWhite,
                    letterSpacing = (-0.2).sp
                )
                Text(
                    text = "Direct connection to verified Tokyo & NYC deadstock plugs. Tap to drop new heat.",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 11.sp,
                    color = GhostSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ProductGridCard(
    product: Product,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onQuickDialClick: () -> Unit,
    onToggleWishlist: () -> Unit,
    onSellerClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image Container with Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.05f)
                    .background(Color(0xFF100D24))
            ) {
                Image(
                    painter = painterResource(id = product.primaryImageRes),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Condition chip top-left
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    ConditionChip(condition = if (product.condition.contains("Deadstock")) "DS" else "VNDS")
                }

                // Wishlist Heart button top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0x99100D24))
                        .border(1.dp, DarkCardBorder, CircleShape)
                        .clickable { onToggleWishlist() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Save grail",
                        tint = if (isWishlisted) HypeOrange else GhostWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Size chip overlay bottom-left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Persian.copy(alpha = 0.85f))
                        .border(1.dp, GhostWhite.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = product.selectedSize,
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite
                    )
                }
            }

            // Details section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Seller Name with Verified Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSellerClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.seller.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = GhostSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    VerifiedBadge(isCompact = true)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Item Title
                Text(
                    text = product.title,
                    fontFamily = SyneFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = GhostWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price Row and Quick Dial Action in Persian & Ghost
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$${product.price}",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = GhostWhite
                        )
                        if (product.originalPrice != null && product.originalPrice > product.price) {
                            Text(
                                text = "$${product.originalPrice}",
                                fontSize = 11.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = TextMuted
                            )
                        }
                    }

                    // Quick Dial button in Persian Blue with Ghost White
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Persian)
                            .border(1.dp, GhostWhite.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { onQuickDialClick() }
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                            .testTag("quick_dial_${product.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Dial Plug",
                                tint = GhostWhite,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "DIAL",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = GhostWhite
                            )
                        }
                    }
                }
            }
        }
    }
}
