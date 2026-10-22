# Metro Launcher

A native Android launcher prototype built with Kotlin and Jetpack Compose. It combines a typographic home screen with a searchable app drawer and a small set of local tools.

## Included

- Android `HOME` activity integration; choose **Metro Launcher** from Android's Home app settings to use it as the default launcher.
- Metro-inspired live-tile home screen with a device clock, app count, app drawer, and quick system-app intents.
- Searchable list of apps that expose Android's standard launcher intent.
- A local notes editor saved on-device with Preferences DataStore.
- A basic calculator and a playable 2048 game with swipe and button controls plus a saved best score.
- A forecast sample screen. It does not query a weather service.
- GitHub Actions build and test workflow that uploads an installable debug APK.

## Android boundaries

This project uses standard Android launcher APIs. It does not replace the system's task switcher or quick-settings panel and does not install an Accessibility Service or a cross-app gesture overlay. Android owns those system surfaces and navigation gestures.

Phone, SMS, calendar, camera, browser, and settings shortcuts ask Android to open the corresponding installed app. The launcher does not read contacts, call logs, SMS, or other apps' private data. Apps hidden by Android's package-visibility rules or work-profile policy may not appear in the drawer.

## Build from GitHub Actions

1. Push the repository to GitHub, keeping both the `android-launcher/` folder and `.github/workflows/build-apk.yml` at the repository root.
2. Open **Actions** and run **Build Android APK**, or push a change to `android-launcher/` on any branch. Pull requests that change the launcher also run the build.
3. Open the completed run and download the `metro-launcher-debug-apk` artifact.
4. Transfer `app-debug.apk` to the phone and install it. Android may ask you to allow installation from that source.
5. Select Metro Launcher when Android asks for a Home app, or choose it in the device's default Home-app settings.

The workflow creates an unsigned debug APK, which is suitable for local installation and testing. It does not create a Play Store release or handle signing credentials.

## Local build

Requires JDK 17 and Android SDK platform 35:

```sh
cd android-launcher
./gradlew testDebugUnitTest assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.
