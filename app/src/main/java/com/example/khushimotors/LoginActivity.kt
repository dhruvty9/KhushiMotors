package com.example.khushimotors

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.example.khushimotors.databinding.FragmentLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Close button — exit app
        binding.btnClose.setOnClickListener { finish() }

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhoneNumber.text.toString().trim()
            if (phone.length == 10) {
                val intent = Intent(this, OtpActivity::class.java).apply {
                    putExtra(OtpActivity.EXTRA_PHONE, phone)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginAdmin.setOnClickListener {
            Toast.makeText(this, "Admin Login", Toast.LENGTH_SHORT).show()
        }

        setupFooterLinks()
    }

    private fun setupFooterLinks() {
        val footerText = "By continuing, you agree to our Terms of Service & Privacy Policy"
        val spannable  = SpannableString(footerText)
        val redColor   = "#EF4444".toColorInt()

        listOf("Terms of Service", "Privacy Policy").forEach { word ->
            val start = footerText.indexOf(word)
            if (start != -1) {
                spannable.setSpan(ForegroundColorSpan(redColor), start, start + word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        Toast.makeText(this@LoginActivity, "$word clicked", Toast.LENGTH_SHORT).show()
                    }
                    override fun updateDrawState(ds: android.text.TextPaint) {
                        super.updateDrawState(ds); ds.isUnderlineText = false
                    }
                }, start, start + word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        binding.tvFooter.text = spannable
        binding.tvFooter.movementMethod = LinkMovementMethod.getInstance()
        binding.tvFooter.highlightColor = Color.TRANSPARENT
    }
}
