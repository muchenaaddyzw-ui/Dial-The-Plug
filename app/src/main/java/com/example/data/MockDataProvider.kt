package com.example.data

import com.example.R
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.Seller
import com.example.model.SizeOption

object MockDataProvider {

    val sellerNocturnal = Seller(
        id = "seller_nocturnal",
        name = "Nocturnal Kicks NYC",
        handle = "@nocturnalkicks",
        bio = "NYC's premier archive & deadstock plug. 100% verified authentic. In-hand items ship same-day or pickup in SoHo. Direct dial for fast VIP holds.",
        location = "SoHo, New York",
        rating = 4.98f,
        totalDeals = 412,
        authScore = 99.8f,
        responseTime = "< 3 mins",
        isVerified = true,
        phoneNumber = "+1 (212) 555-0199",
        whatsappNumber = "12125550199",
        avatarEmoji = "⚡",
        coverBannerRes = R.drawable.img_store_banner
    )

    val sellerArchiveTokyo = Seller(
        id = "seller_archive_tokyo",
        name = "Archive Plug Tokyo",
        handle = "@archiveplug.tyo",
        bio = "Rare Urahara, Undercover, Number (N)ine, and grail runners curated straight from Harajuku. Worldwide insured dispatch.",
        location = "Shibuya, Tokyo",
        rating = 4.95f,
        totalDeals = 289,
        authScore = 99.4f,
        responseTime = "< 8 mins",
        isVerified = true,
        phoneNumber = "+81 90 5555 0142",
        whatsappNumber = "819055550142",
        avatarEmoji = "🎌",
        coverBannerRes = R.drawable.img_store_banner
    )

    val sellerVaultLondon = Seller(
        id = "seller_vault_london",
        name = "Vault 88 London",
        handle = "@vault88.ldn",
        bio = "Mayfair exclusive consignment. Rare SBs, tier-zero runners, and runway samples. Instant WhatsApp authentication proofs available.",
        location = "Mayfair, London",
        rating = 4.99f,
        totalDeals = 540,
        authScore = 100.0f,
        responseTime = "< 2 mins",
        isVerified = true,
        phoneNumber = "+44 20 7946 0912",
        whatsappNumber = "442079460912",
        avatarEmoji = "👑",
        coverBannerRes = R.drawable.img_store_banner
    )

    val sellerApexLA = Seller(
        id = "seller_apex_la",
        name = "Apex Grails LA",
        handle = "@apexgrails",
        bio = "Fairfax Ave VIP locker. Sourcing verified heaters for celebrity stylists and private collectors since 2018. Dial plug to lock in.",
        location = "Fairfax, Los Angeles",
        rating = 4.92f,
        totalDeals = 360,
        authScore = 99.1f,
        responseTime = "< 5 mins",
        isVerified = true,
        phoneNumber = "+1 (323) 555-0184",
        whatsappNumber = "13235550184",
        avatarEmoji = "🔥",
        coverBannerRes = R.drawable.img_store_banner
    )

    val sampleProducts = listOf(
        Product(
            id = "prod_travis_canary",
            title = "Travis Scott x AJ1 Retro Low 'Canary'",
            brand = "Jordan x Cactus Jack",
            category = ProductCategory.KICKS,
            price = 680,
            originalPrice = 850,
            condition = "Deadstock / Brand New",
            sizeOptions = listOf(
                SizeOption("US 8.0", true, 1),
                SizeOption("US 8.5", true, 2),
                SizeOption("US 9.0", true, 1),
                SizeOption("US 9.5", true, 3),
                SizeOption("US 10.0", true, 2),
                SizeOption("US 10.5", true, 1),
                SizeOption("US 11.0", false, 0),
                SizeOption("US 12.0", true, 1)
            ),
            selectedSize = "US 10.0",
            colorway = "Canary / Racer Blue / Light Silver",
            releaseYear = "2024",
            description = "The hyped Travis Scott signature silhouette features premium tumbled canary yellow leather, backward oversized Swoosh in Racer Blue, and Cactus Jack heel embroidery. 100% deadstock with special edition box and three pairs of extra waxed laces.",
            seller = sellerNocturnal,
            isSold = false,
            isFeatured = true,
            primaryImageRes = R.drawable.img_kicks_travis,
            galleryImages = listOf(
                R.drawable.img_kicks_travis,
                R.drawable.img_hero_banner,
                R.drawable.img_store_banner
            )
        ),
        Product(
            id = "prod_tech_jacket",
            title = "Cybertech Tactical Reflective Shell Puffer",
            brand = "AcroStudio Techwear",
            category = ProductCategory.DRIP,
            price = 490,
            originalPrice = 620,
            condition = "Deadstock / With Tags",
            sizeOptions = listOf(
                SizeOption("Small", true, 2),
                SizeOption("Medium", true, 4),
                SizeOption("Large", true, 1),
                SizeOption("X-Large", false, 0)
            ),
            selectedSize = "Large",
            colorway = "Matte Carbon / 3M Reflective / Acid Lime",
            releaseYear = "2024",
            description = "Engineered with dual-layer weatherproof eVent membrane and modular tactical harness straps. Features Aquaguard YKK zippers in high-voltage lime, concealed RFID chest pouches, and magnetic Fidlock cuffs. Built for urban reconnaissance.",
            seller = sellerNocturnal,
            isSold = false,
            isFeatured = true,
            primaryImageRes = R.drawable.img_drip_jacket,
            galleryImages = listOf(
                R.drawable.img_drip_jacket,
                R.drawable.img_hero_banner,
                R.drawable.img_kicks_travis
            )
        ),
        Product(
            id = "prod_real_madrid_y3",
            title = "Real Madrid 2024/25 Y-3 Limited Match Kit",
            brand = "Adidas x Yohji Yamamoto",
            category = ProductCategory.KITS,
            price = 320,
            originalPrice = 400,
            condition = "Deadstock / Sealed Box",
            sizeOptions = listOf(
                SizeOption("M (Player Fit)", true, 2),
                SizeOption("L (Player Fit)", true, 1),
                SizeOption("XL (Player Fit)", true, 1)
            ),
            selectedSize = "L (Player Fit)",
            colorway = "Pitch Black / Shadow Floral",
            releaseYear = "2024",
            description = "Ultra-rare collaborative match kit designed by legendary Japanese master Yohji Yamamoto. Features subtle tonal floral jacquard print, signature Y-3 badge on chest, and HEAT.RDY competition fabric. Numbered collector edition.",
            seller = sellerArchiveTokyo,
            isSold = false,
            isFeatured = false,
            primaryImageRes = R.drawable.img_hero_banner,
            galleryImages = listOf(
                R.drawable.img_hero_banner,
                R.drawable.img_store_banner
            )
        ),
        Product(
            id = "prod_ch_dagger",
            title = "Chrome Hearts Dagger Pendant .925 Sterling",
            brand = "Chrome Hearts",
            category = ProductCategory.ACCESSORIES,
            price = 850,
            originalPrice = 1100,
            condition = "VNDS / Pristine w/ Pouch",
            sizeOptions = listOf(
                SizeOption("Standard (45mm)", true, 1)
            ),
            selectedSize = "Standard (45mm)",
            colorway = "Antiqued Sterling Silver",
            releaseYear = "2023",
            description = "Iconic Chrome Hearts #10 Dagger pendant crafted in heavyweight 925 sterling silver with darkened oxidation patina. Sourced from the Honolulu boutique. Includes original suede dust pouch and store sales receipt.",
            seller = sellerVaultLondon,
            isSold = false,
            isFeatured = true,
            primaryImageRes = R.drawable.img_kicks_travis,
            galleryImages = listOf(
                R.drawable.img_kicks_travis,
                R.drawable.img_store_banner
            )
        ),
        Product(
            id = "prod_offwhite_volt",
            title = "Off-White x Nike Air Force 1 Low 'Volt'",
            brand = "Off-White x Nike",
            category = ProductCategory.KICKS,
            price = 1450,
            originalPrice = 1800,
            condition = "Deadstock / Factory Laced",
            sizeOptions = listOf(
                SizeOption("US 9.0", true, 1),
                SizeOption("US 9.5", true, 1),
                SizeOption("US 10.5", true, 1),
                SizeOption("US 11.0", false, 0)
            ),
            selectedSize = "US 10.5",
            colorway = "Volt / Cone / Black / Hyper Jade",
            releaseYear = "2018",
            description = "Virgil Abloh's legendary high-visibility masterpiece. Deconstructed Ripstop canvas with exposed foam collar, stitched metallic silver Swoosh with orange tab, and signature zip-tie intact. Authenticity certificate included.",
            seller = sellerApexLA,
            isSold = false,
            isFeatured = true,
            primaryImageRes = R.drawable.img_kicks_travis,
            galleryImages = listOf(
                R.drawable.img_kicks_travis,
                R.drawable.img_hero_banner
            )
        ),
        Product(
            id = "prod_psg_jordan",
            title = "Paris Saint-Germain x Jordan 4th Dri-FIT Kit",
            brand = "Jordan Brand x PSG",
            category = ProductCategory.KITS,
            price = 240,
            originalPrice = 280,
            condition = "Deadstock / Tags Attached",
            sizeOptions = listOf(
                SizeOption("Medium", true, 3),
                SizeOption("Large", true, 2),
                SizeOption("X-Large", true, 1)
            ),
            selectedSize = "Large",
            colorway = "Elephant Print / Infrared 23",
            releaseYear = "2024",
            description = "Official player-spec edition with authentic flocking and Ligue 1 champion badges. Incorporates iconic Air Jordan elephant print gradient on moisture-wicking engineered mesh.",
            seller = sellerVaultLondon,
            isSold = false,
            isFeatured = false,
            primaryImageRes = R.drawable.img_hero_banner,
            galleryImages = listOf(
                R.drawable.img_hero_banner
            )
        ),
        Product(
            id = "prod_bogo_olive",
            title = "Supreme Box Logo Heavyweight Hoodie 'Olive'",
            brand = "Supreme New York",
            category = ProductCategory.DRIP,
            price = 560,
            originalPrice = 700,
            condition = "Deadstock / Sealed Plastic",
            sizeOptions = listOf(
                SizeOption("Medium", true, 1),
                SizeOption("Large", true, 2),
                SizeOption("X-Large", true, 1)
            ),
            selectedSize = "Large",
            colorway = "Olive Drab / Red Embroidered Bogo",
            releaseYear = "2023",
            description = "Heavyweight 450 GSM crossgrain cotton fleece with ribbed gusset side panels. Clean dense embroidery on the chest box logo. Direct pickup from Supreme Bowery NY.",
            seller = sellerNocturnal,
            isSold = false,
            isFeatured = false,
            primaryImageRes = R.drawable.img_drip_jacket,
            galleryImages = listOf(
                R.drawable.img_drip_jacket
            )
        ),
        Product(
            id = "prod_ow_belt",
            title = "Off-White 2.0 Heavy Industrial Webbing Belt",
            brand = "Off-White c/o Virgil Abloh",
            category = ProductCategory.ACCESSORIES,
            price = 280,
            originalPrice = 350,
            condition = "Brand New in Box",
            sizeOptions = listOf(
                SizeOption("One Size (200cm)", true, 4)
            ),
            selectedSize = "One Size (200cm)",
            colorway = "Industrial Yellow / Neon Text / Matte Black Buckle",
            releaseYear = "2023",
            description = "200cm heavy-duty polyamide webbing with contrast black engineered lettering and powder-coated military roller buckle with engraved 'PULL' accent.",
            seller = sellerNocturnal,
            isSold = false,
            isFeatured = false,
            primaryImageRes = R.drawable.img_drip_jacket,
            galleryImages = listOf(
                R.drawable.img_drip_jacket
            )
        )
    )

    // Historical sold grails for the Seller Storefront "Sold Items" tab
    val soldProducts = listOf(
        Product(
            id = "sold_travis_frag",
            title = "Travis Scott x Fragment x Air Jordan 1 High OG",
            brand = "Jordan x Fragment Design",
            category = ProductCategory.KICKS,
            price = 3400,
            originalPrice = 3800,
            condition = "Deadstock / Verified Authenticated",
            sizeOptions = listOf(SizeOption("US 10.5", false, 0)),
            selectedSize = "US 10.5",
            colorway = "Sail / Black / Military Blue / Shy Pink",
            releaseYear = "2021",
            description = "Sold directly to collector via Dial the Plug VIP concierge. Signed and verified with NFC proof of provenance.",
            seller = sellerNocturnal,
            isSold = true,
            primaryImageRes = R.drawable.img_kicks_travis
        ),
        Product(
            id = "sold_red_october",
            title = "Nike Air Yeezy 2 NRG 'Red October'",
            brand = "Nike Sportswear",
            category = ProductCategory.KICKS,
            price = 14500,
            originalPrice = 16000,
            condition = "Deadstock / Vault Preserved",
            sizeOptions = listOf(SizeOption("US 11.0", false, 0)),
            selectedSize = "US 11.0",
            colorway = "Red / Red / Red",
            releaseYear = "2014",
            description = "Holy grail status. Complete with original gold lace aglets, dust bag, and verification certificate. Hand-delivered in NYC.",
            seller = sellerNocturnal,
            isSold = true,
            primaryImageRes = R.drawable.img_kicks_travis
        ),
        Product(
            id = "sold_kermit_tee",
            title = "Supreme x Terry Richardson Kermit the Frog Tee (2008)",
            brand = "Supreme New York",
            category = ProductCategory.DRIP,
            price = 1850,
            originalPrice = 2200,
            condition = "VNDS 9.5/10 / Archive Preserved",
            sizeOptions = listOf(SizeOption("Medium", false, 0)),
            selectedSize = "Medium",
            colorway = "White / Kermit Photo Print",
            releaseYear = "2008",
            description = "Museum grade archive piece from the infamous 2008 campaign photographed by Terry Richardson.",
            seller = sellerNocturnal,
            isSold = true,
            primaryImageRes = R.drawable.img_drip_jacket
        ),
        Product(
            id = "sold_dior_jordan",
            title = "Dior x Air Jordan 1 High OG Limited 8500",
            brand = "Dior x Jordan",
            category = ProductCategory.KICKS,
            price = 7200,
            originalPrice = 8500,
            condition = "Deadstock / Complete Set with Metal Hangtags",
            sizeOptions = listOf(SizeOption("US 10.0", false, 0)),
            selectedSize = "US 10.0",
            colorway = "Grey / White / Oblique Jacquard",
            releaseYear = "2020",
            description = "Numbered 4,218 of 8,500 worldwide pairs. Made in Italy with finest calf leather and Dior Oblique jacquard swooshes.",
            seller = sellerNocturnal,
            isSold = true,
            primaryImageRes = R.drawable.img_kicks_travis
        )
    )
}
