# CoFarmer - Agro-Tech Terminology & Tone Guide

This document defines the vocabulary, tone, and messaging for CoFarmer to ensure consistent agricultural context across all user-facing content.

## Core Terminology Mapping

### Physical Infrastructure

| Smart Home Term | CoFarmer Term | Context |
|-----------------|---------------|---------|
| Smart Home | Agricultural Installation | Primary physical deployment |
| Home | Farm / Installation | Physical location |
| Room | Zone / Greenhouse / Field | Specific area within farm |
| Device | Sensor / Actuator / Equipment | Physical hardware |
| Hub | CoFarmer Hub / Local Hub | Central control unit |

### Operations & Monitoring

| Smart Home Term | CoFarmer Term | Context |
|-----------------|---------------|---------|
| Automation | Monitoring & Control | Tracking and managing farm operations |
| Control the home | Monitor and manage your installation | Primary user goal |
| Smart home enthusiast | Agricultural operator / Farm technician | User persona |
| Away from home | Away from the farm | Remote access scenario |
| At home | On-site / On the farm | Local network scenario |

### Technology & Features

| Smart Home Term | CoFarmer Term | Context |
|-----------------|---------------|---------|
| Smart devices | Agricultural sensors & actuators | What the app controls |
| Connected home | Connected farm | Networked system |
| Home automation | Agricultural monitoring | Core functionality |
| Smart Assistant | CoFarmer Intelligence | Future AI features |
| Scenes | Profiles / Strategies | Predefined configurations |

### Data & Privacy

| Smart Home Term | CoFarmer Term | Context |
|-----------------|---------------|---------|
| Private smart home | Secure agricultural data | Data protection promise |
| Keep your data local | Data stays on your farm | Local-first architecture |
| Control your data | Own your farm data | User empowerment |

---

## Tone & Voice Principles

### ✅ DO

- **Professional & Calm**: Agricultural operations are serious business
- **Technical but Accessible**: Farmers aren't IT specialists, but they're practical problem-solvers
- **Honest about Capabilities**: Don't overpromise; explain what the system can do
- **Practical Examples**: Reference real farm scenarios (temperature monitoring, humidity alerts)
- **Respect Local Networks**: Emphasize that the hub works offline, locally
- **Inclusive Language**: Acknowledge different farm types (greenhouses, open fields, hydroponics, etc.)

### ❌ DON'T

- **Enthusiastic Marketing**: "Amazing!", "Incredible!" - sounds like consumer tech
- **Home Automation References**: Avoid "smart home", "IoT", "connected devices" (too vague)
- **Consumer Comparisons**: Don't compare to Alexa, Google Home, etc.
- **Overly Technical**: Avoid WebSocket, mDNS, reverse proxy - use simpler terms
- **False Promises**: "Real-time alerts" when they might be delayed on poor connectivity
- **Emotional Language**: Avoid "bring your home to life" - farms are already alive!

---

## Use Case Scenarios

### Scenario 1: Greenhouse Climate Control

**Wrong (Consumer):**
> "CoFarmer lets you control your home climate from anywhere!"

**Right (Agricultural):**
> "Monitor greenhouse temperature and humidity. CoFarmer alerts you when conditions drift outside optimal ranges, helping you maintain consistent growing conditions even when you're away from the greenhouse."

---

### Scenario 2: Sensor Data Collection

**Wrong (Consumer):**
> "Connect all your smart devices and see data in real-time!"

**Right (Agricultural):**
> "CoFarmer collects data from your agricultural sensors - soil moisture, nutrient levels, climate conditions. Access readings from your phone when you need them, or set up automated alerts for important thresholds."

---

### Scenario 3: Remote Monitoring

**Wrong (Consumer):**
> "Control your home from anywhere in the world!"

**Right (Agricultural):**
> "When connected to your local network, CoFarmer lets you check on your installation from anywhere. For reliable remote access, consider setting up secure external connectivity (VPN/cloud options available)."

---

### Scenario 4: Local-First Architecture

**Wrong (Consumer):**
> "Your data is private because it's encrypted!"

**Right (Agricultural):**
> "CoFarmer hub runs entirely on your farm. Your sensor data never leaves your network unless you explicitly configure external access. This keeps your operational data secure and means the system works even without internet connectivity."

---

## Feature Descriptions

### For Onboarding

**Current (Generic):**
> "Connect to your CoFarmer hub"

**Agro-Tech:**
> "Connect to your CoFarmer hub installed at your farm location. This local hub collects data from all your sensors and equipment."

---

### For Sensors

**Current (Generic):**
> "Temperature sensor: Measures ambient temperature"

**Agro-Tech:**
> "Temperature: Monitor greenhouse or field conditions. Critical for detecting frost warnings or heat stress in crops."

---

### For Permissions

**Current (Generic):**
> "Location access for network detection"

**Agro-Tech:**
> "CoFarmer needs to detect your farm's local network to ensure reliable connectivity. This helps the app automatically connect when you're on-site and improves performance."

---

### For Settings

**Current (Generic):**
> "Home network settings"

**Agro-Tech:**
> "Local network configuration: Set up your farm's Wi-Fi network for optimal connectivity and alerts."

---

## Examples by App Section

### Welcome Screen

**Current:**
```
"Welcome to CoFarmer Companion"
"Control your Smart Home from anywhere"
```

**Agro-Tech:**
```
"Welcome to CoFarmer"
"Monitor and manage your agricultural installation"
```

---

### Onboarding - Discovery

**Current:**
```
"Searching on home network..."
"Looking for servers on your home network"
```

**Agro-Tech:**
```
"Scanning local network..."
"Looking for your CoFarmer hub on the local network"
```

---

### Onboarding - Manual Entry

**Current:**
```
"What is your Home Assistant address?"
"Enter your CoFarmer hub address"
```

**Agro-Tech:**
```
"What is your CoFarmer hub address?"
"Enter the IP address or hostname of your hub (e.g., 192.168.1.50 or cofarmer.local)"
```

---

### Location Permission

**Current:**
```
"Location access for network detection"
"We need your location to detect your home network"
```

**Agro-Tech:**
```
"Network detection"
"CoFarmer needs to detect your farm's local network. Android requires location permission to read Wi-Fi network information. We never track your position."
```

---

### Notifications

**Current:**
```
"Stay connected to your home"
"Get alerts when something changes"
```

**Agro-Tech:**
```
"Stay informed about your installation"
"Receive alerts for important events: temperature threshold breaches, system errors, operational status changes"
```

---

## Sensor Description Examples

### Temperature Sensor
**Before:**
> "Monitors ambient temperature"

**After:**
> "Tracks greenhouse or field temperature. Alert on frost danger or excessive heat to protect crops."

### Humidity Sensor
**Before:**
> "Measures relative humidity"

**After:**
> "Monitors air humidity levels. Critical for disease prevention and optimal growing conditions in controlled environments."

### Soil Moisture Sensor
**Before:**
> "Detects moisture in soil"

**After:**
> "Tracks soil moisture to optimize irrigation. Reduce water waste and prevent crop stress."

### Battery Level
**Before:**
> "Shows device battery percentage"

**After:**
> "Sensor battery level. Alerts help prevent missed readings from dead batteries."

---

## Color & Visual Language

### Color Symbolism

| Color | Meaning |
|-------|---------|
| Green (#04D288) | Growth, healthy conditions, go |
| Blue (#0066CC) | Trust, water, data flow |
| Teal (#03B799) | Nature, sustainability, balance |

---

## Frequently Used Phrases

### ✅ Use These

- "Your installation"
- "Agricultural sensors"
- "Local hub"
- "Farm network"
- "Monitoring and control"
- "Operational data"
- "Climate conditions"
- "Sensor readings"
- "Alert threshold"
- "On-site" / "Off-site"

### ❌ Avoid These

- "Your home"
- "Smart devices"
- "Home network"
- "Smart home"
- "Automation"
- "Personal data" (use "operational data")
- "Smart temperature"
- "Device status"
- "Trigger"
- "Away from home"

---

## Tone Examples

### Alert Message

**Bad:**
```
"Your home temperature is too high!"
```

**Good:**
```
"Greenhouse temperature exceeded 32°C. Check ventilation systems if no action has been taken."
```

### Success Message

**Bad:**
```
"Successfully connected to your smart home!"
```

**Good:**
```
"Connected to CoFarmer hub. Your sensors are reporting data normally."
```

### Error Message

**Bad:**
```
"Connection failed. Try again later."
```

**Good:**
```
"Can't reach the CoFarmer hub. Check that your hub is powered on and connected to the network."
```

---

## Accessibility Notes

- Use simple, clear language (avoid jargon)
- Explain technical terms when first mentioned
- Provide context for why permissions/settings matter
- Example: "We need camera access **so you can** scan the QR code on your hub"

---

## Version History

| Date | Version | Changes |
|------|---------|---------|
| 2026-01-21 | 1.0 | Initial agro-tech terminology guide |

