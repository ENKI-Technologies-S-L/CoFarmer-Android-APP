# Upstream Synchronization Guide

This document explains how to keep the CoFarmer Android app synchronized with the original [Home Assistant Companion for Android](https://github.com/home-assistant/android) repository.

---

## Overview

CoFarmer is a **fork** of the Home Assistant Android app. As the Home Assistant team releases new features, bug fixes, and security updates, we need to periodically merge these changes into our fork while preserving our customizations.

### Key Principles

1. **Preserve Branding**: Our visual identity (icons, colors, strings) must not be overwritten
2. **Adopt Improvements**: Security patches, performance improvements, and new features should be merged
3. **Resolve Conflicts Carefully**: String files and theme files will often have conflicts
4. **Test Thoroughly**: After any merge, full regression testing is required

---

## Repository Setup

### Initial Setup (One-time)

```bash
# Clone the CoFarmer repository
git clone https://github.com/enkitek/CoFarmer-Android-APP.git
cd CoFarmer-Android-APP

# Add the original Home Assistant repo as "upstream"
git remote add upstream https://github.com/home-assistant/android.git

# Verify remotes
git remote -v
# Should show:
# origin    https://github.com/enkitek/CoFarmer-Android-APP.git (fetch)
# origin    https://github.com/enkitek/CoFarmer-Android-APP.git (push)
# upstream  https://github.com/home-assistant/android.git (fetch)
# upstream  https://github.com/home-assistant/android.git (push)
```

---

## Sync Process

### Step 1: Fetch Upstream Changes

```bash
# Make sure you're on main branch
git checkout main

# Fetch all upstream changes (doesn't modify local files)
git fetch upstream
```

### Step 2: Review What's New

```bash
# See commits since our last sync
git log main..upstream/main --oneline

# See changed files
git diff main..upstream/main --stat

# Review specific areas of concern
git diff main..upstream/main -- common/src/main/res/values/strings.xml
git diff main..upstream/main -- common/src/main/kotlin/*/theme/HAColors.kt
```

### Step 3: Create a Sync Branch

```bash
# Create a dedicated branch for the merge
git checkout -b sync/upstream-YYYY-MM-DD

# Example:
git checkout -b sync/upstream-2026-02-01
```

### Step 4: Merge Upstream

```bash
# Merge upstream main into your sync branch
git merge upstream/main
```

### Step 5: Resolve Conflicts

Conflicts are expected in these files. Here's how to handle them:

#### High-Conflict Files (Always Review Carefully)

| File | Strategy |
|------|----------|
| `common/src/main/res/values/strings.xml` | Keep CoFarmer terminology, accept new strings |
| `common/src/main/kotlin/.../theme/HAColors.kt` | Keep CoFarmer colors, accept new color tokens |
| `app/src/main/res/values/colors.xml` | Keep CoFarmer palette |
| `app/src/main/res/drawable/ic_launcher_*.xml` | **Always keep ours** |
| `app/src/main/res/drawable/ic_home_assistant_*.xml` | **Always keep ours** |
| `build-logic/.../AndroidApplicationConventionPlugin.kt` | Keep our APPLICATION_ID |

#### Conflict Resolution Commands

```bash
# Keep our version of a file entirely
git checkout --ours path/to/file.xml
git add path/to/file.xml

# Keep upstream version entirely (rare, only for non-branded files)
git checkout --theirs path/to/file.xml
git add path/to/file.xml

# Manual merge (most common for strings.xml)
# 1. Open the file in VS Code
# 2. Use the merge editor to pick changes
# 3. Save and stage
git add path/to/file.xml
```

#### Strings.xml Merge Strategy

When merging `strings.xml`:

1. **Accept all NEW strings** from upstream (new features)
2. **Keep our MODIFIED strings** (CoFarmer terminology)
3. **Accept CHANGED strings** if they're purely technical (error codes, etc.)
4. **Review DELETED strings** - they may indicate removed features

Example conflict:
```xml
<<<<<<< HEAD (ours - CoFarmer)
    <string name="manual_title">What is your CoFarmer address?</string>
=======
    <string name="manual_title">What is your Home Assistant address?</string>
>>>>>>> upstream/main
```
**Resolution**: Keep ours (CoFarmer terminology)

### Step 6: Build and Test

```bash
# Clean build after merge
./gradlew clean assembleFullDebug

# Run tests
./gradlew test

# Check lint
./gradlew lint --continue
```

### Step 7: Complete the Merge

```bash
# If everything passes, commit the merge
git commit -m "Sync with upstream home-assistant/android (YYYY-MM-DD)"

# Push to origin
git push origin sync/upstream-YYYY-MM-DD

# Create a Pull Request to main
```

### Step 8: Post-Merge Checklist

- [ ] App builds successfully (full and minimal flavors)
- [ ] App launches without crashes
- [ ] Onboarding flow works (including QR scanner)
- [ ] Icons are correct (not reverted to Home Assistant)
- [ ] Colors are correct (CoFarmer green/blue palette)
- [ ] All "Home Assistant" text is still replaced with "CoFarmer"
- [ ] Notifications show CoFarmer icon
- [ ] Splash screen shows Enkitek logo

---

## Handling Major Upstream Changes

### Scenario: New Onboarding Screen Added

1. Accept the new screen from upstream
2. Review for "Home Assistant" terminology
3. Add necessary strings to our `strings.xml` with CoFarmer wording
4. Verify navigation integration

### Scenario: Theme System Refactored

1. Accept structural changes
2. Re-apply CoFarmer color values to new structure
3. Test light/dark modes thoroughly

### Scenario: New Dependencies Added

1. Accept dependency changes
2. Run `./gradlew alldependencies --write-locks`
3. Verify no conflicts with existing dependencies

### Scenario: Breaking API Changes

1. Review changelog/release notes from Home Assistant
2. Understand the changes before merging
3. May require code modifications beyond conflict resolution

---

## Automated Sync Monitoring

### GitHub Actions (Recommended)

Create `.github/workflows/upstream-check.yml`:

```yaml
name: Check Upstream Updates

on:
  schedule:
    - cron: '0 9 * * 1'  # Every Monday at 9 AM
  workflow_dispatch:

jobs:
  check-upstream:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      
      - name: Add upstream remote
        run: git remote add upstream https://github.com/home-assistant/android.git
      
      - name: Fetch upstream
        run: git fetch upstream
      
      - name: Check for new commits
        id: check
        run: |
          COMMITS=$(git rev-list --count main..upstream/main)
          echo "new_commits=$COMMITS" >> $GITHUB_OUTPUT
          
      - name: Create issue if updates available
        if: steps.check.outputs.new_commits > 0
        uses: actions/github-script@v7
        with:
          script: |
            github.rest.issues.create({
              owner: context.repo.owner,
              repo: context.repo.repo,
              title: '🔄 Upstream updates available',
              body: `There are ${{ steps.check.outputs.new_commits }} new commits in the upstream Home Assistant repository.\n\nPlease review and sync following UPSTREAM_SYNC.md`,
              labels: ['upstream-sync']
            })
```

---

## Version Mapping

Track which upstream version CoFarmer is based on:

| CoFarmer Version | Based on HA Android | Sync Date |
|------------------|---------------------|-----------|
| 1.0.0-alpha | main@2026-01-20 | 2026-01-20 |

Update this table after each successful sync.

---

## Troubleshooting

### Build Fails After Merge

```bash
# Try cleaning everything
./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew assembleFullDebug
```

### Dependency Lock Errors

```bash
# Regenerate lock files
./gradlew alldependencies --write-locks
```

### KtLint Errors After Merge

```bash
# Auto-format
./gradlew ktlintFormat
```

### Icons Reverted to Home Assistant

The merge likely overwrote our drawable files. Restore them:

```bash
git checkout HEAD~1 -- app/src/main/res/drawable/ic_launcher_*.xml
git checkout HEAD~1 -- app/src/main/res/drawable/ic_home_assistant_branding.xml
git checkout HEAD~1 -- common/src/main/res/drawable/ic_stat_ic_notification*.xml
```

---

## Contact

For questions about the sync process, contact the CoFarmer development team.
