# SunoCare development log

## CP0 — Repository audit and baseline

- Owner and review. Codex led the audit. A separate Codex reviewer independently checked the inventory and the proposed first CP1 slice.
- Branch. `docs/cp0-baseline`. Audited revision `60976a0` (`Instructions`), preceded by `11e2536` (`Initial commit`).
- Scope. Audited all tracked files, local refs, and tool availability. Recorded the planned architecture and the first implementation slice in [architecture.md](architecture.md).
- Checkpoint commit. `1f495be` (`docs(project): document SunoCare baseline and architecture`).
- Status. CP0 evidence is recorded below. The first implementation slice follows the existing CP1 requirements.

| Check | Result |
| --- | --- |
| `git status --short --branch` and `git status --short --ignored` | Started on clean `main` tracking `origin/main`, with no ignored working files reported. |
| `git ls-files`, `git log -12 --oneline`, and `git branch -a` | Three tracked Markdown files, two commits, and no other project branches in the checkout. |
| `git diff --check` | Passed on the audited checkout. |
| `python --version` | Python 3.13.5 available. |
| `docker --version` and `docker compose version` | Docker client 29.7.2 and Docker Compose v5.5.0 available. |
| `docker info --format '{{.ServerVersion}}'` | The sandboxed check was denied access to the engine pipe. The approved retry reported that Docker Desktop was unable to start. |
| Android SDK directory inventory and `platform-tools/adb.exe version` | Android API 36 and Build Tools 36.0.0 present under the local Android SDK. ADB 37.0.1 runs from that SDK. |
| `Get-Command java,gradle,adb` and inspection of Android and Java environment variables | None of these executables were on PATH. `JAVA_HOME`, `ANDROID_HOME`, and `ANDROID_SDK_ROOT` were unset. |

There were no application tests or builds to run against the baseline. No product flow, simulator, or external integration was exercised. CP1 needs a compatible JDK, a checked-in Gradle wrapper, and an emulator or connected Android device. CP2 needs a working Docker engine for its Compose acceptance check. Wispr credentials and physical-device behavior will be verified at their respective checkpoints.

## CP1 — Native Android foundation and role navigation

- Scope. One Kotlin and Compose APK with Patient and Caregiver routes, accessible headings, large navigation targets, light and dark themes, and a labelled sample request. Patient Mode displays its monitoring-off state. Caregiver Mode opens the sample request in a detail view.
- Owner and review. Codex implemented the build and welcome navigation. The delegated Android agent contributed the Patient, Caregiver, and request-detail screens. The lead reviewed and integrated the role screens.
- Build choices. JDK 17, Gradle 8.13, compile and target SDK 36, and minimum SDK 26. The wrapper JAR matched the published Gradle SHA-256, and the wrapper verified the distribution SHA-256 before running.
- Branch and commit. `feat/android-app-shell`, `feat(android): add patient and caregiver role navigation`. To retrieve its SHA, run `git log -1 --format=%h --grep="^feat(android): add patient and caregiver role navigation$"`.

| Check | Result |
| --- | --- |
| `gradlew.bat --no-daemon :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug :app:testDebugUnitTest` | Both APKs assembled. Lint completed with six warnings covering newer dependencies, Android 12+ data extraction rules, and a missing launcher icon. The JVM unit-test task reported `NO-SOURCE`. |
| `aapt dump permissions android/app/build/outputs/apk/debug/app-debug.apk` | The merged APK declares AndroidX's private receiver permission. It requests no microphone or internet permissions. |
| `gradlew.bat --no-daemon :app:connectedDebugAndroidTest` | Both navigation tests passed on the SunoCare API 36 x86_64 emulator running Android 16. The first emulator boot exceeded the initial waiting window. A retry after boot ran both tests. |

The water request and patient are synthetic. The app has no account, microphone capture, backend, Wispr integration, or notification routing. Physical Android hardware and external credentials were outside this checkpoint's verification.
