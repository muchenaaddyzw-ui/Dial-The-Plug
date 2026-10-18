# Star-Based Seller Rating System & Social Login (Google & Facebook) Plan

Integration plan to implement an interactive 1–5 star rating and review system on seller store profile cards with authentic buyer trust signals, alongside branded one-tap Google and Facebook login buttons on the welcome auth gate.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following confirmed decisions incorporate your answers from Phase 1:
> - **Interactive Star Rating & Review Sheet**: Tapping the rating or "Rate Plug" action opens an interactive bottom sheet with a 1–5 star selector, tag pills (e.g. *Fast Shipping*, *100% Authentic*, *Responsive Plug*, *Deadstock Quality*), and optional comment input.
> - **Trust Signals on Seller Profile Card**: Seller store cards will display an interactive 5-star visual breakdown, verified buyer reviews counter, authentication pass rate (`99.8% Auth Score`), and recent buyer feedback chips.
> - **Branded Social Sign-In**: Prominent Google and Facebook one-tap sign-in buttons with official brand colors and icons on `AuthGateScreen`, allowing instant authentication as a verified buyer or plug.

---

## 1. Overview & Core Concept

1. **Seller Trust & Verification Ecosystem**:
   - **Interactive 5-Star Rating Component**: Displays half and full stars with decimal precision (e.g., `★ 4.98`).
   - **"Rate This Plug" Action**: Buyers can rate their experience directly from the Product Detail store profile card or Seller Storefront, updating the seller's composite rating and deal counter in real-time.
   - **Recent Buyer Reviews & Trust Badges**: Verified buyer feedback chips (*"Legit plug, shipped same-day"*, *"NFC verified authentic"*).
2. **Branded Google & Facebook Login**:
   - High-contrast Google button (white card with official multi-colored G logo) and Facebook button (rich brand blue `#1877F2` with official white F logo).
   - Instant one-tap sign-in creating a verified buyer profile while preserving the **"Explore as Guest"** option.

---

## 2. User Experience & Visual Design

### Key User Flows

```
┌──────────────────────────────────────────────┐
│             Auth Gate Screen                 │
│ • "DIAL THE PLUG" High-Voltage Branding      │
│ • [ G  Continue with Google  ]               │
│ • [ f  Continue with Facebook ]              │
│ • Email / Password (Sign In / Register)      │
│ • [── EXPLORE AS GUEST ──]                   │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│          Seller Store Profile Card           │
│ • Plug Avatar & Verified Shield Badge        │
│ • ★★★★★ 4.98 Rating (412 Verified Deals)     │
│ • Trust Tags: [99.8% Auth] [Fast Same-Day]   │
│ • [★ Rate This Plug] Clickable Action Button │
└──────────────────────┬───────────────────────┘
                       │
                       ▼ (Tap "Rate This Plug")
┌──────────────────────────────────────────────┐
│         Rate & Review Plug Sheet             │
│ • Interactive 1–5 Star Rating Stepper        │
│ • Quick Praise Chips: [Authentic] [Fast]     │
│ • Optional Buyer Note / Experience           │
│ • [Submit Verified Review & Rating]          │
└──────────────────────────────────────────────┘
```

### Visual Identity & Theme
- **Star Gold (`#FFB800`)**: Radiant amber-gold for filled stars and rating indicators.
- **Persian Blue (`#27187E`) & Ghost White (`#F7F7FF`)**: Theme backdrop, borders, and typography.
- **Social Brand Tokens**: Official Google branding and Facebook Blue (`#1877F2`).

---

## 3. Key Product Decisions & Trade-Offs

- **Interactive Sheet vs. In-Card Taps**:
  - *Chosen Approach*: Interactive bottom sheet (`RateSellerSheet`) invoked by tapping the rating pill or "Rate Plug" button.
  - *Why*: Allows entering verified buyer notes and feedback tags without cluttering the compact store card layout.
- **Local Persistence of Reviews**:
  - *Chosen Approach*: Store reviews and updated ratings in `LocalMarketplaceRepository` so user ratings persist and reflect immediately on seller storefronts and product cards.
  - *Why*: Instant feedback loop that updates seller reputations across the app.

---

## 4. Technical Architecture & Data Strategy

```
┌─────────────────────────────────────────────────────────────┐
│                       DialViewModel                         │
│  • submitSellerRating(sellerId, stars, reviewText, tags)    │
│  • socialLogin(provider: GOOGLE | FACEBOOK)                 │
│  • showRateSellerSheet: Boolean                             │
└──────────────┬──────────────────────────────┬───────────────┘
               │                              │
               ▼                              ▼
┌─────────────────────────────┐ ┌─────────────────────────────┐
│  LocalMarketplaceRepository │ │      SellerReview Model     │
│ • saveSellerReview()        │ │ • id, sellerId, buyerName   │
│ • recalculateRating()       │ │ • rating (1-5), comment     │
│ • getSellerReviews()        │ │ • verifiedBuyer: Boolean    │
└─────────────────────────────┘ └─────────────────────────────┘
```

### Components to Update / Create:
1. **`RateSellerSheet.kt`**: Interactive bottom sheet featuring 5-star tap selector, quick praise chips, and submit button.
2. **`SellerReview.kt`**: Data model for buyer reviews and rating calculations.
3. **`CommonUi.kt` & `ProductDetailScreen.kt`**: Update store profile card with star rating breakdown, review counter, and "Rate Plug" button.
4. **`SellerStorefrontScreen.kt`**: Display recent buyer reviews tab and trust stats.
5. **`AuthGateScreen.kt`**: Add Google and Facebook branded login buttons.
