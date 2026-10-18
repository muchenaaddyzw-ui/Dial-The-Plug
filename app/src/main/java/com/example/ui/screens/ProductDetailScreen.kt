package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Product
import com.example.model.Seller
import com.example.ui.components.ConditionChip
import com.example.ui.components.LivePlugStatusPulse
import com.example.ui.components.RatingPill
import com.example.ui.components.SellerStoreProfileCard
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.launchPhoneDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.Ghost
import com.example.ui.theme.GhostWhite
import com.example.ui.theme.HypeOrange
import com.example.ui.theme.NeonLime
import com.example.ui.theme.Persian
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.StarGold
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextLightGrey
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VerifiedBadgeColor
import com.example.ui.theme.WhatsAppColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    selectedSize: String,
    isWishlisted: Boolean,
    onBackClick: () -> Unit,
    onSizeSelected: (String) -> Unit,
    onSellerClick: (Seller) -> Unit,
    onDialPlugClick: () -> Unit,
    onMakeOfferClick: () -> Unit,
    onToggleWishlist: () -> Unit,
    onRatePlugClick: () -> Unit = {}
) {
    val context = LocalContext.current
    BackHandler { onBackClick() }

    val images = if (product.galleryImages.isNotEmpty()) product.galleryImages else listOf(product.primaryImageRes)
    val pagerState = rememberPagerState(pageCount = { images.size })

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            // Sticky "Dial the Plug" Bottom Action Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .border(
                        androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                    )
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("sticky_dial_bottom_bar")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick WhatsApp Button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(WhatsAppColor)
                            .clickable {
                                val msg = "Yo! Inquiring about '${product.title}' (Size $selectedSize, $${product.price}) on Dial the Plug. Is it available for same-day dispatch?"
                                launchWhatsApp(context, product.seller.whatsappNumber, msg)
                            }
                            .testTag("product_quick_whatsapp_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💬", fontSize = 22.sp)
                    }

                    // Quick Call Button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                launchPhoneDialer(context, product.seller.phoneNumber)
                            }
                            .testTag("product_quick_call_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Quick Call",
                            tint = GhostWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Master "Dial the Plug" Button in Persian Blue & Ghost White
                    Button(
                        onClick = onDialPlugClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("product_dial_plug_master_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Persian),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "⚡",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DIAL THE PLUG",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = GhostWhite
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top navigation row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .testTag("product_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }

                    Text(
                        text = product.brand.uppercase(),
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = NeonLime
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onToggleWishlist,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .testTag("product_toggle_wishlist")
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) HypeOrange else TextWhite
                            )
                        }
                    }
                }
            }

            // High-Res Image Carousel
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.15f)
                        .background(Color(0xFF0F1116))
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = painterResource(id = images[page]),
                            contentDescription = "${product.title} angle $page",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Authenticity verification badge overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xCC00281F))
                                .border(1.dp, NeonLime, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Authenticity check",
                                tint = NeonLime,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "100% VERIFIED AUTHENTIC",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = NeonLime
                            )
                        }
                    }

                    // Carousel dots indicator
                    if (images.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(images.size) { index ->
                                val isCurrent = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .size(if (isCurrent) 20.dp else 6.dp, 6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (isCurrent) NeonLime else Color(0x66FFFFFF))
                                )
                            }
                        }
                    }
                }
            }

            // Main Product Info: Title & Bold Pricing
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ConditionChip(condition = product.condition)
                        LivePlugStatusPulse(label = "IN HAND • READY TO DISPATCH")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = product.title,
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextWhite,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bold Pricing Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${product.price}",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                color = NeonLime
                            )
                            if (product.originalPrice != null && product.originalPrice > product.price) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$${product.originalPrice}",
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SAVE $${product.originalPrice - product.price}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HypeOrange
                                )
                            }
                        }

                        // Make Offer Chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
                                .clickable { onMakeOfferClick() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("product_make_offer_chip")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = "Make offer",
                                    tint = NeonLime,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "MAKE OFFER",
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = TextWhite
                                )
                            }
                        }
                    }
                }
            }

            // Size Selection Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SELECT SIZE",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp,
                            color = TextWhite
                        )
                        Text(
                            text = "Selected: $selectedSize",
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NeonLime
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Size Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(product.sizeOptions) { sizeOption ->
                            val isSelected = sizeOption.label == selectedSize
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isSelected -> NeonLime
                                            sizeOption.isAvailable -> DarkSurfaceVariant
                                            else -> Color(0xFF15171D)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isSelected -> NeonLime
                                            sizeOption.isAvailable -> DarkCardBorder
                                            else -> Color(0xFF22252E)
                                        }
                                    )
                                    .clickable(enabled = sizeOption.isAvailable) {
                                        onSizeSelected(sizeOption.label)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("size_chip_${sizeOption.label}")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = sizeOption.label,
                                        fontFamily = SpaceGroteskFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = when {
                                            isSelected -> Color.Black
                                            sizeOption.isAvailable -> TextWhite
                                            else -> TextMuted
                                        }
                                    )
                                    Text(
                                        text = if (sizeOption.isAvailable) "${sizeOption.stockCount} in hand" else "Sold",
                                        fontSize = 9.sp,
                                        color = when {
                                            isSelected -> Color(0xFF2A3A00)
                                            sizeOption.isAvailable -> TextLightGrey
                                            else -> Color(0xFF6B7280)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Store Profile Card with Interactive Star Rating & Rate Plug Action
            item {
                SellerStoreProfileCard(
                    seller = product.seller,
                    onSellerClick = onSellerClick,
                    onRatePlugClick = onRatePlugClick,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Authenticity & Specification Checklist
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "AUTHENTICATION GUARANTEE",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            product.authenticationChecklist.forEach { checkItem ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF112918)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Passed",
                                            tint = NeonLime,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = checkItem,
                                        fontFamily = SpaceGroteskFontFamily,
                                        fontSize = 12.sp,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Item Description & Specifications
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "ITEM SPECIFICATIONS",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            SpecRow("Colorway", product.colorway)
                            Divider(color = DarkCardBorder, modifier = Modifier.padding(vertical = 8.dp))
                            SpecRow("Release Year", product.releaseYear)
                            Divider(color = DarkCardBorder, modifier = Modifier.padding(vertical = 8.dp))
                            SpecRow("Category", product.category.displayName)
                            Divider(color = DarkCardBorder, modifier = Modifier.padding(vertical = 8.dp))
                            SpecRow("Provenance", "Direct from ${product.seller.name}")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "PLUG NOTES",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.description,
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = TextLightGrey
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
        Text(
            text = value,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextWhite
        )
    }
}
