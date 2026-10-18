package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Seller
import com.example.model.SizeOption
import com.example.ui.theme.DarkBackground
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
import com.example.ui.theme.SyneFontFamily
import com.example.ui.theme.TextLightGrey
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun SellerUploadScreen(
    onBackClick: () -> Unit,
    onProductPublished: (Product) -> Unit
) {
    BackHandler { onBackClick() }

    var title by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ProductCategory.KICKS) }
    var priceText by remember { mutableStateOf("") }
    var originalPriceText by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Deadstock / Brand New") }
    var colorway by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("2024") }
    var description by remember { mutableStateOf("") }

    // Seller Info
    var storeName by remember { mutableStateOf("My Vault NYC") }
    var phoneNumber by remember { mutableStateOf("+1 (212) 555-0188") }
    var whatsappNumber by remember { mutableStateOf("12125550188") }
    var location by remember { mutableStateOf("SoHo, New York") }

    // Size Selection
    var selectedSizes by remember {
        mutableStateOf(listOf("US 9.0", "US 9.5", "US 10.0", "US 10.5"))
    }
    var newSizeInput by remember { mutableStateOf("") }

    // Image preset selection
    val imagePresets = listOf(
        R.drawable.img_kicks_travis to "Kicks Studio",
        R.drawable.img_drip_jacket to "Techwear Drip",
        R.drawable.img_hero_banner to "Match Kit",
        R.drawable.img_store_banner to "Vault Item"
    )
    var selectedImageRes by remember { mutableIntStateOf(R.drawable.img_kicks_travis) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("upload_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GhostWhite
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "DROP A NEW GRAIL",
                        fontFamily = SyneFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = GhostWhite
                    )
                    Text(
                        text = "SELLER UPLOAD SUITE",
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = PersianBlueGlow
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner if any
            if (errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF5A1A1A))
                            .border(1.dp, Color(0xFFFF5252), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GhostWhite
                        )
                    }
                }
            }

            // Image Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "PRODUCT PHOTO",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GhostWhite,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Preview of selected photo
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F0C24))
                                .border(1.5.dp, PersianBlueLight, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = painterResource(id = selectedImageRes),
                                contentDescription = "Selected Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Persian.copy(alpha = 0.85f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "VERIFIED SHOT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GhostWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Select Photo Asset Preset:",
                            fontSize = 11.sp,
                            color = GhostSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(imagePresets) { (resId, label) ->
                                val isSelected = selectedImageRes == resId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Persian else DarkSurfaceVariant)
                                        .border(1.dp, if (isSelected) GhostWhite else DarkCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { selectedImageRes = resId }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GhostWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Item Details Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "ITEM INFORMATION",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GhostWhite,
                            letterSpacing = 1.sp
                        )

                        // Title Field
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Item Title (e.g. Travis Scott AJ1 Low)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("upload_title_field"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GhostWhite,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            )
                        )

                        // Brand Field
                        OutlinedTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Brand / Designer (e.g. Jordan x Cactus Jack)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("upload_brand_field"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GhostWhite,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            )
                        )

                        // Category Selector
                        Column {
                            Text(
                                text = "Category:",
                                fontSize = 11.sp,
                                color = GhostSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(listOf(ProductCategory.KICKS, ProductCategory.KITS, ProductCategory.DRIP, ProductCategory.ACCESSORIES)) { cat ->
                                    val isSelected = category == cat
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Persian else DarkSurfaceVariant)
                                            .border(1.dp, if (isSelected) GhostWhite else DarkCardBorder, RoundedCornerShape(8.dp))
                                            .clickable { category = cat }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = cat.displayName.uppercase(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GhostWhite
                                        )
                                    }
                                }
                            }
                        }

                        // Price and Original Price Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Price ($)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("upload_price_field"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )

                            OutlinedTextField(
                                value = originalPriceText,
                                onValueChange = { originalPriceText = it },
                                label = { Text("Est. Value / Retail ($)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )
                        }

                        // Condition
                        Column {
                            Text(text = "Condition:", fontSize = 11.sp, color = GhostSecondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            val conditions = listOf("Deadstock / Brand New", "VNDS 9.8/10", "Pristine With Tags", "Vault Kept")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(conditions) { cond ->
                                    val isSelected = condition == cond
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Persian else DarkSurfaceVariant)
                                            .border(1.dp, if (isSelected) GhostWhite else DarkCardBorder, RoundedCornerShape(8.dp))
                                            .clickable { condition = cond }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = cond,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GhostWhite
                                        )
                                    }
                                }
                            }
                        }

                        // Colorway & Year
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = colorway,
                                onValueChange = { colorway = it },
                                label = { Text("Colorway") },
                                singleLine = true,
                                modifier = Modifier.weight(1.3f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )

                            OutlinedTextField(
                                value = releaseYear,
                                onValueChange = { releaseYear = it },
                                label = { Text("Year") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(0.7f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )
                        }

                        // Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Plug Notes & Description") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GhostWhite,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            )
                        )
                    }
                }
            }

            // Size Options Builder
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "AVAILABLE SIZES IN HAND",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GhostWhite,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Selected sizes chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedSizes.forEach { sz ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Persian)
                                        .border(1.dp, GhostWhite.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = sz, fontSize = 11.sp, color = GhostWhite, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove size",
                                            tint = GhostSecondary,
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clickable {
                                                    selectedSizes = selectedSizes.filter { it != sz }
                                                }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick size presets
                        val presetSizes = if (category == ProductCategory.KICKS) {
                            listOf("US 8.0", "US 8.5", "US 9.0", "US 9.5", "US 10.0", "US 10.5", "US 11.0", "US 12.0")
                        } else {
                            listOf("Small", "Medium", "Large", "X-Large", "One Size")
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(presetSizes) { sz ->
                                val isAdded = selectedSizes.contains(sz)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isAdded) DarkCardBorder else DarkSurfaceVariant)
                                        .clickable {
                                            if (!isAdded) selectedSizes = selectedSizes + sz
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "+ $sz",
                                        fontSize = 10.sp,
                                        color = if (isAdded) TextMuted else GhostSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Plug / Seller Details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "PLUG CONTACT & DISPATCH",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = GhostWhite,
                            letterSpacing = 1.sp
                        )

                        OutlinedTextField(
                            value = storeName,
                            onValueChange = { storeName = it },
                            label = { Text("Store / Plug Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GhostWhite,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone Number") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )

                            OutlinedTextField(
                                value = whatsappNumber,
                                onValueChange = { whatsappNumber = it },
                                label = { Text("WhatsApp (no +)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GhostWhite,
                                    unfocusedBorderColor = DarkCardBorder,
                                    focusedTextColor = GhostWhite,
                                    unfocusedTextColor = GhostWhite
                                )
                            )
                        }

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Dispatch City / Region (e.g. SoHo, NY)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GhostWhite,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            )
                        )
                    }
                }
            }

            // Publish Button
            item {
                Button(
                    onClick = {
                        val parsedPrice = priceText.toIntOrNull()
                        if (title.isBlank()) {
                            errorMessage = "Please enter an item title."
                            return@Button
                        }
                        if (parsedPrice == null || parsedPrice <= 0) {
                            errorMessage = "Please enter a valid price."
                            return@Button
                        }
                        if (selectedSizes.isEmpty()) {
                            errorMessage = "Please add at least one available size."
                            return@Button
                        }

                        val parsedOrigPrice = originalPriceText.toIntOrNull()
                        val sellerObj = Seller(
                            id = "plug_${System.currentTimeMillis()}",
                            name = if (storeName.isNotBlank()) storeName else "Verified Plug",
                            handle = "@${(storeName.lowercase().replace(" ", ""))}",
                            bio = "Direct authenticated vault listings. In-hand dispatch.",
                            location = if (location.isNotBlank()) location else "NYC",
                            rating = 5.0f,
                            totalDeals = 1,
                            authScore = 100.0f,
                            responseTime = "< 3 mins",
                            isVerified = true,
                            phoneNumber = if (phoneNumber.isNotBlank()) phoneNumber else "+1 (212) 555-0199",
                            whatsappNumber = if (whatsappNumber.isNotBlank()) whatsappNumber else "12125550199",
                            avatarEmoji = "⚡",
                            coverBannerRes = R.drawable.img_store_banner
                        )

                        val newProduct = Product(
                            id = "prod_${System.currentTimeMillis()}",
                            title = title.trim(),
                            brand = if (brand.isNotBlank()) brand.trim() else "Archive Plug",
                            category = category,
                            price = parsedPrice,
                            originalPrice = parsedOrigPrice,
                            condition = condition,
                            sizeOptions = selectedSizes.map { SizeOption(it, true, 1) },
                            selectedSize = selectedSizes.first(),
                            colorway = if (colorway.isNotBlank()) colorway.trim() else "Signature",
                            releaseYear = if (releaseYear.isNotBlank()) releaseYear.trim() else "2024",
                            description = if (description.isNotBlank()) description.trim() else "100% verified authentic deadstock. In hand and ready for same-day dispatch.",
                            seller = sellerObj,
                            isSold = false,
                            isFeatured = true,
                            primaryImageRes = selectedImageRes,
                            galleryImages = listOf(selectedImageRes)
                        )

                        onProductPublished(newProduct)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("publish_product_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Persian),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GhostWhite)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = GhostWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PUBLISH DROP TO MARKETPLACE",
                            fontFamily = SyneFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GhostWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
