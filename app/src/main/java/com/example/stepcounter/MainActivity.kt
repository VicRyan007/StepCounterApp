package com.example.stepcounter

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_ACTIVITY_RECOGNITION = 1001
        private const val REQUEST_POST_NOTIFICATIONS = 1002
    }

    private lateinit var textViewStepCount: TextView
    private val stepUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val steps = intent.getIntExtra("step_count", 0)
            textViewStepCount.text = "Passos dados: $steps"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewStepCount = findViewById(R.id.textViewStepCount)

        // Solicita a permissão de notificações para Android 13 (Tiramisu) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_POST_NOTIFICATIONS
                )
            }
        }

        // Solicita a permissão ACTIVITY_RECOGNITION para Android 10 (Q) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    REQUEST_ACTIVITY_RECOGNITION
                )
            } else {
                checkAndStartService()
            }
        } else {
            checkAndStartService()
        }
    }

    private fun checkAndStartService() {
        // Verifica se a permissão ACTIVITY_RECOGNITION está concedida (necessária para Android Q+)
        val hasActivityRecognition = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        // Verifica se a permissão POST_NOTIFICATIONS está concedida (necessária para Android Tiramisu+)
        val hasPostNotifications = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (hasActivityRecognition && hasPostNotifications) {
            startStepCounterService()
        }
    }

    private fun startStepCounterService() {
        val serviceIntent = Intent(this, StepCounterService::class.java)
        startForegroundService(serviceIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACTIVITY_RECOGNITION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndStartService()
                } else {
                    textViewStepCount.text = "Permissão de atividade negada."
                }
            }
            REQUEST_POST_NOTIFICATIONS -> {
                // Mesmo que a permissão de notificações seja negada, o serviço pode iniciar,
                // porém a notificação poderá não ser exibida corretamente.
                checkAndStartService()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(stepUpdateReceiver, IntentFilter("com.example.stepcounter.STEP_UPDATE"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(stepUpdateReceiver)
    }
}
