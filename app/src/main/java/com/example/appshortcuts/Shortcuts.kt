package com.example.appshortcuts

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.*
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import com.example.data.AppShortcutInfo

const val shortcut_website_id = "id_website"
const val shortcut_messages_id = "id_messages"
const val shortcut_settings_id = "id_settings"
const val shortcut_call_id = "id_call"

//Requires api level 25
@RequiresApi(Build.VERSION_CODES.N_MR1)
object Shortcuts {

    fun setUpSingleShortcut(context: Context, appShortcutInfo: AppShortcutInfo) {
        val shortcut = ShortcutInfoCompat.Builder(context, appShortcutInfo.id)
            .setShortLabel(appShortcutInfo.label)
            .setLongLabel(appShortcutInfo.longLabel)
            .setIcon(appShortcutInfo.icon)
            .setIntent(appShortcutInfo.intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    fun setupMultipleShortcuts(context: Context, list: List<AppShortcutInfo>) {
        val shortcutManager = getSystemService(context, ShortcutManager::class.java)
        val listOfShortcutInfo = ArrayList<ShortcutInfo>()
        for (appShortcutInfo in list) {
            val shortcutInfo = ShortcutInfo.Builder(context, appShortcutInfo.id)
                .setShortLabel(appShortcutInfo.label)
                .setLongLabel(appShortcutInfo.longLabel)
                .setIcon(appShortcutInfo.icon.toIcon())
                .setIntent(appShortcutInfo.intent)
                .build()
            listOfShortcutInfo.add(shortcutInfo)
        }
        shortcutManager!!.dynamicShortcuts = listOfShortcutInfo
    }
}