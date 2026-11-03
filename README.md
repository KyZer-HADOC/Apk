# KyZer YouBe — Android

A dark-themed Android video and audio downloader UI by **KyZer_HADOC**.

> **Preview status:** This Android build currently provides the YouTube-focused download flow described below. The PC application's complete feature set is **not yet available on Android**. In particular, TikTok, Facebook, generic Other DL, WhatsApp Status, Download Later, queue management, clip trimming and subtitle downloads are not ready in this APK. The yt-dlp multi-site runtime is intentionally excluded from this preview until it is fully connected to the Android UI; this avoids shipping a large unused dependency.

## Download

1. Open the repository's **Releases** page.
2. Download `app-debug.apk` from **KyZer YouBe Android v2.0.1 Preview**.
3. Open the APK on your Android phone and allow installation from that source if Android asks.\n\nDirect APK: [Download app-debug.apk](https://github.com/KyZer-HADOC/Apk/releases/download/v2.0.1-preview/app-debug.apk).

The preview is debug-signed. If Android says the app cannot be installed because of a signature conflict, uninstall the previous build first, then install this APK. Uninstalling may erase the old app's local history and settings.

## Current Android features

- Paste a YouTube URL or use **Share → KyZer YouBe** from another app.
- Load video title, channel, thumbnail and available stream qualities.
- Download MP4 video at available resolutions.
- Download and convert audio to MP3 (64–320 kbps choices).
- Show download progress, transferred size and speed.
- Pause, resume and cancel downloads during the download stage.
- Save to Movies/Music, Downloads, or a folder selected through Android's folder picker.
- Keep a local download history and open or remove history entries.
- Dark interface with app settings for default format, quality and save location.
- Save a crash report locally so the user can copy it for troubleshooting.

## Requirements

- Android 8.0 (API 26) or newer.
- Internet connection.
- ARM64 devices are recommended. The embedded yt-dlp dependency currently supports arm64-v8a and x86_64 only; broader ABI support has not been verified.
- Storage permission is requested only on older Android versions that need it. Newer Android versions use MediaStore or the Android folder picker.

## Build from source

Requirements:
- JDK 17
- Gradle 8.9 (or compatible)
- Android SDK with compile SDK 35

Build a debug APK:

```bash
gradle assembleDebug
```

Run the verification tasks:

```bash
gradle testDebugUnitTest lintDebug assembleDebug
```

The APK is written to:

```
app/build/outputs/apk/debug/app-debug.apk
```

GitHub Actions runs unit-test, lint and APK build tasks on pushes to `main` and `feature/pc-parity`. Successful builds are also uploaded as workflow artifacts.

## Troubleshooting

- **Link not supported:** The current Android UI uses NewPipe Extractor and is primarily YouTube-focused. Other PC-supported platforms are not yet enabled in Android.
- **No quality appears:** Try another video URL or retry later; stream availability can vary by video and region.
- **Download stops or fails:** Check the internet connection and available storage, then retry.
- **Cannot save to selected folder:** Re-select the folder in Settings to grant Android's folder permission again.
- **APK signature conflict:** Remove the previous differently signed installation before installing this preview.
- **App crash:** Use the crash card shown on the next launch and copy the report when submitting a bug report.

## Known limitations

This preview is not feature-equivalent to the Windows PC edition. The following are not yet implemented in the Android UI:

- TikTok and Facebook extraction.
- Generic URL / Other DL support.
- WhatsApp Status media access.
- Download Later and multi-item queue.
- Start/end clip trimming.
- Subtitle selection and download.
- Full in-app video playback preview.

Pause/resume is supported while network streams are downloading; conversion/merging stages are not pausable.

## Legal

Only download content that you own or have permission to save. Respect the terms of service and copyright laws applicable to each platform. Platform access and extraction behavior may change without notice.
