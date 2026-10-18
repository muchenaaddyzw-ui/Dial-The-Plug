package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.model.Seller
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.SpaceGroteskFontFamily
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
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
import com.example.ui.theme.StarGold
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextLightGrey
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VerifiedBadgeColor

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier,
    text: String = "VERIFIED PLUG",
    isCompact: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PersianContainer)
            .border(1.dp, PersianBlueLight.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
            .padding(horizontal = if (isCompact) 5.dp else 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Verified badge",
            tint = GhostWhite,
            modifier = Modifier.size(if (isCompact) 10.dp else 12.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = text,
            fontFamily = SyneFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = if (isCompact) 9.sp else 10.sp,
            letterSpacing = 0.5.sp,
            color = GhostWhite
        )
    }
}

@Composable
fun ConditionChip(
    condition: String,
    modifier: Modifier = Modifier
) {
    val isDeadstock = condition.contains("Deadstock", ignoreCase = true)
    val accentColor = if (isDeadstock) GhostWhite else PersianBlueGlow

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Persian.copy(alpha = 0.85f))
            .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = condition.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = accentColor
        )
    }
}

@Composable
fun RatingPill(
    rating: Float,
    dealsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating star",
            tint = StarGold,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "%.2f".format(rating),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GhostWhite
        )
        Text(
            text = " ($dealsCount deals)",
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}

@Composable
fun StarRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: androidx.compose.ui.unit.Dp = 14.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isFilled = rating >= i - 0.25f
            Icon(
                imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (isFilled) StarGold else DarkCardBorder,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
fun SellerStoreProfileCard(
    seller: Seller,
    onSellerClick: (Seller) -> Unit,
    onRatePlugClick: () -> Unit,
    modifier: Modifier = Modifier,
    recentPraiseTags: List<String> = listOf("100% Authentic", "Fast Same-Day Dispatch")
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSellerClick(seller) }
            .testTag("product_seller_profile_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Avatar, Name, Verified Badge, Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, PersianBlueGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = seller.avatarEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = seller.name,
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = GhostWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            VerifiedBadge(isCompact = true)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${seller.handle} • ${seller.location}",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 11.sp,
                            color = GhostSecondary
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "View Storefront",
                    tint = PersianBlueGlow,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Star Rating & Trust Signals Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StarRatingBar(rating = seller.rating, starSize = 14.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "%.2f".format(seller.rating),
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = StarGold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${seller.totalDeals} Verified Deals • 99.8% Auth",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 10.sp,
                            color = GhostSecondary
                        )
                    }

                    // Rate This Plug Action Button
                    Button(
                        onClick = onRatePlugClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Persian),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("rate_plug_card_button")
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Trust praise feedback chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recentPraiseTags.take(2).forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PersianContainer.copy(alpha = 0.7f))
                            .border(1.dp, PersianBlueLight.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "✓ $tag",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = GhostWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LivePlugStatusPulse(
    modifier: Modifier = Modifier,
    label: String = "PLUG IN HAND"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(PersianContainer)
            .border(1.dp, PersianBlueLight.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .scale(pulseScale)
                .size(7.dp)
                .clip(CircleShape)
                .background(GhostWhite)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
            color = GhostWhite
        )
    }
}

/**
 * High-energy Streetwear Pull-to-Refresh Banner & Indicator
 * Features rotating lightning bolt icon, pulsating Persian Blue & Ghost White waves,
 * and real-time plug vault synchronization status.
 */
@Composable
fun StreetwearPullToRefreshIndicator(
    isRefreshing: Boolean,
    onManualRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ptr_anim")
    
    // Continuous rotation for the refresh icon
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulsing scale for the glowing ring
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    // Gradient shift
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientShift"
    )

    AnimatedVisibility(
        visible = isRefreshing,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Persian,
                            PersianBlueVibrant,
                            PersianContainer,
                            Persian
                        )
                    )
                )
                .border(1.5.dp, GhostWhite.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .testTag("pull_to_refresh_banner"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Rotating & Pulsing Plug emblem
                    Box(
                        modifier = Modifier
                            .scale(glowScale)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GhostWhite)
                            .border(1.5.dp, Persian, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Syncing",
                            tint = Persian,
                            modifier = Modifier
                                .size(22.dp)
                                .rotate(rotation)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⚡ SYNCING VAULT DROPS",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 0.8.sp,
                                color = GhostWhite
                            )
                        }
                        Text(
                            text = "Pulling live Tokyo & NYC verified deadstock...",
                            fontSize = 10.sp,
                            color = GhostSecondary
                        )
                    }
                }

                // Sleek Ghost White circular spinner
                CircularProgressIndicator(
                    color = GhostWhite,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Utility Intent Launchers
fun launchWhatsApp(context: Context, whatsappNumber: String, message: String) {
    try {
        val sanitizedPhone = whatsappNumber.replace("+", "").replace(" ", "").replace("-", "")
        val encodedMessage = Uri.encode(message)
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$sanitizedPhone&text=$encodedMessage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Opening WhatsApp for +$whatsappNumber...", Toast.LENGTH_SHORT).show()
    }
}

fun launchPhoneDialer(context: Context, phoneNumber: String) {
    try {
        val uri = Uri.parse("tel:${phoneNumber.trim()}")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Dialing $phoneNumber...", Toast.LENGTH_SHORT).show()
    }
}
