package com.apprajapati.solarsystem.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apprajapati.solarsystem.R
import com.apprajapati.solarsystem.databinding.FragmentPcPointsBinding

class PCPointsFragment : Fragment(R.layout.fragment_pc_points) {

    private var _binding: FragmentPcPointsBinding ? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPcPointsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding?.textview?.setOnClickListener{
//            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
//        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("Ajay", "pc points- start")

    }

    override fun onPause() {
        super.onPause()
        Log.d("Ajay", "pc points- pause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}