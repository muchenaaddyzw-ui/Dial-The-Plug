package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AuthUser
import com.example.data.LocalMarketplaceRepository
import com.example.data.MockDataProvider
import com.example.model.SellerReview
import com.example.ui.DialViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Dial the Plug", appName)
    }

    @Test
    fun `test seller review persistence and rating calculation`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = LocalMarketplaceRepository(context)
        val sellerId = "test_plug_1"

        val review = SellerReview(
            id = "test_rev_1",
            sellerId = sellerId,
            buyerName = "@grail_buyer",
            rating = 5,
            comment = "100% authentic, lightning fast delivery!",
            praiseTags = listOf("100% Authentic", "Fast Same-Day Dispatch"),
            timestamp = "Just now",
            isVerifiedPurchase = true
        )

        repository.saveSellerReview(review)
        val reviews = repository.getSellerReviews(sellerId)
        assertTrue(reviews.any { it.id == "test_rev_1" })
        assertEquals(5, reviews.first { it.id == "test_rev_1" }.rating)
    }

    @Test
    fun `test social login saves auth provider`() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = DialViewModel(application)

        viewModel.socialLogin("GOOGLE")
        val stateGoogle = viewModel.uiState.value
        assertNotNull(stateGoogle.currentUser)
        assertEquals("GOOGLE", stateGoogle.currentUser?.authProvider)
        assertEquals("AlexGrails", stateGoogle.currentUser?.username)

        viewModel.socialLogin("FACEBOOK")
        val stateFb = viewModel.uiState.value
        assertNotNull(stateFb.currentUser)
        assertEquals("FACEBOOK", stateFb.currentUser?.authProvider)
        assertEquals("AlexPlugFB", stateFb.currentUser?.username)
    }
}
