# SunoCare architecture

## Repository baseline

The audited revision is `60976a0`. The repository contains the master specification, contributor instructions, and a short README. Android source, backend source, database migrations, build definitions, dependency manifests, tests, and CI are all pending. There are no live integrations or simulated product flows at this revision.

## Planned system

| Component | Responsibilities |
| --- | --- |
| Native Android app | One Kotlin and Compose application with Patient and Caregiver navigation. Owns device permissions, explicit microphone activation, local audio segmentation, and accessible status and controls. |
| FastAPI backend | Owns authentication, patient and caregiver authorization, pairing, event classification and persistence, notification routing, and monitoring health. |
| PostgreSQL | Persists care records, consent, event history, delivery attempts, and acknowledgements. Alembic manages schema changes. |
| Transcription providers | A shared Android provider boundary supports Wispr Flow, verified on-device recognition, and clearly labelled demo input. The backend issues short-lived client credentials when Wispr access is configured and keeps the organisation key server-side. |
| Notifications | The backend records delivery attempts and escalation. Firebase Cloud Messaging is an optional transport. Event creation, delivery status, and caregiver acknowledgement have distinct records. |

The central workflow begins with a patient voice request. The Android app obtains text through the active transcription provider. The backend verifies patient access, evaluates configured urgent phrases using deterministic rules, persists the event, and routes it to authorised caregivers. The caregiver's acknowledgement and resolution update the event history. Patient feedback uses the delivery and acknowledgement evidence available to the system.

Night Watch requires explicit activation and a visible Android microphone foreground-service notification. Audio stays in a short volatile buffer by default. Cloud transcription receives necessary speech segments after consent. Monitoring status reflects the service, permission, and provider evidence available to the app and backend.

## First implementation slice

CP1 starts with an accessible welcome screen and two role destinations in one Android APK. Each destination shows labelled synthetic content and supports returning to the welcome screen. Patient Mode starts with monitoring off. Caregiver Mode presents a synthetic request that can be opened for inspection. The acceptance check is a successful Gradle debug build and navigation through both roles on an emulator or device. Subsequent checkpoints add backend persistence, pairing, and the real voice-to-caregiver workflow.
