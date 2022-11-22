package com.example.appshortcuts

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.IconCompat
import com.example.appshortcuts.databinding.ActivityMainBinding
import com.example.data.AppShortcutInfo
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Check's that the api version meets the requirements
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Takes care of activating the pinned shortcuts
            binding.btnYoutubeSc.setOnClickListener {
                val shortcutInfo = getShortcutInfo(shortcut_website_id)
                shortcutInfo?.let { info -> Shortcuts.setUpSingleShortcut(applicationContext, info) }
                val snack = Snackbar.make(it,"Add YouTube shortcut on Home screen?",Snackbar.LENGTH_LONG)
                snack.setAction("ADD") {
                    shortcutPin(applicationContext, shortcut_website_id, 0)
                }
                snack.show()
            }

            binding.btnMultiple.setOnClickListener {
                val shortcutInfo1 = getShortcutInfo(shortcut_settings_id)
                val shortcutInfo2 = getShortcutInfo(shortcut_messages_id)
                shortcutInfo1?.let { info1->
                    shortcutInfo2?.let { info2->
                        Snackbar.make(it,"Adding Settings and Messages as app shortcuts",
                            Snackbar.LENGTH_LONG).show()
                        Shortcuts.setupMultipleShortcuts(applicationContext, listOf(info1, info2))
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getShortcutInfo(id: String): AppShortcutInfo? {
        return when (id) {
            shortcut_website_id -> {
                AppShortcutInfo(
                    label = "Youtube",
                    longLabel = "Open Youtube",
                    id = id,
                    icon = IconCompat.createWithResource(applicationContext, R.drawable.ic_website),
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/")
                    )
                )
            }

            shortcut_messages_id -> {
                AppShortcutInfo(
                    label = "Messages",
                    longLabel = "Open messages",
                    id = id,
                    icon = IconCompat.createWithResource(applicationContext, R.drawable.ic_messages),
                    intent = Intent(Intent.ACTION_VIEW,
                        null,
                        applicationContext,
                        Messages::class.java)
                )
            }

            shortcut_settings_id -> {
                AppShortcutInfo(
                    label = "Settings",
                    longLabel = "Open Settings",
                    id = id,
                    icon = IconCompat.createWithResource(applicationContext,
                        android.R.drawable.arrow_down_float),
                    intent = Intent(Settings.ACTION_SETTINGS)
                )
            }

            else -> null
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