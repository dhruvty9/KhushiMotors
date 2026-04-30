package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.khushimotors.databinding.ActivityServiceDetailBinding

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: "Service"
        val description = intent.getStringExtra("description") ?: ""
        val emoji = intent.getStringExtra("emoji") ?: "🔧"

        binding.tvServiceEmoji.text = emoji
        binding.tvServiceTitle.text = title
        binding.tvServiceDescription.text = description

        binding.btnBack.setOnClickListener { finish() }

        // "Book This Service" — Car Service & Car Washing go through the booking flow,
        // all other services go directly to WhatsApp
        binding.btnBookThisService.setOnClickListener {
            when (title) {
                "Car Service" -> {
                    startActivity(Intent(this, BookingActivity::class.java).apply {
                        putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
                    })
                }
                "Car Washing" -> {
                    startActivity(Intent(this, BookingActivity::class.java).apply {
                        putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
                    })
                }
                else -> openWhatsApp("Hi, I want to book $title service")
            }
        }

        binding.btnCallService.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
        }

        binding.btnWhatsappService.setOnClickListener {
            openWhatsApp("Hi, I want to book $title service")
        }
    }

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
}
