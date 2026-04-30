package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.khushimotors.databinding.ActivityBookingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.launch
import org.json.JSONObject

class BookingActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityBookingBinding

    private var serviceType: String = "service" // "service" or "washing"
    private var serviceId: String = ""

    private var selectedBrand: Brand? = null
    private var selectedModel: CarModel? = null
    private var selectedPackageName: String = ""

    private var brands: List<Brand> = emptyList()
    private var models: List<CarModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Checkout.preload(applicationContext)

        serviceType = intent.getStringExtra(EXTRA_SERVICE_TYPE) ?: "service"

        setupHeader()
        fetchBrandsAndServices()

        binding.btnViewPackages.setOnClickListener {
            fetchPricesAndShowSheet()
        }
    }

    // ── Header ─────────────────────────────────────────────────────────────
    private fun setupHeader() {
        binding.btnBack.setOnClickListener { finish() }
        if (serviceType == "washing") {
            binding.tvHeaderEmoji.text = "🚿"
            binding.tvHeaderTitle.text = "Book Car Wash"
            binding.tvHeaderSubtitle.text = "Select your car to see wash packages"
        } else {
            binding.tvHeaderEmoji.text = "🔧"
            binding.tvHeaderTitle.text = "Book Car Service"
            binding.tvHeaderSubtitle.text = "Select your car to see service packages"
        }
    }

    // ── Fetch brands + services ────────────────────────────────────────────
    private fun fetchBrandsAndServices() {
        lifecycleScope.launch {
            try {
                // Fetch brands
                val brandsResp = RetrofitClient.instance.getBrands()
                brands = brandsResp.brands

                // Fetch services to get the serviceId
                val servicesResp = RetrofitClient.instance.getServices()
                val targetName = if (serviceType == "washing") "Car Washing" else "Car Service"
                serviceId = servicesResp.services.find { it.name == targetName }?._id ?: ""

                if (serviceId.isEmpty()) {
                    Toast.makeText(this@BookingActivity, "Service not found", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }

                setupBrandSpinner()

            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch brands/services", e)
                Toast.makeText(this@BookingActivity, "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    // ── Brand Spinner ──────────────────────────────────────────────────────
    private fun setupBrandSpinner() {
        val brandNames = listOf("Select Brand") + brands.map { it.name }
        val adapter = ArrayAdapter(this, R.layout.item_spinner, brandNames)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerBrand.adapter = adapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                if (pos == 0) {
                    selectedBrand = null
                    binding.layoutStep2.visibility = View.GONE
                    binding.btnViewPackages.visibility = View.GONE
                } else {
                    selectedBrand = brands[pos - 1]
                    fetchModelsAndSetupSpinner(selectedBrand!!._id)
                    binding.layoutStep2.visibility = View.VISIBLE
                    binding.btnViewPackages.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // ── Fetch models + setup spinner ───────────────────────────────────────
    private fun fetchModelsAndSetupSpinner(brandId: String) {
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.getModels(brandId)
                models = resp.models ?: emptyList()

                val modelNames = listOf("Select Model") + models.map { it.name }
                val adapter = ArrayAdapter(this@BookingActivity, R.layout.item_spinner, modelNames)
                adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
                binding.spinnerModel.adapter = adapter

                binding.spinnerModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                        if (pos == 0) {
                            selectedModel = null
                            binding.btnViewPackages.visibility = View.GONE
                        } else {
                            selectedModel = models[pos - 1]
                            binding.btnViewPackages.visibility = View.VISIBLE
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch models", e)
                Toast.makeText(this@BookingActivity, "Failed to load models", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ── Fetch prices + show bottom sheet ───────────────────────────────────
    private fun fetchPricesAndShowSheet() {
        val brand = selectedBrand ?: return
        val model = selectedModel ?: return

        val dialog = BottomSheetDialog(this)
        val sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_packages, null)
        dialog.setContentView(sheetView)

        sheetView.findViewById<TextView>(R.id.tv_sheet_car).text = "${brand.name} ${model.name}"
        sheetView.findViewById<TextView>(R.id.tv_sheet_title).text =
            if (serviceType == "washing") "Wash Packages" else "Service Packages"

        val loadingView  = sheetView.findViewById<View>(R.id.layout_loading)
        val errorView    = sheetView.findViewById<View>(R.id.layout_error)
        val layoutService = sheetView.findViewById<LinearLayout>(R.id.layout_service_packages)
        val layoutWash    = sheetView.findViewById<LinearLayout>(R.id.layout_wash_packages)

        loadingView.visibility  = View.VISIBLE
        errorView.visibility    = View.GONE
        layoutService.visibility = View.GONE
        layoutWash.visibility   = View.GONE

        sheetView.findViewById<ImageView>(R.id.btn_close_sheet).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.getPackages(
                    brandId   = brand._id,
                    modelId   = model._id,
                    serviceId = serviceId
                )

                val packages = resp.packages ?: emptyList()
                val result = parsePackages(packages)

                loadingView.visibility = View.GONE

                if (serviceType == "washing") {
                    layoutWash.visibility = View.VISIBLE
                    bindWashPrices(sheetView, result, dialog)
                } else {
                    layoutService.visibility = View.VISIBLE
                    bindServicePrices(sheetView, result, dialog)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Pricing fetch failed", e)
                loadingView.visibility = View.GONE
                errorView.visibility   = View.VISIBLE

                val errorMsg = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Cannot reach server.\nCheck your internet connection."
                    e.message?.contains("timeout") == true ->
                        "Request timed out.\nCheck your network connection."
                    else -> "Error: ${e.message}"
                }
                sheetView.findViewById<TextView>(R.id.tv_error_detail).text = errorMsg

                sheetView.findViewById<Button>(R.id.btn_retry).setOnClickListener {
                    dialog.dismiss()
                    fetchPricesAndShowSheet()
                }
            }
        }
    }

    // ── Parse packages into basic/standard/premium ─────────────────────────
    private fun parsePackages(packages: List<LivePackage>): PricingResult {
        var basic: LivePackage? = null
        var standard: LivePackage? = null
        var premium: LivePackage? = null

        packages.forEach { pkg ->
            val name = (pkg.packageName ?: pkg.title ?: "").lowercase()
            when {
                name.contains("basic")    -> basic    = pkg
                name.contains("standard") -> standard = pkg
                name.contains("premium")  -> premium  = pkg
            }
        }

        return PricingResult(basic, standard, premium)
    }

    // ── Bind service prices ────────────────────────────────────────────────
    private fun bindServicePrices(
        sheetView: View,
        result: PricingResult,
        dialog: BottomSheetDialog
    ) {
        sheetView.findViewById<TextView>(R.id.tv_price_basic_service).text    = "₹${formatPrice(result.basicPackage?.price ?: 0)}"
        sheetView.findViewById<TextView>(R.id.tv_price_standard_service).text = "₹${formatPrice(result.standardPackage?.price ?: 0)}"
        sheetView.findViewById<TextView>(R.id.tv_price_premium_service).text  = "₹${formatPrice(result.premiumPackage?.price ?: 0)}"

        sheetView.findViewById<Button>(R.id.btn_sheet_basic_service).setOnClickListener {
            selectedPackageName = "Basic Service"
            dialog.dismiss()
            startRazorpayPayment()
        }
        sheetView.findViewById<Button>(R.id.btn_sheet_standard_service).setOnClickListener {
            selectedPackageName = "Standard Service"
            dialog.dismiss()
            startRazorpayPayment()
        }
        sheetView.findViewById<Button>(R.id.btn_sheet_premium_service).setOnClickListener {
            selectedPackageName = "Premium Service"
            dialog.dismiss()
            startRazorpayPayment()
        }
    }

    // ── Bind wash prices ───────────────────────────────────────────────────
    private fun bindWashPrices(
        sheetView: View,
        result: PricingResult,
        dialog: BottomSheetDialog
    ) {
        sheetView.findViewById<TextView>(R.id.tv_price_basic_wash).text    = "₹${formatPrice(result.basicPackage?.price ?: 0)}"
        sheetView.findViewById<TextView>(R.id.tv_price_standard_wash).text = "₹${formatPrice(result.standardPackage?.price ?: 0)}"
        sheetView.findViewById<TextView>(R.id.tv_price_premium_wash).text  = "₹${formatPrice(result.premiumPackage?.price ?: 0)}"

        sheetView.findViewById<Button>(R.id.btn_sheet_basic_wash).setOnClickListener {
            selectedPackageName = "Basic Wash"
            dialog.dismiss()
            startRazorpayPayment()
        }
        sheetView.findViewById<Button>(R.id.btn_sheet_standard_wash).setOnClickListener {
            selectedPackageName = "Standard Wash"
            dialog.dismiss()
            startRazorpayPayment()
        }
        sheetView.findViewById<Button>(R.id.btn_sheet_premium_wash).setOnClickListener {
            selectedPackageName = "Premium Wash"
            dialog.dismiss()
            startRazorpayPayment()
        }
    }

    // ── Razorpay ───────────────────────────────────────────────────────────
    private fun startRazorpayPayment() {
        val checkout = Checkout()
        // Replace with your actual Razorpay Key ID from dashboard.razorpay.com
        checkout.setKeyID("rzp_test_XXXXXXXXXXXXXXXX")

        try {
            val options = JSONObject().apply {
                put("name", "Khushi Motors")
                put("description", "$selectedPackageName — ${selectedBrand?.name} ${selectedModel?.name}")
                put("theme.color", "#C0392B")
                put("currency", "INR")
                put("amount", 10000) // ₹100 in paise
                put("prefill", JSONObject().apply {
                    put("contact", "")
                    put("email", "")
                })
                put("notes", JSONObject().apply {
                    put("package", selectedPackageName)
                    put("car", "${selectedBrand?.name} ${selectedModel?.name}")
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Payment failed to start: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        val message = "Hi, I have booked *$selectedPackageName* for my *${selectedBrand?.name} ${selectedModel?.name}*.\n" +
                "Payment ID: $razorpayPaymentId\n" +
                "Advance paid: ₹100. Please confirm my appointment."
        openWhatsApp(message)
        Toast.makeText(this, "Booking confirmed! Payment ID: $razorpayPaymentId", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment cancelled or failed. Please try again.", Toast.LENGTH_SHORT).show()
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private fun openWhatsApp(message: String) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/916397759757?text=${Uri.encode(message)}"))
            )
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
        }
    }

    private fun formatPrice(amount: Int): String {
        return if (amount >= 1000) {
            val thousands = amount / 1000
            val remainder = amount % 1000
            if (remainder == 0) "${thousands},000" else "$thousands,${remainder.toString().padStart(3, '0')}"
        } else {
            amount.toString()
        }
    }

    companion object {
        private const val TAG = "BookingActivity"
        const val EXTRA_SERVICE_TYPE = "service_type"
    }
}
