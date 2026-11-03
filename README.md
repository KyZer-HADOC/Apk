# KyZer YouBe — Android

Dark-themed Android downloader by **KyZer_HADOC**.

## Android v2.2.0 beta

- YouTube video and MP3 downloads with quality controls, progress, pause/resume/cancel and history.
- **Other DL:** yt-dlp-supported sites with MP4/MP3 and quality selection. Some sites may block the free extractor runtime.
- **WhatsApp Status:** browse and save viewed WhatsApp status images/videos after granting access to the WhatsApp Media folder.
- Saved statuses are copied to Pictures/KyZer YouBe/WhatsApp Statuses or Movies/KyZer YouBe/WhatsApp Statuses.

### Connect WhatsApp Status

1. Open **WA Status** in the app and tap **Connect WhatsApp**.
2. In Android's folder picker, open Android/media/com.whatsapp/WhatsApp/Media and select the **Media** folder. For WhatsApp Business, use Android/media/com.whatsapp.w4b/WhatsApp Business/Media.
3. Open/view a status in WhatsApp first, return to KyZer YouBe and tap **Refresh**.
4. Tap **Save** beside an image or video.

Only statuses already cached by WhatsApp are available. Expired or not-yet-viewed statuses cannot be fetched. Android's file picker may hide the .Statuses directory; select the parent **Media** folder so the app can look inside it.

## APK size and compatibility

This beta APK is built for **arm64-v8a only**, avoiding the unused x86_64 native libraries. It is intended for modern 64-bit Android phones, including Samsung Galaxy A05s. It will not install on 32-bit-only or x86 devices. The embedded yt-dlp/FFmpeg native runtimes still make it larger than the original YouTube-only build.

## Download and install

Open the GitHub Releases page and download app-arm64-v8a-debug.apk from **KyZer YouBe Android v2.2.0 Beta**.

This is a debug-signed beta. If Android reports a signature conflict, uninstall the previous differently signed build first; this can erase local history/settings.

## Build

Requirements: JDK 17, Gradle 8.9, Android SDK 35.

    gradle testDebugUnitTest lintDebug assembleDebug

The arm64 APK is generated at app/build/outputs/apk/debug/app-arm64-v8a-debug.apk.

## Known limitations

- The free yt-dlp runtime lacks TLS fingerprint impersonation; TikTok/Facebook/Instagram can reject requests.
- Download Later, queue management, clip trimming and subtitle downloads are intentionally omitted.
- WhatsApp Status access depends on WhatsApp having cached the status and Android granting folder permission.
- Physical-device testing is still required.

Only save content you own or have permission to download. Respect copyright and platform terms.