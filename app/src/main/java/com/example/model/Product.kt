package com.example.model

import androidx.annotation.DrawableRes

enum class ProductCategory(val displayName: String) {
    ALL("All"),
    KICKS("Kicks"),
    KITS("Kits"),
    DRIP("Drip"),
    ACCESSORIES("Accessories")
}

data class Seller(
    val id: String,
    val name: String,
    val handle: String,
    val bio: String,
    val location: String,
    val rating: Float,
    val totalDeals: Int,
    val authScore: Float,
    val responseTime: String,
    val isVerified: Boolean,
    val phoneNumber: String,
    val whatsappNumber: String,
    val avatarEmoji: String = "👟",
    @DrawableRes val coverBannerRes: Int? = null
)

data class SizeOption(
    val label: String,
    val isAvailable: Boolean = true,
    val stockCount: Int = 1
)

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val category: ProductCategory,
    val price: Int,
    val originalPrice: Int? = null,
    val condition: String, // e.g. "Deadstock / Brand New", "VNDS 9.8/10"
    val sizeOptions: List<SizeOption>,
    val selectedSize: String,
    val colorway: String,
    val releaseYear: String,
    val description: String,
    val seller: Seller,
    val isSold: Boolean = false,
    val isFeatured: Boolean = false,
    val authenticationChecklist: List<String> = listOf(
        "UV Light & Stitch Accuracy Checked",
        "RFID & Holographic Security Tag Intact",
        "Original Packaging, Box & Accessories",
        "Direct Plug Sourcing Verified"
    ),
    @DrawableRes val primaryImageRes: Int,
    val galleryImages: List<Int> = emptyList()
)
