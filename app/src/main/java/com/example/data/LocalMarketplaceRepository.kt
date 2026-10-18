package com.example.data

import android.content.Context
import com.example.R
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Seller
import com.example.model.SizeOption
import com.example.model.SellerReview
import org.json.JSONArray
import org.json.JSONObject

data class AuthUser(
    val email: String,
    val username: String,
    val isPlugSeller: Boolean = false,
    val isGuest: Boolean = false,
    val authProvider: String = "EMAIL" // GOOGLE, FACEBOOK, EMAIL, GUEST
)

class LocalMarketplaceRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("dial_the_plug_storage", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_RECENT_SEARCHES = "key_recent_searches"
        private const val KEY_PRODUCTS = "key_products"
        private const val KEY_USER_EMAIL = "key_user_email"
        private const val KEY_USER_NAME = "key_user_name"
        private const val KEY_IS_GUEST = "key_is_guest"
        private const val KEY_AUTH_PROVIDER = "key_auth_provider"
        private const val KEY_SELLER_REVIEWS = "key_seller_reviews"

        val POPULAR_TAGS = listOf(
            "Travis Scott",
            "Off-White",
            "Techwear",
            "Deadstock",
            "Y-3 Kit",
            "Chrome Hearts",
            "SB Dunk",
            "Supreme",
            "Jordan 1"
        )
    }

    // --- Search History Persistence ---
    fun getRecentSearches(): List<String> {
        val raw = prefs.getString(KEY_RECENT_SEARCHES, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                val item = array.optString(i)
                if (item.isNotBlank()) list.add(item)
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        val current = getRecentSearches().toMutableList()
        current.remove(trimmed)
        current.add(0, trimmed)
        val trimmedList = current.take(10)
        val array = JSONArray()
        trimmedList.forEach { array.put(it) }
        prefs.edit().putString(KEY_RECENT_SEARCHES, array.toString()).apply()
    }

    fun removeRecentSearch(query: String) {
        val current = getRecentSearches().toMutableList()
        current.remove(query)
        val array = JSONArray()
        current.forEach { array.put(it) }
        prefs.edit().putString(KEY_RECENT_SEARCHES, array.toString()).apply()
    }

    fun clearRecentSearches() {
        prefs.edit().remove(KEY_RECENT_SEARCHES).apply()
    }

    // --- Products Persistence (Starts at Zero Items!) ---
    fun getProducts(): List<Product> {
        val raw = prefs.getString(KEY_PRODUCTS, null)
        // If not initialized yet, start strictly at ZERO items as requested!
        if (raw == null) {
            return emptyList()
        }
        return try {
            val array = JSONArray(raw)
            val list = mutableListOf<Product>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(productFromJson(obj))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addProduct(product: Product) {
        val current = getProducts().toMutableList()
        current.add(0, product)
        saveProducts(current)
    }

    fun saveProducts(products: List<Product>) {
        val array = JSONArray()
        products.forEach { array.put(productToJson(it)) }
        prefs.edit().putString(KEY_PRODUCTS, array.toString()).apply()
    }

    fun clearAllProducts() {
        prefs.edit().putString(KEY_PRODUCTS, "[]").apply()
    }

    fun loadSampleProducts(): List<Product> {
        val samples = MockDataProvider.sampleProducts
        saveProducts(samples)
        return samples
    }

    // --- Auth Persistence ---
    fun getAuthUser(): AuthUser? {
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return null
        val name = prefs.getString(KEY_USER_NAME, "User") ?: "User"
        val isGuest = prefs.getBoolean(KEY_IS_GUEST, false)
        val provider = prefs.getString(KEY_AUTH_PROVIDER, "EMAIL") ?: "EMAIL"
        return AuthUser(email = email, username = name, isGuest = isGuest, authProvider = provider)
    }

    fun saveAuthUser(user: AuthUser?) {
        if (user == null) {
            prefs.edit()
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_NAME)
                .remove(KEY_IS_GUEST)
                .remove(KEY_AUTH_PROVIDER)
                .apply()
        } else {
            prefs.edit()
                .putString(KEY_USER_EMAIL, user.email)
                .putString(KEY_USER_NAME, user.username)
                .putBoolean(KEY_IS_GUEST, user.isGuest)
                .putString(KEY_AUTH_PROVIDER, user.authProvider)
                .apply()
        }
    }

    // --- Seller Reviews & Star Rating Persistence ---
    fun getSellerReviews(sellerId: String): List<SellerReview> {
        val allReviews = getAllReviews()
        val sellerSpecific = allReviews.filter { it.sellerId == sellerId }
        if (sellerSpecific.isNotEmpty()) {
            return sellerSpecific
        }
        return getDefaultReviewsForSeller(sellerId)
    }

    fun getAllReviews(): List<SellerReview> {
        val raw = prefs.getString(KEY_SELLER_REVIEWS, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            val list = mutableListOf<SellerReview>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val tagsArray = obj.optJSONArray("praiseTags")
                val tags = mutableListOf<String>()
                if (tagsArray != null) {
                    for (j in 0 until tagsArray.length()) {
                        tags.add(tagsArray.getString(j))
                    }
                }
                list.add(
                    SellerReview(
                        id = obj.optString("id", "rev_${System.currentTimeMillis()}"),
                        sellerId = obj.optString("sellerId", ""),
                        buyerName = obj.optString("buyerName", "Verified Buyer"),
                        rating = obj.optInt("rating", 5),
                        comment = obj.optString("comment", ""),
                        praiseTags = tags,
                        timestamp = obj.optString("timestamp", "Recently"),
                        isVerifiedPurchase = obj.optBoolean("isVerifiedPurchase", true)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveSellerReview(review: SellerReview) {
        val current = getAllReviews().toMutableList()
        current.add(0, review)
        val array = JSONArray()
        current.forEach { r ->
            val obj = JSONObject()
            obj.put("id", r.id)
            obj.put("sellerId", r.sellerId)
            obj.put("buyerName", r.buyerName)
            obj.put("rating", r.rating)
            obj.put("comment", r.comment)
            val tagsArr = JSONArray()
            r.praiseTags.forEach { tagsArr.put(it) }
            obj.put("praiseTags", tagsArr)
            obj.put("timestamp", r.timestamp)
            obj.put("isVerifiedPurchase", r.isVerifiedPurchase)
            array.put(obj)
        }
        prefs.edit().putString(KEY_SELLER_REVIEWS, array.toString()).apply()

        updateSellerCompositeRating(review.sellerId)
    }

    private fun updateSellerCompositeRating(sellerId: String) {
        val reviews = getSellerReviews(sellerId)
        if (reviews.isEmpty()) return
        val avgRating = reviews.map { it.rating }.average().toFloat()

        val products = getProducts().toMutableList()
        var updated = false
        for (i in products.indices) {
            val p = products[i]
            if (p.seller.id == sellerId) {
                val updatedSeller = p.seller.copy(
                    rating = avgRating,
                    totalDeals = p.seller.totalDeals + 1
                )
                products[i] = p.copy(seller = updatedSeller)
                updated = true
            }
        }
        if (updated) {
            saveProducts(products)
        }
    }

    private fun getDefaultReviewsForSeller(sellerId: String): List<SellerReview> {
        return listOf(
            SellerReview(
                id = "rev_init_1_$sellerId",
                sellerId = sellerId,
                buyerName = "@kicks_fanatic",
                rating = 5,
                comment = "Deadstock kicks arrived pristine and double-boxed. NFC check passed instantly!",
                praiseTags = listOf("100% Authentic", "Pristine Deadstock", "Fast Same-Day Dispatch"),
                timestamp = "2 hours ago",
                isVerifiedPurchase = true
            ),
            SellerReview(
                id = "rev_init_2_$sellerId",
                sellerId = sellerId,
                buyerName = "@hype_hunter_nyc",
                rating = 5,
                comment = "Fast response on WhatsApp, super smooth deal negotiation. Will dial this plug again.",
                praiseTags = listOf("Responsive & Friendly", "Fair Negotiation"),
                timestamp = "1 day ago",
                isVerifiedPurchase = true
            ),
            SellerReview(
                id = "rev_init_3_$sellerId",
                sellerId = sellerId,
                buyerName = "@tokyo_archivist",
                rating = 5,
                comment = "Grail piece authentic as promised. 10/10 experience.",
                praiseTags = listOf("100% Authentic", "Secure Packaging"),
                timestamp = "3 days ago",
                isVerifiedPurchase = true
            )
        )
    }

    // JSON serialization helpers
    private fun productToJson(p: Product): JSONObject {
        val obj = JSONObject()
        obj.put("id", p.id)
        obj.put("title", p.title)
        obj.put("brand", p.brand)
        obj.put("category", p.category.name)
        obj.put("price", p.price)
        obj.put("originalPrice", p.originalPrice ?: -1)
        obj.put("condition", p.condition)
        obj.put("selectedSize", p.selectedSize)
        obj.put("colorway", p.colorway)
        obj.put("releaseYear", p.releaseYear)
        obj.put("description", p.description)
        obj.put("primaryImageRes", p.primaryImageRes)

        // Sizes array
        val sizesArray = JSONArray()
        p.sizeOptions.forEach { size ->
            val sObj = JSONObject()
            sObj.put("label", size.label)
            sObj.put("isAvailable", size.isAvailable)
            sObj.put("stockCount", size.stockCount)
            sizesArray.put(sObj)
        }
        obj.put("sizes", sizesArray)

        // Seller
        val s = p.seller
        val sellerObj = JSONObject()
        sellerObj.put("id", s.id)
        sellerObj.put("name", s.name)
        sellerObj.put("handle", s.handle)
        sellerObj.put("bio", s.bio)
        sellerObj.put("location", s.location)
        sellerObj.put("rating", s.rating.toDouble())
        sellerObj.put("totalDeals", s.totalDeals)
        sellerObj.put("authScore", s.authScore.toDouble())
        sellerObj.put("responseTime", s.responseTime)
        sellerObj.put("isVerified", s.isVerified)
        sellerObj.put("phoneNumber", s.phoneNumber)
        sellerObj.put("whatsappNumber", s.whatsappNumber)
        sellerObj.put("avatarEmoji", s.avatarEmoji)
        obj.put("seller", sellerObj)

        return obj
    }

    private fun productFromJson(obj: JSONObject): Product {
        val sizesList = mutableListOf<SizeOption>()
        val sizesArray = obj.optJSONArray("sizes")
        if (sizesArray != null) {
            for (i in 0 until sizesArray.length()) {
                val sObj = sizesArray.getJSONObject(i)
                sizesList.add(
                    SizeOption(
                        label = sObj.optString("label", "Standard"),
                        isAvailable = sObj.optBoolean("isAvailable", true),
                        stockCount = sObj.optInt("stockCount", 1)
                    )
                )
            }
        }
        if (sizesList.isEmpty()) {
            sizesList.add(SizeOption("Standard", true, 1))
        }

        val sellerObj = obj.optJSONObject("seller")
        val seller = if (sellerObj != null) {
            Seller(
                id = sellerObj.optString("id", "plug_custom"),
                name = sellerObj.optString("name", "Verified Plug"),
                handle = sellerObj.optString("handle", "@plug"),
                bio = sellerObj.optString("bio", "Verified Plug in hand."),
                location = sellerObj.optString("location", "NYC"),
                rating = sellerObj.optDouble("rating", 4.95).toFloat(),
                totalDeals = sellerObj.optInt("totalDeals", 12),
                authScore = sellerObj.optDouble("authScore", 99.5).toFloat(),
                responseTime = sellerObj.optString("responseTime", "< 5m"),
                isVerified = sellerObj.optBoolean("isVerified", true),
                phoneNumber = sellerObj.optString("phoneNumber", "+1 (212) 555-0199"),
                whatsappNumber = sellerObj.optString("whatsappNumber", "12125550199"),
                avatarEmoji = sellerObj.optString("avatarEmoji", "⚡")
            )
        } else {
            MockDataProvider.sellerNocturnal
        }

        val origPriceRaw = obj.optInt("originalPrice", -1)
        val origPrice = if (origPriceRaw > 0) origPriceRaw else null

        val catString = obj.optString("category", "KICKS")
        val category = try {
            ProductCategory.valueOf(catString)
        } catch (e: Exception) {
            ProductCategory.KICKS
        }

        val imgRes = obj.optInt("primaryImageRes", R.drawable.img_kicks_travis)

        return Product(
            id = obj.optString("id", "prod_${System.currentTimeMillis()}"),
            title = obj.optString("title", "Streetwear Item"),
            brand = obj.optString("brand", "Archive Plug"),
            category = category,
            price = obj.optInt("price", 100),
            originalPrice = origPrice,
            condition = obj.optString("condition", "Deadstock / Brand New"),
            sizeOptions = sizesList,
            selectedSize = obj.optString("selectedSize", sizesList.first().label),
            colorway = obj.optString("colorway", "Classic"),
            releaseYear = obj.optString("releaseYear", "2024"),
            description = obj.optString("description", "Direct from verified plug."),
            seller = seller,
            isSold = false,
            primaryImageRes = if (imgRes != 0) imgRes else R.drawable.img_kicks_travis,
            galleryImages = listOf(if (imgRes != 0) imgRes else R.drawable.img_kicks_travis)
        )
    }
}
