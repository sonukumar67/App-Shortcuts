package com.example.data

import android.content.Intent
import androidx.core.graphics.drawable.IconCompat

data class AppShortcutInfo (
    val label: String,
    val longLabel: String,
    val id: String,
    val icon: IconCompat,
    val intent: Intent
)