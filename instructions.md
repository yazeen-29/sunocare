# SunoCare — Master Product, Engineering and Codex Execution Specification

**Project:** SunoCare  
**Purpose:** Single source of truth for product requirements, engineering decisions, phased implementation, testing, Git workflow and Codex orchestration.  
**Audience:** Codex, the repository owner, and all human contributors.

---

## How to use this specification

1. Start Codex from the repository root (or a directory below it). Codex must first read the root `AGENTS.md`; that file directs it to read this entire `instructions.md` before changing code.
2. Read this document in full, not only the section related to the immediate task. Requirements concerning privacy, emergency handling, Android limitations, Wispr, testing, commits and team collaboration apply across the project.
3. Inspect the actual repository, active branch, working tree, existing modules, build scripts, tests and dependency versions before deciding what to implement. This document describes the intended product; it does not prove any feature already exists.
4. Preserve useful working implementation. Adapt the architecture to the repository where sensible; explain material deviations rather than rewriting everything by default.
5. Implement in small, testable increments. Do not stop after producing a plan, a UI mockup, generated scaffolding or nonfunctional placeholder screens.
6. Use current official Android, Wispr, Firebase and library documentation for changeable API behavior. Historical snippets in this file are requirements/context, not a substitute for checking the current vendor contract.
7. Be exact about implementation status. Separate live functionality, deterministic simulation/demo functionality, unavailable integrations and items requiring physical-device verification.

### Precedence when requirements conflict

Apply this priority order:

1. Patient/user safety, consent and privacy.
2. Actual Android platform restrictions and current official vendor/API documentation.
3. Requirements in this master specification.
4. Existing repository conventions and maintainability.
5. Convenience or implementation speed.

Do not silently discard a requirement. Explain the conflict and choose the safest compliant implementation.

### Critical vendor-documentation note

The detailed Wispr Flow endpoint, authentication and audio protocol information in the supplied master prompt is retained below because it is part of the project's requested design. Before implementing or claiming live integration, verify every endpoint, message field, authentication step, encoding and limit against the current official Wispr documentation linked in Part 3. If the current docs differ, follow the current docs and document the difference. Never hard-code an unverified historical endpoint or invent an API flow.

---

# Part A — Development governance and execution plan

## A1. Development checkpoints and exit criteria

Work through the checkpoints below. A checkpoint is complete only when its acceptance criteria are met and evidence is recorded. A folder, screen, TODO, stub endpoint or passing compilation alone does not make a product capability complete.

| Checkpoint | Scope | Minimum exit evidence |
|---|---|---|
| **CP0 — Repository audit and baseline** | Inspect branches, Git status, app/backend structure, current implementation, tooling, dependencies, tests and architecture. Preserve useful work. Write `docs/architecture.md` and `docs/development-log.md`. | Clear inventory of implemented vs missing vs simulated capabilities; reproducible baseline commands and actual results; agreed first vertical slice. |
| **CP1 — Native Android foundation and role navigation** | Kotlin/Compose app shell, design system, Patient/Caregiver entry points, navigation, accessibility foundations and synthetic demo data. | Gradle build succeeds; both role flows can be opened and navigated on emulator/device; demo content is explicitly labelled. |
| **CP2 — Backend and persistence foundation** | FastAPI, PostgreSQL, migrations, schemas, repositories, health endpoint, Docker Compose, development authentication if production auth is not ready. | `docker compose up --build` works from documented clean setup; database persistence and backend tests verified; no secrets required for demo startup. |
| **CP3 — Patient profiles, caregiver access and device pairing** | Caregiver accounts, patient profile, patient-caregiver relationships, primary/backup roles, expiring single-use QR/PIN pairing, device registration and revocation. | Pairing, expiry, reuse rejection, revocation and cross-patient authorization tests pass. |
| **CP4 — Voice capture and transcription-provider boundary** | Permission-aware push-to-talk, native audio path, VAD/segmentation boundary, provider abstraction, demo adapter, Wispr adapter when approved API access exists, language configuration and transcript provenance. | A supported test audio/input flows through the provider boundary; live/demo provider status is explicit; permission, provider-down and malformed-response failures tested. |
| **CP5 — Classification, possible distress and event lifecycle** | Deterministic ordinary/urgent/ambiguous classification, negation handling, patient-specific phrases, event persistence and audit history. | Test set covers ordinary requests, urgent phrases, negative/contradictory phrases, ambiguity and duplicate events; events have valid state transitions. |
| **CP6 — Caregiver alerts, acknowledgement and escalation** | Caregiver inbox, push/local notification adapters, primary/backup routing, timeout escalation, acknowledge, resolve, false alarm and truthful delivery statuses. | Automated tests prove recipient routing, deduplication, timeout behavior, failed/pending delivery and acknowledgement/resolution; UI reflects server state. |
| **CP7 — Night Watch and monitoring-health subsystem** | Consent flow, explicit microphone foreground service, persistent notification, pause/stop, audio pipeline, service/permission/network/provider/battery health, interruption and recovery reporting. | Tests on supported Android versions and physical devices; active/interrupted/paused/stopped states are truthful; no covert boot start or silent restart; limitations documented. |
| **CP8 — Voice assistant, languages, caregiver voice navigation and care history** | TTS, caregiver voice commands, supported language/code-switching, spoken confirmations, daily factual summary, event timeline and authorised PDF export. | Language support and fallbacks tested; voice actions have confirmation where consequential; summaries only report observations; export/privacy controls verified. |
| **CP9 — Reliability, accessibility, security and release hardening** | Offline behavior, service interruptions, security/access controls, retention/deletion, TalkBack, scaling, contrast, CI, docs and reproducible clean setup. | Unit/API/integration/Android checks run and recorded; key failure scenarios exercised; secrets scan clean; clean clone can run documented demo. |
| **CP10 — Competition demo and submission package** | Two-device patient/caregiver demo, ordinary request and separate urgent phrase scenario, pitch PDF, demo video, README and submission verification. | Reproducible truthful demo; simulated parts labelled; demo no longer than the current competition limit; current event portal requirements checked and recorded. |

### Checkpoint execution rules

- A checkpoint may contain multiple commits where each commit is a coherent, independently reviewable slice.
- A large feature should be developed through thin vertical slices, not one huge unreviewable change.
- Do not start a dependent checkpoint before its prerequisites are sufficiently stable. Parallel work may proceed only on independent modules with agreed interfaces.
- Do not claim physical-device behavior based only on emulator results.
- Do not claim live Wispr behavior when credentials/API approval were unavailable.
- Record blockers and safely defer work that depends on approvals, physical devices or external services.

## A2. Mandatory meaningful Git commits at every accepted checkpoint

**The commit history must show meaningful engineering progress throughout development. Do not implement everything and push it in one giant final commit.**

### Commit timing

1. Inspect current `git status` and branch before work.
2. Work on a focused feature/fix/docs/test branch. Do not develop directly on `main` when the team workflow protects it.
3. After a checkpoint's exit criteria are met, create one or more focused commits representing the verified work.
4. A commit should include the implementation and the relevant tests/documentation/configuration needed to understand and validate it.
5. Do not commit broken work merely to meet a commit count. For a legitimate intermediate scaffold, use an honest message such as `feat(pairing): scaffold pairing flow` and clearly label it incomplete in the development log/PR.
6. Never create empty commits, meaningless commits or commits whose only purpose is to inflate the contribution graph.

### Commit message convention

Use Conventional Commit-style messages:

`type(scope): concise description`

Good examples:

- `docs(project): document architecture and team workflow`
- `feat(android): add patient and caregiver role navigation`
- `feat(api): add patient profile endpoints`
- `feat(pairing): add expiring single-use device pairing`
- `feat(voice): add transcription provider abstraction`
- `feat(wispr): implement documented websocket adapter`
- `feat(events): classify and persist patient requests`
- `feat(alerts): add caregiver acknowledgement and escalation`
- `feat(android): add explicit Night Watch foreground service`
- `test(safety): cover emergency phrases and negation`
- `fix(pairing): reject expired and revoked device codes`
- `chore(ci): add Android and backend verification`

Avoid vague messages such as `update`, `changes`, `final`, `done`, `fixes`, `work` or `misc`.

### Pre-commit checklist

Before each commit:

- Run `git status --short` and confirm only intended files are changed.
- Run the relevant tests, build, lint or static checks for the slice.
- Review `git diff` and `git diff --cached`.
- Check that `.env`, API keys, signing material, Firebase service-account files, patient details, recordings, generated build output and local databases are not staged.
- Check for accidental unrelated refactors, generated files, debug bypasses and TODO-only implementations.
- Confirm the commit message describes what changed, not merely that files changed.

After each commit:

- Verify the commit exists and inspect `git show --stat --oneline HEAD`.
- Confirm the working tree is in the expected state.
- Record checkpoint ID, commit SHA, commit message, changed scope, checks run and results in `docs/development-log.md` or the PR.
- Push the feature branch and open a PR when ready for team review. Do not force-push shared branches or rewrite a teammate's published commits without explicit coordination.
- The lead integrator normally creates the coherent checkpoint commit after reviewing integrated work. An agent may commit only when it owns an isolated branch/worktree and that ownership was explicitly assigned.

### Example branch naming

- `feat/android-app-shell`
- `feat/patient-caregiver-roles`
- `feat/backend-foundation`
- `feat/patient-pairing`
- `feat/voice-provider`
- `feat/wispr-integration`
- `feat/voice-events`
- `feat/caregiver-alerts`
- `feat/night-watch`
- `feat/language-accessibility`
- `chore/release-hardening`

### Required checkpoint log

Maintain `docs/development-log.md` with at least:

| Field | Required record |
|---|---|
| Checkpoint | CP0–CP10 and name |
| Scope | Functionality included and excluded |
| Owner | Human contributor or Codex agent |
| Branch | Branch/worktree used |
| Commits | Full or short SHAs and meaningful messages |
| Validation | Exact commands/tests/builds run and actual results |
| Evidence | Screenshots, demo steps, logs or physical-device observations where appropriate |
| Status | Not started / in progress / blocked / accepted |
| Limitations | Unverified provider, device, language, notification or security behavior |
| Review | Reviewer and unresolved comments |

## A3. Parallel development and sub-agent orchestration

**Use multiple Codex sub-agents when the active Codex environment supports them and when tasks can be separated safely.** Do not pretend agents are available if the current interface/runtime does not support them. When they are unavailable, work sequentially or assign bounded modules to the human teammates.

### Lead agent responsibilities

The lead Codex agent is the technical integrator and remains responsible for the entire product. It must:

- Understand the full specification and current repository before delegation.
- Define architecture, API/data contracts, shared types, event states and module boundaries before parallel work.
- Split work into small tasks with non-overlapping file ownership and explicit acceptance criteria.
- Assign the right agent to each independent module and phase; do not spawn agents simply to increase activity.
- Keep the product safety, privacy and platform constraints consistent across all agents.
- Review all agent output and diffs; do not merge blindly.
- Resolve contract conflicts and integration problems.
- Run combined end-to-end tests after integration.
- Own checkpoint acceptance, final review, development log and coherent commit unless isolated agent branches were explicitly agreed.

### Suggested sub-agent roles

| Agent | Primary responsibility | Typical ownership boundary | Must coordinate with |
|---|---|---|---|
| **Android UX agent** | Compose Patient Mode/Caregiver Mode screens, navigation, design system, accessibility and Android state presentation. | `android/.../feature/*` UI and presentation files agreed for that task. | Lead; voice and backend contracts. |
| **Android voice/Night Watch agent** | AudioRecord/VAD boundary, service lifecycle, permissions, foreground service notification, TTS and device tests. | `android/.../core/voice`, `feature/nightwatch` and related tests where assigned. | Lead; Wispr adapter/backend contract; Android UX. |
| **Backend agent** | FastAPI routes, models, database migrations, authorization, pairing, event lifecycle, alert routing and API tests. | `backend/...` except shared contracts explicitly assigned elsewhere. | Lead; voice and Android API consumer. |
| **Wispr/provider agent** | Official-doc verification, transcription provider abstraction, Wispr adapter, token/session integration, language handling and mock provider tests. | Provider-specific Android/backend modules explicitly allocated. | Lead; backend and Android voice agent. |
| **Safety/classification agent** | Deterministic phrase matching, negation, ambiguity, deduplication and safety test fixtures. | Classification module and its dedicated tests. | Lead; backend event contract and voice agent. |
| **QA/docs agent** | Test matrix, synthetic phrase fixtures, CI, architecture/API/privacy docs, demo checklist and acceptance evidence. | `docs/`, `.github/` and dedicated tests when not conflicting. | Lead and all module owners. |

These are possible responsibilities, not a requirement to launch six agents. Combine roles when the project is small; split them only when the work is genuinely parallelizable.

### Delegation protocol

Every delegated task must specify:

1. Objective and user-visible outcome.
2. Exact modules/files the agent owns.
3. Shared API/data/interface contracts to consume or produce.
4. Files and responsibilities explicitly out of scope.
5. Acceptance criteria and edge cases.
6. Commands/tests the agent must run.
7. Expected report: changed files, design decisions, test commands/results, limitations and blockers.

Additional rules:

- Define contracts first. Do not have two agents independently invent request/response schemas or event-state names.
- Prefer isolated Git worktrees/branches when the environment supports them. If agents share a working tree, assign strictly non-overlapping files and do not let them edit shared interfaces.
- Avoid parallel edits to root Gradle settings, dependency catalogs, migrations, central navigation, shared DTOs, provider interfaces and other high-conflict files.
- Do not allow an agent to invent a Wispr API, weaken Android permission handling, introduce hidden recording, bypass authorization or relax safety rules.
- Agents report actual validation; “implemented” without checks is not evidence.
- The lead reviews every diff, checks for privacy/security regressions and resolves integration conflicts.
- Do not let multiple agents commit to one shared branch concurrently. The lead integrates and commits after review unless isolated branch ownership was explicitly agreed.
- Run full integrated tests after combining agent work. Isolated test success does not prove the whole app works.
- If one agent is blocked by credentials, hardware or API approval, continue independent work without faking the blocked feature.

### When to parallelize by phase

- **CP0:** usually one lead performs repository audit; a documentation/QA agent may independently map existing tests and setup docs.
- **CP1:** Android UX agent may work on Patient Mode while another agent develops shared design/accessibility primitives only after contracts and file boundaries are agreed.
- **CP2–CP3:** backend schema/API work may proceed alongside Android UI shell work using agreed API contracts; avoid parallel edits to shared contracts.
- **CP4–CP5:** voice provider and deterministic safety classifier can be developed independently behind a stable provider/event interface.
- **CP6:** backend notification/escalation and caregiver UI can proceed in parallel after event lifecycle is frozen.
- **CP7:** Night Watch service and monitoring-health backend can proceed in parallel only after the session/status contract is agreed; physical-device testing remains an integrated responsibility.
- **CP8–CP9:** language/accessibility work, QA automation, documentation and hardening can run in parallel with non-overlapping ownership.
- **CP10:** demo, deck and final verification can be delegated, but the lead must verify every claim against the actual product and portal requirements.

## A4. Definition of done for every checkpoint

A checkpoint is accepted only when all applicable conditions below are satisfied:

- The user-visible behavior exists and is wired to real backend/business logic where required.
- API and data contracts are documented and validated.
- Authorization and state transitions are enforced in the backend, not merely hidden in the UI.
- Loading, empty, error, offline and permission-denied states are handled.
- Privacy, consent, accessibility and monitoring-health behavior are considered.
- Tests cover important success and failure paths.
- Relevant builds, tests, lint or static checks have actually run; results are recorded honestly.
- Manual/physical-device verification is completed when required, or explicitly marked outstanding.
- README and technical docs reflect the implemented behavior.
- The diff is reviewed for secrets, patient data, unsafe behavior and unrelated changes.
- A meaningful checkpoint commit is created and recorded.
- Remaining gaps, assumptions and external dependencies are disclosed.

## A5. Competition delivery reminders

The Round 2 brief previously shared by the team included a public GitHub repository, one-command Docker Compose backend setup, approximately 300+ lines of original team code, a YouTube demonstration of up to three minutes, and a pitch-deck PDF explaining problem/solution, users, dependencies and launch plan. These are planning notes, not a substitute for current official requirements. Before submission, revisit the live event portal and verify deadline, format, visibility, judging and upload requirements. Do not claim compliance until the deliverables are present and tested.

---

# Part B — Detailed product and engineering requirements

The detailed master prompt below preserves the full requirements supplied by the project owner. The governance additions above are mandatory for how this specification is executed.

---

# Detailed SunoCare Master Requirements

## Build a Production-Quality, Voice-First Bedside Care and Caregiver Coordination Ecosystem

Act as a senior Android engineer, mobile systems architect, voice-AI engineer, backend engineer, security specialist and product designer.

Your task is to build SunoCare: a comprehensive, privacy-conscious, voice-first Android care application for bedridden elderly people, stroke survivors, paralysed patients, people recovering at home and the family members responsible for caring for them.

Do not produce only a UI mockup or a technical proposal. Inspect the existing repository, understand its current implementation, make an implementation plan, and then build and test the application.

You have permission to make sensible architecture improvements, refactor weak implementations, introduce useful patient-care features, improve accessibility, strengthen security and add valuable capabilities that make the product more reliable. However, do not add features that compromise patient safety, privacy, Android platform compliance or the main voice-first experience.

The central product principle is:

**A patient should be able to communicate a need, request help or express distress without having to unlock their phone, type, navigate menus or reach a small button. Their voice is their primary interface.**

The caregiver should also be able to operate the application primarily through voice, with accessible visual controls as alternatives.

---

# PART 1 — PRODUCT IDENTITY AND ARCHITECTURE

Product name: SunoCare.

Tagline: Your Voice. Your Care. Always Connected.

Build ONE native Android application with TWO role-based experiences:

1. Patient Mode — bedside monitoring and hands-free voice communication.
2. Caregiver Mode — patient management, voice interaction, alerts, escalation, care history and configuration.

Both roles use the same Android application and backend. They have different permissions, navigation and interfaces.

A caregiver can manage multiple patients. A patient can have multiple authorised caregivers.

Use:

- Kotlin.
- Jetpack Compose.
- Material 3 with a custom accessible design system.
- MVVM and clean architecture.
- Kotlin Coroutines and Flow.
- Room for encrypted or appropriately protected local operational data.
- Retrofit or Ktor for REST networking.
- OkHttp WebSocket or an equivalent reliable WebSocket implementation.
- FastAPI backend.
- PostgreSQL.
- Docker Compose for backend deployment.
- Firebase Cloud Messaging as an optional remote push-notification provider.

The application must be maintainable, modular, documented and testable.

Recommended Android modules:

- core:common
- core:designsystem
- core:database
- core:network
- core:security
- core:voice
- core:notifications
- feature:authentication
- feature:onboarding
- feature:pairing
- feature:patient
- feature:caregiver
- feature:nightwatch
- feature:alerts
- feature:carehistory
- feature:settings

Do not make unnecessary modules if they increase complexity without value.

# PART 2 — ROLE-BASED LOGIN AND ACCOUNT ECOSYSTEM

Create a polished welcome screen with two large options:

"I am a Patient"
"I am a Caregiver"

Both open separate experiences in the same APK.

## Caregiver registration

Support secure account registration and login, profile management, session management and logout.

Use secure password hashing, access tokens and appropriate refresh-token handling in the backend. A production authentication provider may be introduced through an adapter.

The caregiver can:

- Create a patient profile.
- Pair a bedside device.
- Add additional caregivers.
- Assign primary and backup caregiver roles.
- Configure language preferences.
- Configure patient-specific emergency phrases.
- Configure monitoring schedules and notification preferences.
- View and manage multiple linked patients.

## Patient registration and pairing

A bedridden patient must not be forced to enter lengthy credentials every day.

Allow the caregiver to create the patient's profile and generate a one-time QR pairing code or PIN.

The bedside device selects Patient Mode, scans the QR or enters the PIN, and becomes securely paired with that patient profile.

Pairing credentials must:

- Expire after a short configurable period.
- Be single-use.
- Be securely validated by the backend.
- Be revocable by an authorised caregiver.

The bedside device should remember its authorised patient pairing across ordinary application restarts.

Allow patient authentication through the paired device and optional assisted reauthentication.

Do not expose other patients' records through a shared device.

Implement role-based authorisation on the backend. Hiding a button in the Android interface is not sufficient security.

# PART 3 — WISPR FLOW INTEGRATION (CORE REQUIREMENT)

Wispr Flow Voice Interface API must be the primary cloud transcription provider when approved API access and credentials are configured.

Do not confuse the consumer Wispr Flow Android dictation application with its Voice Interface API.

Do not attempt to automate Wispr Flow's floating bubble or treat it as an always-on microphone service.

Read and follow the current official documentation:

[https://api-docs.wisprflow.ai/introduction](https://api-docs.wisprflow.ai/introduction)

[https://api-docs.wisprflow.ai/websocket_api](https://api-docs.wisprflow.ai/websocket_api)

[https://api-docs.wisprflow.ai/websocket_quickstart](https://api-docs.wisprflow.ai/websocket_quickstart)

[https://api-docs.wisprflow.ai/request_schema](https://api-docs.wisprflow.ai/request_schema)

[https://api-docs.wisprflow.ai/client_side_auth_basics](https://api-docs.wisprflow.ai/client_side_auth_basics)

## Required integration

Implement a dedicated abstraction:

TranscriptionProvider

- WisprFlowTranscriptionProvider
- OnDeviceTranscriptionProvider
- DemoTranscriptionProvider

WisprFlowTranscriptionProvider must use the official Wispr Flow WebSocket API for streaming transcription.

The supplied prompt identifies the following client-auth WebSocket endpoint as its reference example:

`wss://platform-api.wisprflow.ai/api/v1/dash/client_ws?client_key=Bearer%20<CLIENT_KEY>`

**Verification gate:** confirm this exact endpoint and query/authentication semantics against the current official Wispr documentation before use. If the current official docs specify a different endpoint or flow, the current official docs take precedence; record the verified endpoint and date in `docs/wispr-integration.md`.

The organisation API key must NEVER be embedded in the Android application.

The backend must:

1. Read WISPR_API_KEY from environment variables.
2. Generate a short-lived client JWT using the official access-token endpoint.
3. Associate the token with a stable pseudonymous device/client identifier.
4. Return the short-lived JWT to the authenticated Android application.
5. Allow token renewal and revocation.
6. Apply server-side usage controls.

Follow the official client-authentication flow and WebSocket protocol exactly.

After authentication, initialise the transcription session with:

- type: auth
- access_token
- language
- context

Use app context:

- app.name: SunoCare
- app.type: other
- dictionary_context: approved contextual words and phrases relevant to the patient's language.

Do not send unnecessary patient medical details or personal information in transcription context.

## Audio requirements

Capture audio using Android AudioRecord or an equivalent native audio API.

Prepare audio for Wispr in the documented format:

- 16,000 Hz sample rate.
- Single-channel mono.
- Signed 16-bit PCM.
- WAV-compatible encoding.
- Base64 encoding as required by the WebSocket message format.
- Consistent sequential audio chunks.
- Correct packet positions and final commit message.

Use consistent chunk durations, approximately one second where appropriate, and follow Wispr's current protocol sample for the exact append payload.

Handle:

- WebSocket connection establishment.
- Authentication acknowledgement.
- Partial transcripts.
- Final transcripts.
- Session commit.
- Connection timeout.
- Token expiration.
- Reconnection.
- API failure.
- Billing or usage-limit exhaustion.

Configure the selected language list using ISO 639-1 codes. For example:
Malayalam + English = ["ml", "en"].

One selected language should be passed as a single language. For two-language mode, pass both expected languages. Support automatic language detection only as a fallback, since the API documentation notes that providing expected languages improves the transcription context.

## Wispr safety restrictions

Wispr Flow is a transcription service, not an emergency detector, medical classifier, audio noise-cancellation guarantee or medical diagnostic system.

Its transcription can apply automatic editing, remove fillers and change wording for readability. Do not assume its output is an exact legal or clinical verbatim record.

Therefore:

- Preserve event audio temporarily in an in-memory buffer where consent permits.
- Store the Flow transcript separately from any locally produced literal transcript.
- Preserve language metadata and provider response identifiers.
- Use deterministic, safety-reviewed phrase matching for configured emergency phrases.
- Do not let an LLM independently decide that a patient is medically safe.
- Do not allow a polished transcript to erase the distinction between a positive and negative medical statement.
- When wording is ambiguous or a negation may change meaning, escalate for human verification.
- Never invent a confidence score that the provider did not return.

## API availability and fallback

Wispr API access requires approved developer organisation access. Never assume that a normal Flow Pro subscription or hackathon promotion automatically grants API access.

Implement configuration:

WISPR_ENABLED
WISPR_API_KEY
WISPR_API_BASE_URL
WISPR_DAILY_USAGE_LIMIT
WISPR_SESSION_DURATION_LIMIT

Include .env.example with placeholders, never actual secrets.

If API credentials are absent:

- Build the integration fully.
- Keep the rest of the application usable.
- Use an explicitly labelled offline/demo provider.
- Use a local speech engine only when its actual language capabilities have been verified.
- Do not silently substitute another cloud provider.
- Display "Wispr unavailable — offline/demo mode" accurately.

Protect against unlimited transcription charges. Apply a configurable internal usage budget, duration limits, monitoring and a graceful fallback when limits are reached.

The Wispr API receives transmitted audio segments for cloud transcription. Clearly disclose this to the patient/caregiver and obtain appropriate consent. Do not claim that Wispr API processing is completely local.

# PART 4 — LANGUAGE AND VOICE-FIRST INTERACTION

Language selection is a first-class part of SunoCare.

During patient setup, allow:

1. Single-language mode.
2. Mixed-language/code-switching mode.
3. Primary and secondary language selection.
4. Optional regional accent preferences.
5. Response-language preference.

Initially prioritise:

- Malayalam.
- English.
- Malayalam-English code-switching.
- Hindi.
- Tamil.

Allow other languages only when the selected speech and TTS engines actually support them.

The language selector must not falsely advertise unsupported transcription or speech synthesis.

For every patient, persist:

- Primary language.
- Secondary language.
- Mixed-language enabled state.
- Speech recognition language list.
- Spoken response preference.
- Available TTS voice status.

Use Android TextToSpeech or a suitable voice-output provider.

Whenever possible, speak in the patient's preferred language. If a language voice is unavailable, use the best available fallback and communicate the limitation clearly.

## Patient voice interactions

The patient must be able to:

- Ask for water.
- Request pillow adjustment.
- Ask for repositioning.
- Request bathroom assistance.
- Ask for a caregiver.
- Express discomfort.
- Say that they need urgent help.
- Confirm or deny a clarification question.
- Cancel an unsubmitted ordinary request.
- Receive spoken acknowledgement.
- Ask whether a caregiver has acknowledged the request.

Example:

Patient: "Enikku vellam venam."

SunoCare: "Okay, I've notified your caregiver."

Patient: "Did they hear me?"

SunoCare: "Your caregiver has received your request. They haven't acknowledged it yet."

Only communicate delivery or acknowledgement states supported by actual system events.

## Caregiver voice interactions

Caregiver Mode must also be voice-first.

Provide a global voice command control and voice navigation.

Support commands such as:

- "Show my patients."
- "What is Meera's current monitoring status?"
- "Read the latest critical alert."
- "Show unacknowledged requests."
- "What happened last night?"
- "Read the last care summary."
- "Acknowledge this alert."
- "Contact the primary caregiver."
- "Switch to patient Meera."
- "Open monitoring settings."

Read important alerts aloud when permitted.

Require explicit confirmation for sensitive actions, account changes, deleting records, changing caregiver access and modifying emergency settings.

# PART 5 — NIGHT WATCH AND CONTINUOUS BEDSIDE MONITORING

This is one of the most important engineering components.

Implement Night Watch as a native Android microphone foreground service.

The service must:

- Start only after the patient or authorised caregiver explicitly activates it from a visible application screen.
- Request RECORD_AUDIO permission.
- Declare the correct microphone foreground-service type and permissions for the target Android version.
- Display a persistent Android notification showing that microphone monitoring is active.
- Allow authorised stopping and pausing.
- Report operational status continuously.
- Handle audio-device disconnection.
- Handle permission revocation.
- Detect microphone unavailability.
- Handle battery and connectivity problems.
- Stop and report failure safely when audio capture becomes unavailable.

Support configurable sessions:

- Short daytime care session.
- Overnight Night Watch.
- Longer caregiver-authorised monitoring session.
- All-day monitoring, where the device and Android policies permit it.

Do not silently activate the microphone at boot.

Do not promise uninterrupted background microphone capture on every Android device.

Provide a clear reactivation prompt after reboot, device restart, permission loss or service termination.

Do not use Accessibility permission merely to bypass Android restrictions.

## Local-first audio pipeline

When Night Watch is active:

1. Capture audio locally.
2. Perform lightweight voice activity detection.
3. Preserve weak speech and whisper sensitivity.
4. Apply appropriate local noise suppression where available.
5. Maintain a short volatile ring buffer.
6. Detect speech segments without uploading continuous room silence.
7. Send necessary speech segments to Wispr Flow for transcription.
8. Send relevant text to the intent and safety layer.
9. Discard temporary audio when it is no longer needed.

Do not continuously stream the entire bedroom microphone feed to Wispr Flow.

Add configurable patient-specific sensitivity calibration because a weak patient whisper must not be treated like ordinary low-volume background sound.

Optional advanced feature: implement a local low-power wake phrase detector for "SunoCare" or a user-configured phrase. It should operate locally, if the model is available, and must not be a mandatory condition for detecting configured emergency phrases while Night Watch is active.

# PART 6 — HARDWARE SHORTCUTS AND EMERGENCY ACCESS

The patient must have a way to request assistance even when they cannot navigate the interface.

IMPORTANT: Do not implement an interception of a five-second physical POWER-button hold. Normal Android applications cannot reliably intercept the system-reserved power key. Do not attempt unsupported system-key hacks, exploit Accessibility APIs or override Android Emergency SOS.

Instead, implement the following:

1. Large, accessible SOS button on the patient interface.
2. Persistent Android notification action for authorised emergency activation.
3. Home-screen widget with a large SOS or help action.
4. User-addable Android Quick Settings tile for SunoCare access.
5. Lock-screen-visible monitoring notification, subject to device settings.
6. Optional integration with a paired Bluetooth Low Energy physical SOS button.

The optional Bluetooth button should:

- Require deliberate pairing.
- Support explicit user testing.
- Trigger a locally registered emergency event.
- Provide connection-status information.
- Avoid claiming that an unpaired or disconnected device is working.

Make physical assistance as easy as possible without violating system restrictions.

For emergency shortcuts, ask for the least amount of interaction possible.

# PART 7 — SAFETY-BASED EVENT CLASSIFICATION

Implement three separate event classes.

## A. Explicit configured emergency phrase

Examples:

- "I cannot breathe."
- "Help me."
- "Chest pain."
- "Call my daughter."
- Caregiver-approved phrases in Malayalam and other configured languages.

Treat a positively recognised, configured emergency phrase as a critical escalation event.

Notify the primary caregiver immediately.

Do not ask the patient to confirm the same explicit emergency statement before alerting the caregiver.

Do not use AI-generated medical diagnoses.

Avoid false matches caused by negations. For example, don't match "I can breathe" as "I cannot breathe".

Emergency phrases should be patient-configurable and reviewed in the preferred language.

## B. Possible distress

Examples:

- Weak, unclear calls for help.
- Unexplained groaning.
- Repeated unclear speech.
- A possible distress sound without sufficiently clear words.

Use a conservative local acoustic-event detector where an appropriate model exists.

Ask one gentle verbal clarification.

If the patient does not respond, escalate as POSSIBLE DISTRESS and notify the caregiver.

Silence is not proof of safety.

## C. Ordinary care request

Examples:

- Water.
- Pillow adjustment.
- Room light.
- Repositioning.
- Bathroom assistance.
- Request to speak with a family member.

Send a normal-priority request, provide a spoken response and track whether someone addressed it.

The system must never describe a request as medically diagnosed or medically confirmed.

Use deterministic rules for emergency phrase matching. Use AI/LLM assistance for summaries and clarification where useful, not for autonomous medical triage.

# PART 8 — CAREGIVER ALERT AND ESCALATION ENGINE

Implement a complete alert lifecycle.

CREATED
TRANSCRIBED
CLASSIFIED
NOTIFICATION_PENDING
NOTIFIED
ACKNOWLEDGED
ESCALATED
RESOLVED

Track each state with timestamps.

For critical events:

1. Notify the primary caregiver immediately.
2. Show a prominent alert on their phone.
3. Use an optional alarm-like notification channel when the user has granted the required Android settings.
4. If there is no acknowledgement before the configured timeout, notify backup caregiver one.
5. If still unacknowledged, notify backup caregiver two if configured.
6. Keep the alert visible until someone takes an appropriate action.
7. Allow the caregiver to contact the patient.
8. Record all notification and acknowledgement events.

Allow configurable escalation intervals. Use short intervals for critical events and longer ones for ordinary requests.

Never automatically call emergency services without explicit user authorisation and verified implementation requirements.

Provide a direct call-to-emergency-services action when configured, but make it clear that the caregiver must initiate it.

Notification infrastructure must report whether the alert was actually accepted for delivery, and where possible whether the device received it.

Do not assume that a backend event creation is equivalent to successful notification delivery.

If the caregiver is offline, mark the notification as pending or delivery failed, and retry using appropriate bounded retry logic.

# PART 9 — CAREGIVER AND PATIENT INTERFACES

Design a professional, calming and highly accessible medical-care coordination interface.

The patient interface must be designed for people who:

- May have weak vision.
- May have limited hand movement.
- May be bedridden.
- May not understand technical terms.
- May be using the app in a dark bedroom.
- May have difficulty reading.
- May need everything read aloud.

Patient Mode:

- Very large text.
- Large primary interaction targets.
- High contrast.
- Minimal visual clutter.
- Night mode.
- Large monitoring state.
- Simple spoken interaction.
- No unnecessary navigation while Night Watch is active.
- Clear indication when monitoring is paused or interrupted.

Caregiver Mode:

- Clean home dashboard.
- Patient cards.
- Patient monitoring status.
- Critical alert list.
- Pending request list.
- Caregiver management.
- Event timeline.
- Language preferences.
- Privacy and device settings.
- Notification settings.
- Voice navigation entry point.

Use a consistent design system with:

- Proper spacing and typography.
- Rounded but professional components.
- Distinct severity labels.
- Semantic colours and text labels.
- Smooth but restrained animations.
- Dark and light mode.
- TalkBack support.
- Minimum accessible tap sizes.
- Large font support and screen scaling.

Do not build a cluttered hospital-management dashboard. SunoCare should feel like a trustworthy personal care companion.

# PART 10 — MONITORING HEALTH AND RELIABILITY

Create a monitoring-health subsystem independent of speech recognition.

Track:

- Night Watch state.
- Microphone permission.
- Service status.
- Last successful audio capture.
- Wispr WebSocket connection.
- Transcription provider status.
- Network status.
- Battery state.
- Charging state.
- Last backend heartbeat.
- Last successful caregiver notification.

Use clearly differentiated states:

ACTIVE
PAUSED
INTERRUPTED
NETWORK_UNAVAILABLE
MICROPHONE_UNAVAILABLE
SERVICE_STOPPED
PERMISSION_REQUIRED
PROVIDER_UNAVAILABLE

Notify caregivers when monitoring is interrupted, subject to available connectivity.

Do not show a green active indicator merely because the app was opened.

If the service stops, report the last-known operational state and its timestamp.

Implement safe recovery and reconnection mechanisms, but never bypass Android foreground-service restrictions.

Account for manufacturer battery optimisation. Provide optional guided instructions for permitted battery settings without coercing the user to disable system security.

# PART 11 — CARE HISTORY AND DAILY SUMMARIES

Create an event-based care timeline.

Include:

- Event time.
- Event type.
- Original language.
- Recognised transcript.
- Transcription provider.
- Notification recipients.
- Notification delivery state.
- Acknowledgement time.
- Caregiver action.
- Resolution state.
- Optional audio attachment status and retention expiry.

Generate a simple morning summary:

- Total requests.
- Possible distress events.
- Configured emergency phrase activations.
- Unanswered events.
- Caregiver response times.
- Recurring patient-reported complaints.

The summary should distinguish observations from medical interpretations.

Allow caregivers to export an authorised PDF summary for a healthcare professional.

Provide appropriate deletion, correction and retention controls.

# PART 12 — PRIVACY AND SECURITY

Privacy is a core product feature, not an optional setting.

Requirements:

- Explicit informed monitoring consent.
- Visible microphone monitoring notification.
- Accessible pause and stop control.
- No covert recording.
- No continuous raw audio storage by default.
- Local audio ring buffer in memory.
- Limited capture-window retention.
- Encryption in transit.
- Secure local storage.
- Backend role-based access.
- Patient/caregiver relationship verification.
- Short-lived authentication tokens.
- Secure API secrets.
- Audit logs for sensitive actions.
- Revocable pairing.
- Account logout and device unpairing.
- Consent-based optional audio sharing.

Only send the audio segments required for Wispr transcription after the monitoring privacy disclosure and consent process.

Do not falsely claim that cloud transcription is local processing.

The organisation's Wispr API credentials must remain on the backend. Never put them into Android resources, source code, APK assets, Gradle files or Git history.

Do not upload real patient data or audio to development testing systems without consent.

Do not claim HIPAA, Indian medical-device compliance, CDSCO approval or clinical validation without evidence.

Do not make medical diagnoses or treatment recommendations.

# PART 13 — BACKEND AND DATABASE

Build a modular FastAPI backend.

Use PostgreSQL with SQLAlchemy and Alembic migrations.

Core backend modules:

- authentication
- users
- patient_profiles
- caregivers
- patient_caregiver_relationships
- devices
- pairing
- language_preferences
- wispr_integration
- transcription_sessions
- care_events
- emergency_phrases
- notification_deliveries
- acknowledgements
- escalation_rules
- care_history
- monitoring_health
- audit_logs

Create documented REST endpoints and WebSocket endpoints.

At minimum support:

- Account creation and login.
- Patient profile creation.
- Patient pairing code generation and validation.
- Device registration.
- Caregiver authorisation.
- Language configuration.
- Wispr temporary-token generation.
- Event creation and classification.
- Alert delivery and status.
- Acknowledgement.
- Escalation.
- Monitoring-health heartbeat.
- Care-history retrieval.
- Summary generation.
- Audit and consent history.

Use proper database constraints, indexes, validation, pagination and error handling.

Never allow one caregiver to retrieve an unrelated patient's data.

# PART 14 — VOICE ASSISTANT DESIGN

Treat voice as a system-wide interaction model.

Every important patient-side and caregiver-side screen should expose a way to speak.

Use Android TextToSpeech for speech output where available.

Support:

- Speak the selected page title.
- Read alerts.
- Read patient monitoring state.
- Read event history.
- Explain monitoring interruptions.
- Confirm a caregiver action.
- Ask clarifying questions.
- Read the morning summary.
- Announce successful and unsuccessful notification states.

Caregivers must be able to use voice navigation.

Patient commands must be interpreted using the selected patient language configuration.

When a voice command has consequences, provide a confirmation step.

If speech output fails, provide an accessible visual and audible fallback.

# PART 15 — DOCKER, DEVELOPMENT AND SCALER SUBMISSION

The repository must be publicly reviewable and easy to run.

Docker Compose must start:

- FastAPI backend.
- PostgreSQL.
- Any necessary backend development services.

The Android APK must be built separately using Gradle.

Provide:

- docker-compose.yml
- backend Dockerfile
- .env.example
- .gitignore
- README.md
- API documentation.
- Architecture documentation.
- Wispr integration guide.
- Android build and installation instructions.
- Local demonstration instructions.
- Automated tests.

One-command backend startup:

docker compose up --build

Do not require real Wispr credentials to start the basic demonstration environment.

When credentials are configured, enable the real Wispr integration.

When they are absent, use an explicitly labelled demonstration or verified local fallback mode.

Never fabricate successful API calls.

Do not commit credentials, private keys, Firebase service-account files, patient recordings or real patient records.

# PART 16 — TESTING AND VALIDATION

Write meaningful unit tests, integration tests and Android tests.

Required tests:

1. Caregiver registration and login.
2. Patient pairing.
3. Role-based authorisation.
4. Malayalam language selection.
5. English language selection.
6. Malayalam-English configuration.
7. Wispr token generation.
8. Wispr API disabled fallback.
9. WebSocket authentication failure.
10. Transcription success and failure.
11. Sequential audio chunk handling.
12. Emergency phrase detection.
13. Negation handling.
14. Ordinary care-request classification.
15. Possible distress classification.
16. Patient spoken acknowledgement.
17. Caregiver alert creation.
18. Caregiver acknowledgement.
19. Backup escalation.
20. Duplicate notification prevention.
21. Monitoring interruption reporting.
22. Microphone permission denial.
23. Service shutdown.
24. Offline event handling.
25. Event history and summary.
26. Unauthorised patient access prevention.
27. Consent and audio-retention behaviour.

Test on real Android hardware, not just an emulator.

Test the bedside device with the display locked and the persistent foreground notification visible.

Test weak speech, fans, television noise, different accents and code-switching using controlled recordings.

Do not represent a small set of demonstration recordings as clinically validated performance.

# PART 17 — DEVELOPMENT PHASES

Implement in the following order.

PHASE 1: Repository inspection and foundation

- Inspect existing source files.
- Preserve useful existing implementation.
- Establish architecture.
- Create Android project.
- Create backend and database.
- Establish role-based navigation.

PHASE 2: Authentication and pairing

- Caregiver account.
- Patient profiles.
- One-time QR/PIN pairing.
- Device registration and permissions.

PHASE 3: Voice foundation

- Android audio capture.
- Night Watch foreground service.
- Wispr WebSocket integration.
- Language configuration.
- Text-to-speech.
- Offline/demo fallback.

PHASE 4: Intelligence and safety

- VAD.
- Weak-speech handling.
- Emergency phrase matching.
- Possible-distress detection.
- Ordinary request classification.
- Spoken clarification.

PHASE 5: Caregiver ecosystem

- Alerts.
- Acknowledgements.
- Backup escalation.
- Patient management.
- Voice navigation.
- Care history.

PHASE 6: Reliability and privacy

- Monitoring health.
- Audio retention controls.
- Authentication hardening.
- Notification reliability.
- Offline handling.

PHASE 7: Final submission

- Android APK.
- Docker Compose validation.
- Public GitHub README.
- Automated tests.
- API documentation.
- Demonstration script.
- Three-minute demo preparation.

# PART 18 — PRODUCT DESIGN AND ENGINEERING AUTONOMY

Do not stop at the minimum feature list if meaningful improvements are possible.

You may add:

- Better accessible voice onboarding.
- Patient-specific speech calibration.
- Additional caregiver response options.
- Better battery-health monitoring.
- Improved alert prioritisation.
- Better recovery from connectivity interruptions.
- A caregiver handoff timeline.
- Improved bilingual speech handling.
- Safe physical SOS switch integration.
- Better event deduplication.
- Useful family care summaries.
- Additional high-value accessibility features.

However, never introduce a feature that:

- Silently records patients.
- Intercepts system-reserved hardware keys.
- Bypasses Android permission restrictions.
- Makes unsupported medical claims.
- Conceals failed monitoring.
- Automatically dismisses critical alerts.
- Stores unnecessary sensitive audio.
- Exposes patient information to unauthorised users.

Prioritise reliability over visual decoration and patient safety over feature count.

# PART 19 — REQUIRED CODEX WORKING METHOD

Begin by inspecting the complete existing repository.

Do not immediately overwrite files or discard working code.

Produce a concise repository audit and implementation checklist, then start implementation in coherent milestones.

Do not leave the project at the planning stage.

For every milestone:

1. Implement the actual functionality.
2. Run relevant tests.
3. Fix errors.
4. Update documentation.
5. Keep the repository in a buildable state.

Do not claim an API works if no API credentials were available to test it.

Do not claim Night Watch works in background if it has only been tested with a foreground microphone screen.

Do not claim emergency notifications were delivered unless actual delivery was verified.

At completion, provide:

- Features actually implemented.
- Features implemented using simulation.
- Features requiring Wispr API approval or credentials.
- Features requiring physical-device verification.
- Features still incomplete.
- Exact commands to run the backend.
- Exact commands to build and install the APK.
- Exact steps for a two-device patient/caregiver demonstration.
- Test results, including any failures.

The final goal is a coherent, credible, voice-first care ecosystem rather than a collection of disconnected features.



**Build SunoCare as if a real patient and their family will eventually depend on it. Make it accessible, private, reliable, transparent and exceptionally easy to use through voice.**

---

# Part C — Final Codex execution checklist

Before beginning a new task:

- [ ] Read root `AGENTS.md` and the entire `instructions.md`.
- [ ] Inspect `git status`, current branch, existing implementation and tests.
- [ ] Identify the current CP0–CP10 checkpoint and its prerequisites.
- [ ] Decide whether work is independent enough for sub-agents; use them only if supported.
- [ ] Define contracts and file ownership before delegation.
- [ ] State a concise plan and the exact behavior being implemented.

Before declaring a checkpoint complete:

- [ ] Acceptance criteria are met with actual evidence.
- [ ] Tests/builds/lint relevant to the changed code were run and results recorded.
- [ ] Any vendor API integration was checked against current official docs.
- [ ] Any Android background/microphone claim was verified on a real device where required.
- [ ] Any notification delivery claim is supported by actual delivery evidence.
- [ ] Privacy, consent, authorization and accessibility have been reviewed.
- [ ] Simulated features are clearly labelled.
- [ ] The diff contains no secrets, real patient information or unnecessary audio.
- [ ] A focused Conventional Commit exists and its SHA is recorded.
- [ ] The development log and relevant documentation are updated.
- [ ] Known limitations and next steps are explicit.

## Final reporting format

At the end of a substantial task or checkpoint, report:

1. **Checkpoint:** CP number and title.
2. **Implemented:** concrete behavior and modules changed.
3. **Agent contributions:** agent role, owned scope and integrated result, or state that work was completed sequentially because sub-agents were unavailable/not useful.
4. **Commits:** message and SHA for each commit created.
5. **Validation:** exact commands run and actual pass/fail results.
6. **Manual/device verification:** device, Android version and scenario tested, or clearly state not tested.
7. **Live vs simulated:** distinguish live Wispr/FCM/etc. from mocks and local demos.
8. **Outstanding:** bugs, missing credentials, approval, hardware checks and known limitations.
9. **Next checkpoint:** the smallest sensible next implementation step.

**Product north star:** build SunoCare as if a real patient and their family may eventually depend on it. Make it accessible, private, reliable, transparent and exceptionally easy to use by voice. Prioritize the complete patient voice request → backend event → caregiver alert → caregiver acknowledgement/resolution loop over feature count or visual decoration.
