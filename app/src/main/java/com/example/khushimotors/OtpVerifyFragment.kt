package com.example.khushimotors

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.example.khushimotors.databinding.FragmentOtpVerifyBinding

class OtpVerifyFragment : Fragment() {

    private var _binding: FragmentOtpVerifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnClose.setOnClickListener {
            activity?.finish()
        }

        binding.btnVerify.setOnClickListener {
            val otp = "${binding.etOtp1.text}${binding.etOtp2.text}${binding.etOtp3.text}${binding.etOtp4.text}${binding.etOtp5.text}${binding.etOtp6.text}"
            if (otp.length == 6) {
                Toast.makeText(context, "OTP Verified! Welcome to Khushi Motors", Toast.LENGTH_SHORT).show()
                // Navigate to MainActivity (which hosts HomeFragment via bottom nav)
                val intent = android.content.Intent(context, MainActivity::class.java)
                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }

        setupFooterLinks()
    }

    private fun setupFooterLinks() {
        val footerText = "By continuing, you agree to our Terms of Service & Privacy Policy"
        val spannableString = SpannableString(footerText)
        val redColor = "#EF4444".toColorInt()

        val terms = "Terms of Service"
        val privacy = "Privacy Policy"

        val termsStart = footerText.indexOf(terms)
        if (termsStart != -1) {
            spannableString.setSpan(ForegroundColorSpan(redColor), termsStart, termsStart + terms.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) { Toast.makeText(context, "Terms clicked", Toast.LENGTH_SHORT).show() }
                override fun updateDrawState(ds: android.text.TextPaint) { super.updateDrawState(ds); ds.isUnderlineText = false }
            }, termsStart, termsStart + terms.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val privacyStart = footerText.indexOf(privacy)
        if (privacyStart != -1) {
            spannableString.setSpan(ForegroundColorSpan(redColor), privacyStart, privacyStart + privacy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) { Toast.makeText(context, "Privacy clicked", Toast.LENGTH_SHORT).show() }
                override fun updateDrawState(ds: android.text.TextPaint) { super.updateDrawState(ds); ds.isUnderlineText = false }
            }, privacyStart, privacyStart + privacy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvFooter.text = spannableString
        binding.tvFooter.movementMethod = LinkMovementMethod.getInstance()
        binding.tvFooter.highlightColor = Color.TRANSPARENT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
