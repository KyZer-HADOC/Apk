package com.kyzer.ytdl

import android.content.Context

data class Settings(
    val format: String = "MP4",      // MP4 | MP3
    val videoHeight: Int = 0,        // 0 = best available
    val mp3Kbps: Int = 256,
    val saveMode: Int = 0,           // 0 = Movies/Music, 1 = Downloads, 2 = chosen folder
    val customUri: String = "",
    val customName: String = ""
)

object SettingsStore {
    private fun prefs(ctx: Context) = ctx.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun load(ctx: Context): Settings {
        val p = prefs(ctx)
        return Settings(
            p.getString("format", "MP4") ?: "MP4",
            p.getInt("videoHeight", 0),
            p.getInt("mp3Kbps", 256),
            p.getInt("saveMode", 0),
            p.getString("customUri", "") ?: "",
            p.getString("customName", "") ?: ""
        )
    }

    fun save(ctx: Context, s: Settings) {
        prefs(ctx).edit()
            .putString("format", s.format).putInt("videoHeight", s.videoHeight)
            .putInt("mp3Kbps", s.mp3Kbps).putInt("saveMode", s.saveMode)
            .putString("customUri", s.customUri).putString("customName", s.customName)
            .apply()
    }
}
