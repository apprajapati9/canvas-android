package com.apprajapati.solarsystem

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var solarView: SolarView
    override fun onCreate(savedInstanceState: Bundle?) {
        //Setting navbar and status bar color from here.
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, R.color.transparent)),
            statusBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this, R.color.transparent))
        ) // This determined status bar and navigation bar color, investigate.

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //window.navigationBarColor = ContextCompat.getColor(this, R.color.black) // This works too but edgeToEdge() is the way to go.
        solarView = findViewById(R.id.solarView)
    }

    override fun onPause() {
        super.onPause()
        solarView.stopThread()
    }

    override fun onStart() {
        super.onStart()
        solarView.startThread()
    }
}