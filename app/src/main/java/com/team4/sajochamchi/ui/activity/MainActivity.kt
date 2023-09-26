package com.team4.sajochamchi.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team4.sajochamchi.R
import com.team4.sajochamchi.databinding.ActivityMainBinding
import com.team4.sajochamchi.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment: HomeFragment = HomeFragment.newInstance()
    //private val searchFragment : HomeFragment = HomeFragment.newInstance()
    //private val myPageFragment : HomeFragment = HomeFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        //nav view
        bottomNavigationView.itemIconTintList = null

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment).commit()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment)
                        .commit()
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}