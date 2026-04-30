package com.example.khushimotors

import retrofit2.http.GET
import retrofit2.http.Query

// ── Brand ──────────────────────────────────────────────────────────────────
data class Brand(
    val _id: String,
    val name: String,
    val logo: String? = null
)

data class BrandsResponse(
    val success: Boolean,
    val brands: List<Brand>
)

// ── Model ──────────────────────────────────────────────────────────────────
data class CarModel(
    val _id: String,
    val name: String,
    val brandId: String? = null,
    val image: String? = null
)

data class ModelsResponse(
    val success: Boolean? = null,
    val models: List<CarModel>? = null
)

// ── Service ────────────────────────────────────────────────────────────────
data class ServiceItem(
    val _id: String,
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    // legacy fields kept for compatibility
    val id: Int = 0,
    val title: String = name,
    val price: String = "",
    val imageUrl: String = ""
)

data class ServicesResponse(
    val success: Boolean,
    val services: List<ServiceItem>
)

// ── Package / Pricing ──────────────────────────────────────────────────────
data class LivePackage(
    val _id: String? = null,
    val packageName: String? = null,
    val title: String? = null,
    val price: Int? = null,
    val features: List<String>? = null,
    val recommended: Boolean? = null
)

data class PackagesResponse(
    val success: Boolean? = null,
    val packages: List<LivePackage>? = null
)

// Convenience wrapper used by BookingActivity
data class PackagePrices(
    val basic: Int,
    val standard: Int,
    val premium: Int
)

data class PricingResult(
    val basicPackage: LivePackage?,
    val standardPackage: LivePackage?,
    val premiumPackage: LivePackage?
) {
    fun toPrices() = PackagePrices(
        basic    = basicPackage?.price    ?: 0,
        standard = standardPackage?.price ?: 0,
        premium  = premiumPackage?.price  ?: 0
    )
}

// ── API interface ──────────────────────────────────────────────────────────
interface ApiService {

    @GET("brands")
    suspend fun getBrands(): BrandsResponse

    @GET("models")
    suspend fun getModels(@Query("brandId") brandId: String): ModelsResponse

    @GET("services")
    suspend fun getServices(): ServicesResponse

    /**
     * Fetch packages for a brand + model + service combination.
     * GET /api/packages/get?brandId=xxx&modelId=yyy&serviceId=zzz
     */
    @GET("packages/get")
    suspend fun getPackages(
        @Query("brandId")   brandId:   String,
        @Query("modelId")   modelId:   String,
        @Query("serviceId") serviceId: String
    ): PackagesResponse
}
