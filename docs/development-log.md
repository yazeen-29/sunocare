# SunoCare development log

## CP0 — Repository audit and baseline

- Owner and review. Codex led the audit. A separate Codex reviewer independently checked the inventory and the proposed first CP1 slice.
- Branch. `docs/cp0-baseline`. Audited revision `60976a0` (`Instructions`), preceded by `11e2536` (`Initial commit`).
- Scope. Audited all tracked files, local refs, and tool availability. Recorded the planned architecture and the first implementation slice in [architecture.md](architecture.md).
- Checkpoint commit. `docs(project): document SunoCare baseline and architecture`. Get its SHA with `git log -1 --format=%h -- docs/architecture.md`.
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
