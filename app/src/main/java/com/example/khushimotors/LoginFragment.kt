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
import com.example.khushimotors.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhoneNumber.text.toString()
            if (phone.length == 10) {
                // Navigate to OtpVerifyFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, OtpVerifyFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Please enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginAdmin.setOnClickListener {
            Toast.makeText(context, "Admin Login", Toast.LENGTH_SHORT).show()
        }

        binding.btnClose.setOnClickListener {
            activity?.finish()
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
