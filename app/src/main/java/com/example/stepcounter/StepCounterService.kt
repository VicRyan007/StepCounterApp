package com.example.stepcounter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    // Valor base capturado na primeira leitura para contagem relativa
    private var baseline: Int? = null
    // Contagem de passos a partir do baseline
    private var stepCount = 0
    // Armazena a última contagem notificada para atualizar apenas a cada 2 passos
    private var lastUpdatedStepCount = 0

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensor?.also { sensor ->
            // Usando SENSOR_DELAY_UI para atualizações um pouco mais rápidas
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        startForegroundServiceWithNotification()
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "step_counter_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // IMPORTANCE_DEFAULT para garantir que a notificação seja visível na barra
            val channel = NotificationChannel(
                channelId,
                "Contador de Passos",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
        val notification = buildNotification()
        startForeground(1, notification)
    }

    private fun buildNotification(): Notification {
        // Pending intent para abrir a MainActivity ao clicar na notificação
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationText = "Passos dados: $stepCount"
        return NotificationCompat.Builder(this, "step_counter_channel")
            .setContentTitle("Contador de Passos")
            .setContentText(notificationText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Notificação permanente
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // START_STICKY garante que o serviço seja reiniciado se for finalizado
        return START_STICKY
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            // O sensor retorna o total de passos desde o boot
            val totalSteps = event.values[0].toInt()

            // Se ainda não foi definido o baseline, armazena o valor atual
            if (baseline == null) {
                baseline = totalSteps
            }
            // Calcula os passos dados a partir do baseline
            stepCount = totalSteps - (baseline ?: totalSteps)

            // Atualiza somente se a diferença for de 2 passos ou mais
            if (stepCount - lastUpdatedStepCount >= 2) {
                lastUpdatedStepCount = stepCount

                // Atualiza a notificação
                val notification = buildNotification()
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)

                // Envia broadcast para atualizar a UI da MainActivity
                val uiIntent = Intent("com.example.stepcounter.STEP_UPDATE")
                uiIntent.putExtra("step_count", stepCount)
                sendBroadcast(uiIntent)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Não implementado
    }
}
