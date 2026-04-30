package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.khushimotors.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCallPrimary.setOnClickListener { dialPhone("6397759757") }
        binding.btnCallSecondary.setOnClickListener { dialPhone("9368991221") }
        binding.btnWhatsappContact.setOnClickListener { openWhatsApp("916397759757") }
        binding.btnEmailContact.setOnClickListener { sendEmail() }
        binding.btnDirectionsContact.setOnClickListener { openMaps() }
        binding.btnBookContact.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/916397759757?text=${Uri.encode("Hi, I want to book a car service")}")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
            }
        }
        binding.btnSendMessage.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || message.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val whatsappMsg = "Hi, I'm $name (Ph: $phone). $message"
        try {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/916397759757?text=${Uri.encode(whatsappMsg)}"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Message sent! We'll contact you soon.", Toast.LENGTH_LONG).show()
        }
    }

    private fun dialPhone(number: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    }

    private fun openWhatsApp(number: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/$number?text=Hi, I want to book a car service"))
            startActivity(intent)
        } catch (e: Exception) {
            dialPhone("6397759757")
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:Khushimotorsgms@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Service Inquiry - Khushi Motors")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, "Email: Khushimotorsgms@gmail.com", Toast.LENGTH_LONG).show()
        }
    }

    private fun openMaps() {
        val uri = Uri.parse("geo:30.3165,78.0322?q=Khushi+Motors+GMS+Road+Dehradun")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/?q=Khushi+Motors+GMS+Road+Dehradun"))
            startActivity(webIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
