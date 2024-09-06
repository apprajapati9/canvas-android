package com.apprajapati.solarsystem

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.apprajapati.solarsystem.databinding.ActivityMainBinding
import com.apprajapati.solarsystem.fragments.PCPointsFragment
import com.apprajapati.solarsystem.fragments.SolarViewFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        //Setting navbar and status bar color from here.
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, R.color.transparent)),
            statusBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, R.color.transparent))
        ) // This determined status bar and navigation bar color, investigate.
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //This adds margins for bottom navigation bar so view doesn't go beyond navigation bar buttons of android system
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.solarFragmentButton.setOnClickListener{
            replaceFragment(SolarViewFragment())
        }

        binding.pcPointsFragmentButton.setOnClickListener{
            replaceFragment(PCPointsFragment())
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    private fun replaceFragment(fragment: Fragment){
        val ft: FragmentTransaction = supportFragmentManager.
        beginTransaction().
        setReorderingAllowed(true)

        ft.replace(binding.fragmentContainerView.id, fragment).commit()
        //TODO: look for ways to prevent this if current fragment is same as function param fragment
    }

    private fun getCurrentFragment(): Fragment? {
        // Get the FragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        // Find the fragment currently associated with the FragmentContainerView
        return fragmentManager.findFragmentById(binding.fragmentContainerView.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}