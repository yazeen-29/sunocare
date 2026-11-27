# AGENTS.md — Mandatory Codex Instructions for SunoCare

This file applies to the entire repository. Codex must follow it before planning, editing, generating, testing, committing or reviewing project changes.

## 1. Mandatory specification loading

**Before doing any work, read the complete root-level `instructions.md`. Do not rely on a summary, a prior conversation, selected excerpts or memory.** It is the authoritative source for SunoCare's product identity, detailed features, architecture, Android and Wispr requirements, user journeys, Night Watch, emergency handling, privacy, testing, development checkpoints, Git commits and sub-agent orchestration.

If `instructions.md` cannot be found or read, stop before modifying code and tell the user exactly what is missing. Do not silently continue with partial requirements.

When working in a subdirectory, follow this root `AGENTS.md` and check for any more specific nested `AGENTS.md` files. A nested file may add local rules but must not override the safety/privacy requirements or the root master specification.

## 2. Repository reconnaissance first

Before edits:

1. Inspect `git status --short`, current branch and recent commits.
2. Inspect the actual files, architecture, build scripts, dependency versions and relevant tests.
3. Determine whether the requested capability already exists, is incomplete, is mocked or is missing.
4. Preserve working code; avoid broad rewrites unless the evidence justifies them.
5. Identify the appropriate CP0–CP10 development checkpoint and its acceptance criteria.

Do not assume that requirements in `instructions.md` are already implemented.

## 3. Plan, implement and verify

For every substantial task:

1. State a concise plan, assumptions, target checkpoint and likely files/modules to change.
2. Define or confirm API/data contracts before parallel work.
3. Implement a small coherent vertical slice, not disconnected placeholder screens or local-only button state.
4. Run relevant tests, builds, linters and static checks.
5. Inspect the diff for regressions, secrets, real patient data, raw audio, unsafe assumptions and unrelated changes.
6. Update documentation and the development log.
7. Report exactly what works, what is simulated, what failed and what remains unverified.

Never fabricate passing test results, API success, transcription accuracy, notification delivery, service uptime or physical-device validation.

## 4. Checkpoint and commit discipline

Follow the CP0–CP10 checkpoint plan in `instructions.md`. Do not mark a checkpoint complete because its files exist or compilation succeeds alone.

After acceptance criteria are met, create focused, meaningful Conventional Commits. Inspect staged diffs and run relevant checks first. Record checkpoint, SHA, message, scope and test results in `docs/development-log.md` or the PR. Do not create empty, vague or commit-count-inflating commits. Do not push directly to protected `main`, force-push shared branches or rewrite teammates' published commits without explicit agreement.

## 5. Sub-agent policy

Use multiple Codex sub-agents when the active environment supports them and the tasks are genuinely independent. If sub-agents are unavailable, work sequentially or assign bounded tasks to human teammates; never claim an agent was used when it was not.

The lead agent must define contracts and non-overlapping file ownership, give each agent an objective and acceptance criteria, inspect every diff, integrate changes and run combined end-to-end checks. Do not let multiple agents concurrently edit shared schemas, migrations, root Gradle files, navigation roots or core provider interfaces. See Part A3 of `instructions.md` for role suggestions and delegation protocol.

## 6. Safety, privacy and platform constraints

The requirements in `instructions.md` are mandatory. In particular:

- No covert recording, hidden microphone activation, silent boot-time monitoring or false “active” state.
- No bypassing Android permission/foreground-service restrictions, no unsupported power-button interception and no misuse of Accessibility Services.
- No medical diagnosis or unsupported clinical/safety claims.
- No autonomous AI dismissal of configured urgent phrases.
- No claim that an alert was delivered when it was only created or queued.
- No patient data, recordings, credentials, provider keys or private signing material in Git.
- No client-side exposure of the Wispr organisation API key.
- No invention of API endpoints or provider behavior; verify current official documentation.

When a requested shortcut conflicts with safety, privacy or Android policy, explain the limitation and implement the safest supported alternative.

## 7. Required completion report

For every substantial task, conclude with:

- checkpoint and scope;
- implemented behavior and changed modules;
- sub-agent roles/contributions (or state that work was sequential);
- commit message(s) and SHA(s);
- exact tests/builds run and results;
- what is live vs simulated;
- physical-device or external-credential verification still needed;
- unresolved limitations and next step.

**Do not sacrifice patient safety, privacy or truthfulness to make a demo appear complete.**
