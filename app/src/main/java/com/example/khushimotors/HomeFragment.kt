package com.example.khushimotors

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.khushimotors.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val bannerMessages = listOf(
        "🎉 Anniversary Special — 100% FREE Labour Charges!",
        "🔧 General Service | ❄️ AC Repair | 🎨 Denting & Painting",
        "📞 Book Now: 6397759757 | Serving Since 2009",
        "🚿 Car Washing | 📡 Car Scanning | 🛠️ Emergency Services"
    )
    private var bannerIndex = 0
    private val bannerHandler = Handler(Looper.getMainLooper())
    private val bannerRunnable = object : Runnable {
        override fun run() {
            if (_binding != null) {
                bannerIndex = (bannerIndex + 1) % bannerMessages.size
                binding.tvAnnouncementBanner.text = bannerMessages[bannerIndex]
            }
            bannerHandler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupDrawer()
        setupServiceCards()
        setupPackageButtons()
        animateStats()
    }

    private fun setupBanner() {
        binding.tvAnnouncementBanner.text = bannerMessages[0]
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    private fun setupDrawer() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.drawerMenuInclude.btnCloseDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
        binding.drawerMenuInclude.menuHome.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
        binding.drawerMenuInclude.menuServices.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            (activity as? MainActivity)?.navigateToTab(R.id.nav_services)
        }
        binding.drawerMenuInclude.menuAbout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            (activity as? MainActivity)?.navigateToTab(R.id.nav_about)
        }
        binding.drawerMenuInclude.menuContact.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            (activity as? MainActivity)?.navigateToTab(R.id.nav_contact)
        }
        binding.drawerMenuInclude.menuBooking.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            openWhatsApp("916397759757", "Hi, I want to book a service")
        }
        binding.drawerMenuInclude.menuCallDrawer.setOnClickListener {
            dialPhone("6397759757")
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun setupServiceCards() {
        binding.sectionServices.cardCarService.setOnClickListener {
            startActivity(Intent(context, CarServiceActivity::class.java))
        }
        binding.sectionServices.cardCarWash.setOnClickListener {
            startActivity(Intent(context, CarWashActivity::class.java))
        }
        binding.sectionServices.cardSuspension.setOnClickListener {
            openServiceDetail("Suspension Repair", "Complete suspension system repair and maintenance")
        }
        binding.sectionServices.cardGeneralRepair.setOnClickListener {
            openServiceDetail("General Repair", "All types of vehicle repairs handled by experts")
        }
        binding.sectionServices.cardAcService.setOnClickListener {
            openServiceDetail("AC Service", "Air conditioning inspection, repair and recharge")
        }
        binding.sectionServices.cardDetailing.setOnClickListener {
            openServiceDetail("Detailing Services", "Premium car detailing for a showroom finish")
        }
        binding.sectionServices.cardEmergency.setOnClickListener {
            openServiceDetail("Emergency Services", "24/7 emergency roadside assistance anywhere")
        }
        binding.sectionServices.btnViewAllServices.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.nav_services)
        }
        binding.btnCheckPrice.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(R.id.nav_services)
        }
    }

    private fun setupPackageButtons() {
        // Service packages — open booking flow so customer selects their car first
        binding.sectionServicePackages.btnBookBasicService.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
            })
        }
        binding.sectionServicePackages.btnBookStandardService.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
            })
        }
        binding.sectionServicePackages.btnBookPremiumService.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "service")
            })
        }
        // Wash packages — open booking flow so customer selects their car first
        binding.sectionWashingPackages.btnBookBasicWash.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
            })
        }
        binding.sectionWashingPackages.btnBookStandardWash.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
            })
        }
        binding.sectionWashingPackages.btnBookPremiumWash.setOnClickListener {
            startActivity(Intent(context, BookingActivity::class.java).apply {
                putExtra(BookingActivity.EXTRA_SERVICE_TYPE, "washing")
            })
        }
    }

    private fun animateStats() {
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up_item)
        binding.statsRow.startAnimation(slideUp)
    }

    private fun openServiceDetail(title: String, description: String) {
        startActivity(Intent(context, ServiceDetailActivity::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
        })
    }

    private fun dialPhone(number: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    }

    private fun openWhatsApp(number: String, message: String = "Hi, I want to book a car service") {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/$number?text=${Uri.encode(message)}")))
        } catch (e: Exception) {
            dialPhone("6397759757")
        }
    }

    override fun onResume() {
        super.onResume()
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        bannerHandler.removeCallbacks(bannerRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerHandler.removeCallbacks(bannerRunnable)
        _binding = null
    }
}
