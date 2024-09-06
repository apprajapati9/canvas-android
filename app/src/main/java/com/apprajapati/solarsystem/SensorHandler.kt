package com.apprajapati.solarsystem

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService

class SensorHandler(context: Context) {

    var sensorManager : SensorManager ?= null
    var sensors: List<Sensor> = emptyList()

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    fun getAllSensors(): List<Sensor> {
        sensors = sensorManager!!.getSensorList(Sensor.TYPE_ALL)
        return sensors
    }

}