package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.khushimotors.databinding.ActivityCarServiceBinding

class CarServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Main CTA — open booking flow
        binding.btnBookCarService.setOnClickListener {
            openBookingActivity()
        }

        // Package-specific buttons — open booking flow (service type pre-selected)
        binding.btnBasicService.setOnClickListener {
            openBookingActivity()
        }
        binding.btnStandardService.setOnClickListener {
            openBookingActivity()
        }
        binding.btnPremiumService.setOnClickListener {
            openBookingActivity()
        }

        binding.btnCallCarService.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
        }
        binding.btnWhatsappCarService.setOnClickListener {
            openWhatsApp("Hi, I want to book a Car Service")
        }
    }

    private fun openBookingActivity() {
        val intent = Intent(this, BookingActivity::class.java).apply {
            putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
        }
        startActivity(intent)
    }

    private fun openWhatsApp(message: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/916397759757?text=${Uri.encode(message)}")))
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
        }
    }
}
