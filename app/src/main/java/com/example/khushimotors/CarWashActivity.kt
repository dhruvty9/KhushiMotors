package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.khushimotors.databinding.ActivityCarWashBinding

class CarWashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarWashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarWashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Main CTA — open booking flow
        binding.btnBookCarWash.setOnClickListener {
            openBookingActivity()
        }

        // Package-specific buttons — open booking flow (washing type pre-selected)
        binding.btnBasicWash.setOnClickListener {
            openBookingActivity()
        }
        binding.btnStandardWash.setOnClickListener {
            openBookingActivity()
        }
        binding.btnPremiumWash.setOnClickListener {
            openBookingActivity()
        }

        binding.btnCallCarWash.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
        }
        binding.btnWhatsappCarWash.setOnClickListener {
            openWhatsApp("Hi, I want to book a Car Wash")
        }
    }

    private fun openBookingActivity() {
        val intent = Intent(this, BookingActivity::class.java).apply {
            putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
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
