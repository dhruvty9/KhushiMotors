package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.khushimotors.databinding.FragmentServicesBinding

class ServicesFragment : Fragment() {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    data class Service(
        val emoji: String,
        val title: String,
        val description: String,
        val tag: String = ""
    )

    private val services = listOf(
        Service("🔧", "Car Service", "Complete vehicle maintenance and periodic servicing with genuine parts", "MOST POPULAR"),
        Service("🚿", "Car Washing", "Professional car washing with high pressure and foam treatment", ""),
        Service("🔩", "Suspension Repair", "Complete suspension system repair and maintenance for smooth ride", ""),
        Service("🛠️", "General Repair", "All types of vehicle repairs handled by certified technicians", ""),
        Service("❄️", "AC Service", "Air conditioning inspection, repair and gas recharge", ""),
        Service("✨", "Detailing Services", "Premium car detailing for a showroom-quality finish", ""),
        Service("🚨", "Emergency Services", "24/7 emergency roadside assistance anywhere in Dehradun", "URGENT"),
        Service("🎨", "Denting & Painting", "Professional dent removal and paint restoration services", ""),
        Service("📡", "Car Scanning", "Advanced OBD diagnostic scanning for all car brands", "")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ServicesAdapter(services) { service ->
            when (service.title) {
                "Car Service" -> {
                    startActivity(Intent(context, BookingActivity::class.java).apply {
                        putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
                    })
                }
                "Car Washing" -> {
                    startActivity(Intent(context, BookingActivity::class.java).apply {
                        putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
                    })
                }
                else -> {
                    val intent = Intent(context, ServiceDetailActivity::class.java).apply {
                        putExtra("title", service.title)
                        putExtra("description", service.description)
                        putExtra("emoji", service.emoji)
                    }
                    startActivity(intent)
                }
            }
        }
        binding.rvServices.adapter = adapter
        binding.rvServices.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(context, 2)

        binding.btnBookService.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
