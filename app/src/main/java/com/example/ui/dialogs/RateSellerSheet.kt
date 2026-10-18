package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Seller
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.Ghost
import com.example.ui.theme.GhostSecondary
import com.example.ui.theme.GhostWhite
import com.example.ui.theme.Persian
import com.example.ui.theme.PersianBlueGlow
import com.example.ui.theme.PersianBlueLight
import com.example.ui.theme.PersianContainer
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.StarGold
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RateSellerSheet(
    seller: Seller,
    onDismiss: () -> Unit,
    onSubmitReview: (rating: Int, comment: String, praiseTags: List<String>) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var reviewText by remember { mutableStateOf("") }
    val availablePraiseTags = listOf(
        "100% Authentic",
        "Fast Same-Day Dispatch",
        "Pristine Deadstock",
        "Responsive & Friendly",
        "Fair Negotiation",
        "Secure Packaging"
    )
    var selectedTags by remember {
        mutableStateOf(setOf("100% Authentic", "Fast Same-Day Dispatch"))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkSurface,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
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
                                text = "RATE THIS PLUG",
                                fontFamily = SyneFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = GhostWhite
                            )
                        }
                        Text(
                            text = seller.name,
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 12.sp,
                            color = PersianBlueGlow
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_rate_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = GhostSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive 5-Star Rating Stepper
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TAP TO RATE",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GhostSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5 Stars Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { starIndex ->
                            val isFilled = starIndex <= selectedStars
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "$starIndex Stars",
                                tint = if (isFilled) StarGold else DarkCardBorder,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { selectedStars = starIndex }
                                    .testTag("star_rate_button_$starIndex")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val ratingLabel = when (selectedStars) {
                        5 -> "5.0 ★ Certified Grail Plug 🔥"
                        4 -> "4.0 ★ Great Deal & Dispatch 👍"
                        3 -> "3.0 ★ Average Experience"
                        2 -> "2.0 ★ Room for Improvement"
                        else -> "1.0 ★ Disappointing Deal"
                    }

                    Text(
                        text = ratingLabel,
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GhostWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Praise Tag Chips
            Text(
                text = "WHAT WENT WELL?",
                fontFamily = SyneFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp,
                color = GhostWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                availablePraiseTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Persian else DarkSurfaceVariant)
                            .border(1.dp, if (isSelected) GhostWhite else DarkCardBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                selectedTags = if (isSelected) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("praise_tag_$tag")
                    ) {
                        Text(
                            text = tag,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = GhostWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Optional Review Comment Input
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Buyer Experience Note (Optional)", fontSize = 12.sp, color = TextMuted) },
                placeholder = { Text("e.g. Fast response on WhatsApp, kicks came factory laced with authenticity card.", fontSize = 12.sp, color = TextMuted) },
                modifier = Modifier.fillMaxWidth().testTag("rate_comment_field"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GhostWhite,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = GhostWhite,
                    unfocusedTextColor = GhostWhite
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Submit Button
            Button(
                onClick = {
                    val finalComment = if (reviewText.isNotBlank()) reviewText.trim() else "Verified deal with ${seller.name}."
                    onSubmitReview(selectedStars, finalComment, selectedTags.toList())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_rate_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Persian),
                border = androidx.compose.foundation.BorderStroke(1.dp, GhostWhite)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SUBMIT VERIFIED REVIEW",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GhostWhite
                    )
                }
            }
        }
    }
}
