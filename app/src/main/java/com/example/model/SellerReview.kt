package com.example.model

data class SellerReview(
    val id: String,
    val sellerId: String,
    val buyerName: String,
    val rating: Int, // 1 to 5
    val comment: String,
    val praiseTags: List<String> = emptyList(),
    val timestamp: String = "Just now",
    val isVerifiedPurchase: Boolean = true
)
