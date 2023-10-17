package com.dicoding.mygeofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes

class GeofenceBroadcastRecevier : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

            if (geofencingEvent.hasError()) {
                val errorMessage =
                    GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            val geofenceTransition = geofencingEvent.geofenceTransition

            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                val geoFenceTransitionString =
                    when (geofenceTransition) {
                        Geofence.GEOFENCE_TRANSITION_ENTER -> "Anda telah memasuki Area"
                        Geofence.GEOFENCE_TRANSITION_DWELL -> "Anda Berada dalam Area"
                        else -> "Invalid transition type"
                    }

                val triggerGeofences = geofencingEvent.triggeringGeofences
                triggerGeofences?.forEach { geofence ->
                    val geofenceTransitionDetails =
                        "$geoFenceTransitionString ${geofence.requestId}"
                    Log.i(TAG, geofenceTransitionDetails)
                    sendNotification(context, geofenceTransitionDetails)
                }
            } else {
                val errorMessage = "Invalid transition type : $geofenceTransition"
                Log.e(TAG, errorMessage)
                sendNotification(context, errorMessage)
            }
        }
    }

    private fun sendNotification(context: Context, geofenceTransitionDetails: String) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(geofenceTransitionDetails)
            .setContentText("Anda sudah bisa absen skrg")
            .setSmallIcon(R.drawable.ic_notif)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()

        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "GeofenceBroadcast"
        const val ACTION_GEOFENCE_EVENT = "GeofenceEvent"
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Geofence Channel"
        private const val NOTIFICATION_ID = 1
    }
}