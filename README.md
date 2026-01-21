# CoFarmer - Agricultural IoT Companion for Android

[![Build Status](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP/actions/workflows/onPush.yml/badge.svg)](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP/actions/workflows/onPush.yml)  
[![Play Store](https://img.shields.io/badge/Play%20Store-Download-blue?logo=google-play)](https://play.google.com/store/apps/details?id=com.enkitek.cofarmer)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?logo=github)](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP)

Welcome to **CoFarmer** - the agricultural IoT companion app for monitoring and managing your farm installations. Built on proven open-source technology, CoFarmer brings professional-grade monitoring capabilities to farms of all sizes: greenhouses, open fields, vertical farms, vineyards, and more.

---

## Features

- **Monitor Your Installation**: Real-time sensor data from climate systems, soil sensors, HVAC equipment, and actuators.
- **Local-First Architecture**: Your CoFarmer hub runs on your farm. Data stays local unless you explicitly configure remote access.
- **Native Android Experience**: Widgets for quick status checks, background sensors for continuous monitoring, and reliable notifications.
- **Works Offline**: Designed for agricultural environments where connectivity can be unreliable. Core functionality works without internet.
- **Open Source**: Built on transparent, auditable technology. Full control over your agricultural data.

## Get the app

- **[Download from the Play Store](https://play.google.com/store/apps/details?id=com.enkitek.cofarmer)**  
  Available now on Google Play for Android devices.
- **[View on GitHub](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP)**: Access the latest code and contribute.

## Documentation

For detailed setup instructions, sensor information, and troubleshooting:
- **[CoFarmer Documentation](https://docs.cofarmer.io)** *(In development)*
- **[Fork Information](COFARMER_FORK.md)**: What's different from the original Home Assistant app
- **[Upstream Sync Guide](UPSTREAM_SYNC.md)**: How we keep up with upstream improvements
- **[Agro-Tech Terminology](AGRO_TECH_TERMINOLOGY.md)**: Understanding agricultural IoT concepts in CoFarmer

## Report a bug or request a feature

Found an issue or have an idea for improvement? Let us know!  

- **[Open a Bug Report](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP/issues/new?template=Bug_report.md)**  
- **[Request a Feature](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP/issues/new?template=feature_request.md)**  

Your feedback helps us build better tools for agricultural professionals.

## Contributing

We welcome contributions from developers and agricultural professionals. Whether you're reporting bugs, adding features, or improving documentation, your help makes CoFarmer better.

### Getting started

1. Read the [Developer Guide](https://github.com/ENKI-Technologies-S-L/CoFarmer-Android-APP/wiki/Developer-Guide) *(coming soon)*
2. Fork the repository and create a branch for your changes.
3. Submit a pull request with a clear description of your changes.

## Architecture & Technology

- **Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Hilt
- **State Management**: Flow & StateFlow
- **Database**: Room
- **Networking**: Retrofit + OkHttp

## About the Fork

CoFarmer is built on the proven foundation of [Home Assistant](https://github.com/home-assistant/android), adapted specifically for agricultural IoT. See [COFARMER_FORK.md](COFARMER_FORK.md) for detailed information about our modifications and [UPSTREAM_SYNC.md](UPSTREAM_SYNC.md) for how we keep up with upstream improvements.
