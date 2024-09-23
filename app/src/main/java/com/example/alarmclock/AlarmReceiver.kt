package com.example.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager

class AlarmReceiver : BroadcastReceiver() {
    var ringtone: Ringtone? = null
    override fun onReceive(context: Context?, p1: Intent?) {
        var notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, notificationUri)
        if (ringtone == null) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, notificationUri)
        }
        if (ringtone != null) {
            ringtone?.play()
        }
        val intent = Intent(context!!.applicationContext, SecondActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}