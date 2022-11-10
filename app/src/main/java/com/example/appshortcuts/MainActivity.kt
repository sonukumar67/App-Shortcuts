package com.example.appshortcuts

import android.app.PendingIntent
import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.appshortcuts.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Check's that the api version meets the requirements
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //Sets up our shortcuts
            Shortcuts.setUp(applicationContext)
        }

        //Check's that the api version meets the requirements
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Takes care of activating the pinned shortcuts

            binding.btnYoutubeSc.setOnClickListener {
                shortcutPin(applicationContext, shortcut_website_id, 0)
            }

            binding.btnMessagesSc.setOnClickListener {
                shortcutPin(applicationContext, shortcut_messages_id, 1)
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun shortcutPin(context: Context, shortcut_id: String, requestCode: Int) {

        val shortcutManager = getSystemService(ShortcutManager::class.java)

        if (shortcutManager!!.isRequestPinShortcutSupported) {
            val pinShortcutInfo =
                ShortcutInfo.Builder(context, shortcut_id).build()

            val pinnedShortcutCallbackIntent =
                shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            val successCallback = PendingIntent.getBroadcast(
                context, /* request code */
                requestCode,
                pinnedShortcutCallbackIntent, /* flags */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_ONE_SHOT
            )

            shortcutManager.requestPinShortcut(
                pinShortcutInfo,
                successCallback.intentSender
            )
        }
    }
}