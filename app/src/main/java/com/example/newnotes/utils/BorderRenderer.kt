package com.example.newnotes.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.newnotes.listeners.RotationUpdateListener

class BorderRenderer(
    context: Context/*,
    private val listener: RotationUpdateListener*/
) : SensorEventListener {
    private val sensorManager = context
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager
        .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val listeners = ArrayList<RotationUpdateListener>()

    fun start() {
        sensorManager.registerListener(this, sensor, 100000000)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun addListener(listener: RotationUpdateListener) {
        listeners.add(listener)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var updatedX = 0f
        if (event != null) {
            updatedX = event.values[1] * 2
            if (updatedX < -0.5) {
                updatedX = -0.5f
            }
            if (updatedX > 0.5) {
                updatedX = 0.5f
            }
        }

        listeners.forEach {
            it.update(updatedX)
        }
       // listener.update(updatedX)
    }
}