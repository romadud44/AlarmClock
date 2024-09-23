package com.example.alarmclock

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alarmclock.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var calendar: Calendar? = null
    private var materialTimePicker: MaterialTimePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.toolbarMain.title = "Мой будильник"
        binding.toolbarMain.subtitle = "версия 1.0"
        setSupportActionBar(binding.toolbarMain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.setAlarmBTN.setOnClickListener {
            materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Выберите время будильника")
                .build()
            materialTimePicker?.addOnPositiveButtonClickListener {
                calendar = Calendar.getInstance()
                calendar?.set(Calendar.SECOND, 0)
                calendar?.set(Calendar.MILLISECOND, 0)
                materialTimePicker?.hour?.let { it1 -> calendar?.set(Calendar.HOUR_OF_DAY, it1) }
                materialTimePicker?.minute?.let { it1 -> calendar?.set(Calendar.MINUTE, it1) }
                binding.alarmTimeTV.text = dateFormat.format(calendar?.time)
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                calendar?.timeInMillis?.let { it1 ->
                    getAlarmPendingIntent()?.let { it2 ->
                        alarmManager.setExact(
                            RTC_WAKEUP,
                            it1,
                            it2
                        )
                        Toast.makeText(this, "Будильник установлен на ${dateFormat.format(calendar?.time)}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            materialTimePicker?.show(supportFragmentManager, "tag_pick")
        }
    }

    private fun getAlarmPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return  PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> {
                finishAndRemoveTask()
                finishAffinity()
                finish()
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}