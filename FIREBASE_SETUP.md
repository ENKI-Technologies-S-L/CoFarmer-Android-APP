# Firebase Setup - CoFarmer Android APP

## ⚠️ IMPORTANT - Required Configuration

This project requires a `google-services.json` file to build successfully. This file contains Firebase configuration and **MUST NOT be committed to git**.

## 📋 Required Package Names

The `google-services.json` file must include the following package names:

### Production
- **Package name:** `com.enkitek.cofarmer`
- **Purpose:** Release builds for Play Store / production environments

### Debug
- **Package name:** `com.enkitek.cofarmer.debug`
- **Purpose:** Debug builds for development and testing

### Legacy (Optional - for migration compatibility)
- `io.homeassistant.companion.android`
- `io.homeassistant.companion.android.debug`

## 🔧 Setup Instructions

### 1. Access Firebase Console
Go to [Firebase Console](https://console.firebase.google.com/) and select the **CoFarmer Android APP** project.

### 2. Register Applications
In **Project Settings** (⚙️) → **General** → **Your apps**:

- Ensure these apps are registered:
  - **CoFarmer Android APP - Production** (`com.enkitek.cofarmer`)
  - **CoFarmer Android APP - Dev** (`com.enkitek.cofarmer.debug`)

### 3. Download google-services.json
- Click on **CoFarmer Android APP - Dev**
- Download the `google-services.json` file
- This file will contain configuration for ALL registered package names

### 4. Place the File
Copy `google-services.json` to **both** locations:
```
CoFarmer-Android-APP/
├── app/google-services.json          ← Place here
└── automotive/google-services.json   ← Place here (same file)
```

Both files should be identical.

### 5. Verify Configuration
Open `google-services.json` and verify it contains entries for:
```json
{
  "client": [
    {
      "client_info": {
        "android_client_info": {
          "package_name": "com.enkitek.cofarmer"
        }
      }
    },
    {
      "client_info": {
        "android_client_info": {
          "package_name": "com.enkitek.cofarmer.debug"
        }
      }
    }
  ]
}
```

## 🚫 Security - DO NOT COMMIT

The `google-services.json` file is already listed in `.gitignore` and should **NEVER** be committed to the repository.

**Why?**
- Contains API keys and project credentials
- Different developers may use different Firebase projects
- Keeps sensitive configuration out of version control

## 🔨 Building the Project

After placing `google-services.json` in the correct locations:

```bash
# Clean build
.\gradlew clean

# Build debug variant
.\gradlew assembleFullDebug

# Build all variants
.\gradlew assembleDebug
```

## ✅ Verification

To verify Firebase is configured correctly:
1. Build should complete without errors about missing `google-services.json`
2. Firebase services (FCM, Analytics) should work in the app
3. Git status should NOT show `google-services.json` as modified or untracked

## 📞 Support

If you encounter issues with Firebase configuration:
1. Verify you have the latest `google-services.json` from Firebase Console
2. Check that both `app/` and `automotive/` folders contain the file
3. Ensure the file contains the correct package names
4. Try `.\gradlew clean` before rebuilding

---

**Last updated:** January 2026
