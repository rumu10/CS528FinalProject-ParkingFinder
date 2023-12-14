package com.example.firebasep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firebasep.databinding.ActivityParkingBinding

class ParkingActivity : AppCompatActivity() {
    // Declare binding as a lateinit var, which means it will be initialized later
    private lateinit var binding: ActivityParkingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for ParkingActivity
        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize with a default fragment, such as a home fragment
        replaceFragment(ParkingFragment())

        // Assuming you have a BottomNavigationView in your parking app as well
        binding.bottomNavigationView.apply {
            setBackground(null)
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.myparking -> replaceFragment(ParkingFragment())
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.subscriptions -> replaceFragment(UserProfileFragment())
                    R.id.more -> replaceFragment(MoreFragment())
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
}
