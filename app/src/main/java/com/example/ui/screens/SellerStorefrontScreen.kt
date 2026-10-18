package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Product
import com.example.model.Seller
import com.example.model.SellerReview
import com.example.ui.components.ConditionChip
import com.example.ui.components.LivePlugStatusPulse
import com.example.ui.components.StarRatingBar
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.launchPhoneDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GhostSecondary
import com.example.ui.theme.GhostWhite
import com.example.ui.theme.NeonLime
import com.example.ui.theme.Persian
import com.example.ui.theme.PersianBlueGlow
import com.example.ui.theme.PersianBlueLight
import com.example.ui.theme.PersianContainer
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.StarGold
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextLightGrey
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WhatsAppColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SellerStorefrontScreen(
    seller: Seller,
    activeListings: List<Product>,
    soldListings: List<Product>,
    activeTab: Int,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onQuickDialClick: (Product) -> Unit,
    onRatePlugClick: () -> Unit = {},
    sellerReviews: List<SellerReview> = emptyList()
) {
    val context = LocalContext.current
    BackHandler { onBackClick() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header span containing Banner, Logo, Bio, Star Rating, and Stats
            item(span = { GridItemSpan(2) }) {
                StorefrontHeader(
                    seller = seller,
                    onBackClick = onBackClick,
                    onRatePlugClick = onRatePlugClick,
                    onCallClick = { launchPhoneDialer(context, seller.phoneNumber) },
                    onWhatsAppClick = {
                        val msg = "Yo! Inquiring on ${seller.name}'s vault listings via Dial the Plug."
                        launchWhatsApp(context, seller.whatsappNumber, msg)
                    }
                )
            }

            // Tab bar: Active Listings vs. Sold Items vs. Buyer Reviews
            item(span = { GridItemSpan(2) }) {
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = DarkSurface,
                    contentColor = GhostWhite,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = GhostWhite,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = activeTab == 0,
                        onClick = { onTabSelected(0) },
                        modifier = Modifier.testTag("storefront_active_tab"),
                        text = {
                            Text(
                                text = "ACTIVE (${activeListings.size})",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (activeTab == 0) GhostWhite else GhostSecondary
                            )
                        }
                    )
                    Tab(
                        selected = activeTab == 1,
                        onClick = { onTabSelected(1) },
                        modifier = Modifier.testTag("storefront_sold_tab"),
                        text = {
                            Text(
                                text = "SOLD (${soldListings.size})",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (activeTab == 1) GhostWhite else GhostSecondary
                            )
                        }
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { onTabSelected(2) },
                        modifier = Modifier.testTag("storefront_reviews_tab"),
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = StarGold,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "REVIEWS (${sellerReviews.size})",
                                    fontFamily = SyneFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (activeTab == 2) GhostWhite else GhostSecondary
                                )
                            }
                        }
                    )
                }
            }

            // Tab Content
            when (activeTab) {
                0 -> {
                    // Active Listings Grid
                    if (activeListings.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            EmptyStorefrontState(message = "No active listings right now.")
                        }
                    } else {
                        items(activeListings, key = { it.id }) { product ->
                            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                                StorefrontProductCard(
                                    product = product,
                                    isSold = false,
                                    onProductClick = { onProductClick(product) },
                                    onQuickDialClick = { onQuickDialClick(product) }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // Sold Items Grid
                    if (soldListings.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            EmptyStorefrontState(message = "No sold history records yet.")
                        }
                    } else {
                        items(soldListings, key = { it.id }) { product ->
                            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                                StorefrontProductCard(
                                    product = product,
                                    isSold = true,
                                    onProductClick = { onProductClick(product) },
                                    onQuickDialClick = { onQuickDialClick(product) }
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // Buyer Reviews & Star Rating System Tab
                    item(span = { GridItemSpan(2) }) {
                        SellerReviewsHeaderCard(
                            seller = seller,
                            reviewsCount = sellerReviews.size,
                            onRatePlugClick = onRatePlugClick
                        )
                    }

                    if (sellerReviews.isEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            EmptyStorefrontState(message = "No buyer reviews yet. Be the first to rate this plug!")
                        }
                    } else {
                        items(sellerReviews, key = { it.id }) { review ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                BuyerReviewCard(review = review)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StorefrontHeader(
    seller: Seller,
    onBackClick: () -> Unit,
    onRatePlugClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Cover banner container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF141720))
        ) {
            Image(
                painter = painterResource(id = seller.coverBannerRes ?: R.drawable.img_store_banner),
                contentDescription = "${seller.name} cover banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x99000000),
                                Color.Transparent,
                                Color(0xF0090A0D)
                            )
                        )
                    )
            )

            // Back button on banner
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x99000000))
                    .align(Alignment.TopStart)
                    .testTag("storefront_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }

            // Live status badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                LivePlugStatusPulse(label = "ONLINE NOW")
            }
        }

        // Plug Identity & Stats Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Overlapping avatar and contact buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-32).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(3.dp, DarkBackground, CircleShape)
                        .border(1.5.dp, PersianBlueGlow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seller.avatarEmoji,
                        fontSize = 36.sp
                    )
                }

                // Quick Contact Actions in header
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onWhatsAppClick,
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppColor),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("storefront_header_whatsapp")
                    ) {
                        Text(text = "💬", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "WhatsApp",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = onCallClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Persian),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("storefront_header_call")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = GhostWhite,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Call Plug",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GhostWhite
                        )
                    }
                }
            }

            // Name and Verified Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(y = (-20).dp)
            ) {
                Text(
                    text = seller.name,
                    fontFamily = SyneFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GhostWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                VerifiedBadge()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(y = (-18).dp)
            ) {
                Text(
                    text = seller.handle,
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 12.sp,
                    color = PersianBlueGlow
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = TextLightGrey,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = seller.location,
                    fontSize = 12.sp,
                    color = TextLightGrey
                )
            }

            // Bio
            Text(
                text = seller.bio,
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = TextLightGrey,
                modifier = Modifier.offset(y = (-12).dp)
            )

            // Star Rating & Trust Signals Action Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-6).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarRatingBar(rating = seller.rating, starSize = 16.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "%.2f".format(seller.rating),
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = StarGold
                    )
                    Text(
                        text = " (${seller.totalDeals} Deals)",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 11.sp,
                        color = GhostSecondary
                    )
                }

                Button(
                    onClick = onRatePlugClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Persian),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 5.dp),
                    modifier = Modifier.testTag("storefront_rate_plug_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RATE PLUG",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GhostWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 4-Column Stats Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StoreStatItem("DEALS", "${seller.totalDeals}+")
                StoreStatItem("RATING", "★ %.2f".format(seller.rating))
                StoreStatItem("AUTH SCORE", "%.1f%%".format(seller.authScore))
                StoreStatItem("RESPONSE", seller.responseTime)
            }
        }
    }
}

@Composable
fun StoreStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = SyneFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = GhostWhite
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = TextMuted
        )
    }
}

@Composable
fun SellerReviewsHeaderCard(
    seller: Seller,
    reviewsCount: Int,
    onRatePlugClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "%.2f".format(seller.rating),
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = GhostWhite
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            StarRatingBar(rating = seller.rating, starSize = 16.dp)
                            Text(
                                text = "Based on $reviewsCount verified buyer reviews",
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 11.sp,
                                color = GhostSecondary
                            )
                        }
                    }
                }

                Button(
                    onClick = onRatePlugClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Persian),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("submit_review_tab_button")
                ) {
                    Text(
                        text = "+ WRITE REVIEW",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GhostWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Trust highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PersianContainer)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛡️ 99.8% Auth Guaranteed",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PersianContainer)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ Same-Day Courier",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GhostWhite
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuyerReviewCard(review: SellerReview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("buyer_review_card_${review.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.dp, PersianBlueGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = review.buyerName,
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = GhostWhite
                            )
                            if (review.isVerifiedPurchase) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Verified Purchase",
                                    tint = PersianBlueGlow,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }
                        Text(
                            text = review.timestamp,
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                StarRatingBar(rating = review.rating.toFloat(), starSize = 13.dp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = review.comment,
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = GhostWhite
            )

            if (review.praiseTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    review.praiseTags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, DarkCardBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "✓ $tag",
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = PersianBlueGlow
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStorefrontState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = 13.sp,
            color = TextLightGrey
        )
    }
}

@Composable
fun StorefrontProductCard(
    product: Product,
    isSold: Boolean,
    onProductClick: () -> Unit,
    onQuickDialClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("storefront_product_${product.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .background(Color(0xFF0F1116))
            ) {
                Image(
                    painter = painterResource(id = product.primaryImageRes),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (isSold) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xB3000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SOLD OUT",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GhostWhite,
                            modifier = Modifier
                                .border(1.dp, GhostWhite, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    ) {
                        ConditionChip(condition = product.condition)
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.brand.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = PersianBlueGlow
                )
                Text(
                    text = product.title,
                    fontFamily = SyneFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price}",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GhostWhite
                    )

                    if (!isSold) {
                        Button(
                            onClick = onQuickDialClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Persian),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = "DIAL",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = GhostWhite
                            )
                        }
                    }
                }
            }
        }
    }
}
