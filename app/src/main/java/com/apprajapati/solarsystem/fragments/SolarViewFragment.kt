package com.apprajapati.solarsystem.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apprajapati.solarsystem.R
import com.apprajapati.solarsystem.databinding.FragmentSolarViewBinding

class SolarViewFragment : Fragment(R.layout.fragment_solar_view){

    private var _binding: FragmentSolarViewBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolarViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        binding.solarView.startThread()
    }

    override fun onPause() {
        super.onPause()
        binding.solarView.stopThread()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}