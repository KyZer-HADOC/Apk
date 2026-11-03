package com.kyzer.ytdl

import android.content.Context
import dev.ffmpegkit_maintained.ytdlp.YtDlp
import dev.ffmpegkit_maintained.ytdlp.YtDlpRequest
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutionException

/**
 * Embedded yt-dlp runtime for the Android multi-site download path.
 * Run download() from a worker thread, not the main/UI thread.
 */
object YtDlpRuntime {
    @Volatile
    private var initialized = false

    @Synchronized
    fun init(context: Context) {
        if (initialized) return
        try {
            YtDlp.init(context.applicationContext)
            initialized = true
        } catch (t: Throwable) {
            throw IOException("Could not start embedded yt-dlp: ${t.message}", t)
        }
    }

    fun download(
        context: Context,
        url: String,
        outputTemplate: String,
        formatSelector: String = "best",
        onProgress: (Float, Long, String) -> Unit
    ): File {
        if (!initialized) init(context)
        val request = YtDlpRequest(url)
            .setOutputTemplate(outputTemplate)
            .addOption("-f", formatSelector)
            .addOption("--no-playlist")
            .addOption("--newline")
            .addOption("--no-warnings")

        try {
            val response = YtDlp.executeAsync(request) { progress, eta, line ->
                onProgress(progress.coerceIn(0f, 100f), eta, line)
            }.get()
            if (!response.isSuccess) {
                throw IOException("yt-dlp exited with code ${response.exitCode}")
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IOException("Download interrupted", e)
        } catch (e: ExecutionException) {
            val cause = e.cause ?: e
            throw IOException(cause.message ?: "yt-dlp download failed", cause)
        }

        val template = File(outputTemplate)
        val result = template.takeIf { it.isFile && it.length() > 0L }
            ?: template.parentFile?.listFiles()
                ?.filter { it.isFile && it.length() > 0L }
                ?.maxByOrNull { it.lastModified() }

        return result ?: throw IOException("yt-dlp finished but no output file was created")
    }
}
