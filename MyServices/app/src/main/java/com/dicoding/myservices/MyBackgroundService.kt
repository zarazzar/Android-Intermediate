package com.dicoding.myservices

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBackgroundService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not Yet Implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service Dijalankan...")
        serviceScope.launch {
            for (i in 1..50){
                delay(1000)
                Log.d(TAG, "Do Something $i")
            }
            stopSelf()
            Log.d(TAG, "Service Stopped")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service Dihentikan")
    }

    companion object {
        internal val TAG = MyBackgroundService::class.java.simpleName
    }
}