package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.khushimotors.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCallAbout.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757"))
            startActivity(intent)
        }

        binding.btnWhatsappAbout.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/916397759757?text=Hi, I want to know more about Khushi Motors"))
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757"))
                startActivity(intent)
            }
        }

        binding.btnBookAbout.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/916397759757?text=${Uri.encode("Hi, I want to book a car service")}")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:6397759757")))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
