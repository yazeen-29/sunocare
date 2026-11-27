# SunoCare

Voice-first bedside care and caregiver coordination.

The Android demo lets you open Patient Mode, check the monitoring-off state, and inspect a sample water request in Caregiver Mode.

## Android demo

Use JDK 17 and Android SDK Platform 36 with Build Tools 36.0.0. Set `JAVA_HOME` and `ANDROID_HOME`, or configure the SDK location through Android Studio. Run these commands from the repository root in PowerShell.

```powershell
Set-Location android
.\gradlew.bat :app:assembleDebug :app:lintDebug
```

On macOS or Linux, use `sh ./gradlew` in the `android` directory.

To install the demo and run the navigation tests, start an Android emulator or connect a device with USB debugging enabled. Run this command from the `android` directory in PowerShell.

```powershell
.\gradlew.bat :app:installDebug :app:connectedDebugAndroidTest
```

The request and patient in this demo are synthetic. The walkthrough needs no account, microphone permission, or service credentials.

## Project documents

- [Architecture and first implementation slice](docs/architecture.md)
- [Development and validation log](docs/development-log.md)
- [Master specification](instructions.md)
