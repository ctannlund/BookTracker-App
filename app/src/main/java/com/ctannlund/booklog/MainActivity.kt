package com.ctannlund.booklog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.navigation.fragment.NavHostFragment

const val prefKey = "com.ctannlund.booktrackerapp.PREF_FILE_KEY"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(androidx.navigation.fragment.R.id.nav_host_fragment_container)
                as NavHostFragment

        val navController = navHostFragment.navController

        // notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("booktracker_id", "BookTrackerReminders", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "BookTracker Notification Channel"
            val manager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            manager!!.createNotificationChannel(channel)
        }

        // sharedpref
        val sharedPref = getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        sharedPref.getBoolean("notifs", true)

        // app nav buttons

        val homeButton = findViewById<ImageButton>(R.id.booklist_nav_button)
        val goalButton = findViewById<ImageButton>(R.id.stats_nav_button)
        val logButton = findViewById<ImageButton>(R.id.logs_nav_button)
        val shelfButton = findViewById<ImageButton>(R.id.bookshelf_nav_button)
        val settingsButton = findViewById<ImageButton>(R.id.settings_nav_button)
        val buttonArray = arrayOf(homeButton, goalButton, logButton, shelfButton, settingsButton)

        homeButton.setOnClickListener {
            navController.navigate(R.id.bookListFragment)
            buttonBackgroundSelect(homeButton, buttonArray)
        }

        goalButton.setOnClickListener {
            navController.navigate(R.id.bookGoalStatsFragment)
            buttonBackgroundSelect(goalButton, buttonArray)
        }

        logButton.setOnClickListener {
            navController.navigate(R.id.bookLogGraphFragment)
            buttonBackgroundSelect(logButton, buttonArray)
        }

        shelfButton.setOnClickListener {
            navController.navigate(R.id.bookshelfFragment)
            buttonBackgroundSelect(shelfButton, buttonArray)
        }


        settingsButton.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
            buttonBackgroundSelect(settingsButton, buttonArray)
        }

    }

    private fun buttonBackgroundSelect(button: ImageButton, buttonArray: Array<ImageButton>) {
        val defaultColor = "#F4F1E0"
        val clickColor = "#9F7833"
        val clickedBackground = R.drawable.navbar_button_bg
        for (btn in buttonArray) {
            if (btn == button) {
                btn.setColorFilter(Color.parseColor(clickColor))
                btn.setBackgroundResource(clickedBackground)
            }
            else {
                btn.setColorFilter(Color.parseColor(defaultColor))
                btn.setBackgroundResource(0)
            }
        }

    }

}