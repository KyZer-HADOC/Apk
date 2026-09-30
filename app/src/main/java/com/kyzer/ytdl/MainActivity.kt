package com.kyzer.ytdl

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.util.concurrent.Executors

class MainActivity : Activity() {

    private data class Option(val label: String, val url: String, val fileName: String)

    private val io = Executors.newSingleThreadExecutor()
    private var options = listOf<Option>()

    private lateinit var input: EditText
    private lateinit var status: TextView
    private lateinit var list: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewPipe.init(YtDownloader())

        val pad = (16 * resources.displayMetrics.density).toInt()
        val match = LinearLayout.LayoutParams.MATCH_PARENT
        val wrap = LinearLayout.LayoutParams.WRAP_CONTENT

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad * 2, pad, pad)
        }
        input = EditText(this).apply {
            hint = "Paste YouTube link"
            setSingleLine()
        }
        val fetch = Button(this).apply {
            text = "Get download options"
            setOnClickListener { fetchInfo() }
        }
        status = TextView(this).apply { setPadding(0, pad / 2, 0, pad / 2) }
        list = ListView(this)
        list.setOnItemClickListener { _, _, pos, _ -> download(options[pos]) }

        root.addView(input, LinearLayout.LayoutParams(match, wrap))
        root.addView(fetch, LinearLayout.LayoutParams(match, wrap))
        root.addView(status, LinearLayout.LayoutParams(match, wrap))
        root.addView(list, LinearLayout.LayoutParams(match, 0, 1f))
        setContentView(root)

        // Opened via "Share" from the YouTube app
        if (intent?.action == Intent.ACTION_SEND) {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                input.setText(it)
                fetchInfo()
            }
        }
    }

    private fun fetchInfo() {
        val url = Regex("https?://\\S+").find(input.text.toString())?.value
        if (url == null) {
            status.text = "Please paste a valid link."
            return
        }
        status.text = "Loading…"
        list.adapter = null

        io.execute {
            try {
                val info = StreamInfo.getInfo(url)
                val base = info.name.replace(Regex("[\\\\/:*?\"<>|]"), "_").take(80)
                val opts = mutableListOf<Option>()

                info.videoStreams
                    .filter { it.isUrl }
                    .sortedByDescending { it.height }
                    .forEach { s ->
                        val ext = s.format?.suffix ?: "mp4"
                        opts += Option(
                            "Video ${s.resolution} ($ext, with audio)",
                            s.content,
                            "$base-${s.resolution}.$ext"
                        )
                    }

                info.audioStreams
                    .filter { it.isUrl }
                    .sortedByDescending { it.averageBitrate }
                    .take(3)
                    .forEach { s ->
                        val ext = s.format?.suffix ?: "m4a"
                        opts += Option(
                            "Audio only ${s.averageBitrate}kbps ($ext)",
                            s.content,
                            "$base-${s.averageBitrate}kbps.$ext"
                        )
                    }

                runOnUiThread {
                    options = opts
                    status.text = if (opts.isEmpty()) "No downloadable streams found."
                    else "${info.name}\nTap an option to download:"
                    list.adapter = ArrayAdapter(
                        this, android.R.layout.simple_list_item_1, opts.map { it.label }
                    )
                }
            } catch (e: Exception) {
                runOnUiThread { status.text = "Error: ${e.message}" }
            }
        }
    }

    private fun download(o: Option) {
        if (Build.VERSION.SDK_INT < 29 &&
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            status.text = "Allow storage permission, then tap again."
            return
        }
        val req = DownloadManager.Request(Uri.parse(o.url))
            .setTitle(o.fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, o.fileName)
            .addRequestHeader("User-Agent", USER_AGENT)
        (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(req)
        status.text = "Download started → Downloads/${o.fileName}"
    }
}
