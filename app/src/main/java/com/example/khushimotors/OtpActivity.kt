package com.example.khushimotors

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.example.khushimotors.databinding.FragmentOtpVerifyBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: FragmentOtpVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phone = intent.getStringExtra(EXTRA_PHONE) ?: ""
        if (phone.isNotEmpty()) {
            binding.tvSubtitle.text = "We sent a 6-digit code to +91 $phone"
        }

        setupOtpAutoFocus()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnClose.setOnClickListener { finish() }

        binding.btnVerify.setOnClickListener {
            val otp = "${binding.etOtp1.text}${binding.etOtp2.text}${binding.etOtp3.text}" +
                      "${binding.etOtp4.text}${binding.etOtp5.text}${binding.etOtp6.text}"
            if (otp.length == 6) {
                Toast.makeText(this, "OTP Verified! Welcome to Khushi Motors", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                )
            } else {
                Toast.makeText(this, "Please enter the 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }

        setupFooterLinks()

        // Auto-open keyboard on first box
        binding.etOtp1.requestFocus()
    }

    // ── Auto-focus between OTP boxes ───────────────────────────────────────
    private fun setupOtpAutoFocus() {
        val boxes = listOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )

        boxes.forEachIndexed { index, editText ->

            // Forward: type a digit → move to next box
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        // Move to next box
                        if (index < boxes.lastIndex) {
                            boxes[index + 1].requestFocus()
                        } else {
                            // Last box filled — hide keyboard
                            editText.clearFocus()
                            hideKeyboard(editText)
                        }
                    }
                }
            })

            // Backward: backspace on empty box → move to previous box
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL
                    && event.action == KeyEvent.ACTION_DOWN
                    && editText.text.isEmpty()
                    && index > 0
                ) {
                    boxes[index - 1].apply {
                        requestFocus()
                        text.clear()
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // ── Footer links ───────────────────────────────────────────────────────
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
                        Toast.makeText(this@OtpActivity, "$word clicked", Toast.LENGTH_SHORT).show()
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

    companion object {
        const val EXTRA_PHONE = "phone"
    }
}
