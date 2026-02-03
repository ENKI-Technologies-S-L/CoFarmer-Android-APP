# CoFarmer - Google Play Store Submission Guide

## 📦 Generated Files

### AAB (Android App Bundle) - Para Play Store
```
Ubicación: app/build/outputs/bundle/fullRelease/app-full-release.aab
Tamaño: ~31 MB
Firmado: ✅ Sí (cofarmer-release.keystore)
```

### APK Release - Para Testing/Distribución Directa
```
Ubicación: app/build/outputs/apk/full/release/app-full-release.apk
```

### Keystore de Producción
```
Archivo: cofarmer-release.keystore
Alias: cofarmer
Contraseña Store: EnkiTek_CoFarmer_Prod_2026
Contraseña Key: EnkiTek_CoFarmer_Prod_2026
Validez: 10,000 días (~27 años)

Certificado:
  CN=CoFarmer
  OU=Mobile
  O=ENKI Technologies S.L.
  L=Barcelona
  ST=Barcelona
  C=ES
```

⚠️ **IMPORTANTE**: Guarda el keystore y contraseñas en lugar seguro. Sin ellos NO podrás actualizar la app en Play Store.

---

## 🔐 Declaración de Permisos Sensibles

Google Play Console requiere explicaciones para los siguientes permisos. Copia estas declaraciones al formulario:

### 1. ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION
**Categoría**: Location

**Propósito**: 
> CoFarmer uses location to enable farm automation based on geofencing. When a farmer arrives at or leaves a farm location, the app can trigger automations in the connected CoFarmer Hub (e.g., turn on irrigation systems, open gates, activate sensors). Location is only collected when the user explicitly enables location-based sensors or automations.

**Funcionalidad en la app**:
- Geofencing para automatizaciones agrícolas
- Sensor de ubicación para reportar posición al hub
- Geocodificación para mostrar nombre de ubicación

---

### 2. ACCESS_BACKGROUND_LOCATION
**Categoría**: Background Location

**Propósito**:
> Background location access is essential for farm automation to work when the app is not actively in use. Farmers need their automation triggers (geofences) to function while working in fields without checking their phones. For example: automatically enabling irrigation when leaving the farm, or receiving alerts when entering a specific zone. Without background location, core automation features would not function.

**Video demostrativo requerido**: Debes grabar un video mostrando:
1. Configurar una zona de geofencing
2. Salir de la app
3. Mostrar que la automatización se activa al cruzar la zona

---

### 3. CAMERA
**Categoría**: Camera

**Propósito**:
> The camera permission is used for two features: (1) Scanning QR codes during initial hub setup - farmers can scan a QR code displayed on their CoFarmer Hub to quickly connect without manual URL entry, (2) Scanning NFC tags placed around the farm to trigger specific automations. Camera access is only activated when the user explicitly initiates QR scanning.

---

### 4. RECORD_AUDIO
**Categoría**: Microphone

**Propósito**:
> Audio recording is used exclusively for the voice assistant feature (Assist). Farmers can use voice commands to control their farm automation hands-free while working in the fields. For example: "Turn on the greenhouse ventilation" or "What's the soil moisture level?" Audio is processed locally or sent to the user's self-hosted CoFarmer Hub - never to third-party servers.

---

### 5. CALL_PHONE
**Categoría**: Phone

**Propósito**:
> The phone permission allows the app to initiate calls directly from automation actions. For example, a farmer can set up an automation that calls an emergency contact if critical conditions are detected (e.g., temperature sensor detects freezing conditions in a greenhouse). This is an optional feature that users must explicitly configure.

---

### 6. BLUETOOTH_SCAN / BLUETOOTH_CONNECT
**Categoría**: Nearby Devices (Bluetooth)

**Propósito**:
> Bluetooth permissions are used to detect and communicate with Bluetooth Low Energy (BLE) agricultural sensors placed around the farm. These may include soil moisture sensors, temperature probes, or livestock tracking tags. The app reports detected devices to the CoFarmer Hub for monitoring and automation purposes.

---

### 7. NFC
**Categoría**: NFC

**Propósito**:
> NFC is used to scan NFC tags placed around the farm. Farmers can tap their phone on a tag to trigger specific automations or quickly access relevant dashboard information. For example: tap a tag on a greenhouse door to see interior conditions and control ventilation.

---

### 8. REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
**Categoría**: Battery Optimization Exemption

**Propósito**:
> Battery optimization exemption ensures that critical farm alerts and sensor updates are delivered reliably. Agricultural operations often require real-time notifications (frost warnings, water level alerts, security triggers). Users are prompted to grant this permission only if they enable sensors or notifications that require persistent background operation.

---

### 9. SCHEDULE_EXACT_ALARM
**Categoría**: Alarms & Reminders

**Propósito**:
> Exact alarms are used for time-based agricultural automations. For example: scheduled irrigation at specific times, sending sensor updates at regular intervals, or triggering reminders for farm tasks. This ensures automations run precisely when needed regardless of device doze state.

---

### 10. FOREGROUND_SERVICE_LOCATION
**Categoría**: Foreground Service

**Propósito**:
> Foreground service with location is used during active navigation to farm sites or when continuous location tracking is required for precision agriculture applications. A visible notification is always shown to the user when this service is active.

---

### 11. POST_NOTIFICATIONS
**Categoría**: Notifications

**Propósito**:
> Notifications deliver critical alerts from the CoFarmer Hub: frost warnings, security alerts, irrigation status, sensor anomalies, and automation confirmations. Farmers rely on timely notifications to respond to field conditions. Users control which notification types they receive through the app settings.

---

## 📝 Ficha de Play Store

### Nombre de la App
```
CoFarmer - Smart Farm Automation
```

### Descripción Corta (80 caracteres)
```
Connect your smart farm hub. Monitor sensors. Automate everything.
```

### Descripción Completa
```
CoFarmer transforms your smartphone into a powerful farm management tool. Connect to your self-hosted CoFarmer Hub to monitor agricultural sensors, control irrigation systems, and automate your entire farming operation.

🌱 KEY FEATURES

📊 Real-time Monitoring
• View all your farm sensors in one dashboard
• Track soil moisture, temperature, humidity, and more
• Get instant alerts for critical conditions

🤖 Smart Automation
• Create automations based on location, time, or sensor data
• Trigger actions when arriving at or leaving farm zones
• Schedule irrigation and climate control

🎤 Voice Control
• Control your farm hands-free with voice commands
• Works with your self-hosted voice assistant
• Perfect for when your hands are full

📱 Widgets & Quick Actions
• Home screen widgets for instant sensor readings
• Quick Settings tiles for common actions
• NFC tags for tap-to-trigger automations

🔒 Privacy First
• All data stays on YOUR server
• No cloud dependency - works locally
• You own your farm data

🌐 Works Offline
• Core features work without internet
• Perfect for remote farm locations
• Syncs when connection is available

REQUIREMENTS
• CoFarmer Hub (self-hosted server)
• Android 6.0 or higher

ABOUT COFARMER
CoFarmer is developed by Enkitek, specialists in agricultural technology solutions. Our mission is to make smart farming accessible to farmers of all sizes.

🔗 cofarmer.enkitek.eu
📧 support@enkitek.eu
```

### Categoría
```
Tools (Herramientas)
```
Alternativa: Productivity (Productividad)

### Etiquetas/Tags
```
farm, agriculture, automation, smart home, IoT, sensors, irrigation, monitoring
```

---

## 🔗 URLs Requeridas

### Política de Privacidad (OBLIGATORIO)
```
https://cofarmer.enkitek.eu/privacy-policy.html
```

### Términos de Servicio
```
https://cofarmer.enkitek.eu/terms-of-service.html
```

### Sitio Web
```
https://cofarmer.enkitek.eu
```

### Email de Soporte (OBLIGATORIO)
```
support@enkitek.eu
```

---

## 📸 Assets Gráficos Requeridos

### 1. Icono de la App (512x512 PNG)
```
Ubicación: cofarmer-icon-512x512-green.png
```

### 2. Feature Graphic (1024x500 PNG)
Necesitas crear una imagen promocional. Sugerencia:
- Fondo verde degradado (#04D288 → #00B894)
- Logo de CoFarmer centrado
- Texto: "Smart Farm Automation"

### 3. Screenshots (mínimo 2, máximo 8)
Tamaños recomendados:
- Teléfono: 1080x1920 px (o 1080x2340 para pantallas altas)
- Tablet 7": 1200x1920 px
- Tablet 10": 1600x2560 px

Screenshots sugeridos:
1. Dashboard principal con sensores
2. Lista de automatizaciones
3. Configuración de geofencing
4. Control de dispositivos
5. Widgets en home screen
6. Asistente de voz

---

## ⚙️ Configuración de Play Console

### Clasificación de Contenido
Completa el cuestionario indicando:
- No contiene violencia
- No contiene contenido sexual
- No contiene lenguaje inapropiado
- No permite compras in-app
- No recopila datos personales en servidores propios (los datos van al servidor del usuario)

Resultado esperado: **PEGI 3** / **Everyone**

### Target Audience
```
Edad: 18+ (orientado a profesionales agrícolas)
No dirigido a niños
```

### Data Safety

| Tipo de Dato | ¿Se recopila? | ¿Se comparte? | ¿Obligatorio? | Propósito |
|--------------|---------------|---------------|---------------|-----------|
| Location | Sí | No | Opcional | Automatización, geofencing |
| Device ID | Sí | No | Obligatorio | Identificación del dispositivo ante el hub |
| Crash logs | Sí | No | Opcional | Mejora de la app (si el usuario lo habilita) |

**Nota importante**: Los datos van al servidor auto-hospedado del usuario, no a servidores de Enkitek.

### Países de Distribución
Recomendado comenzar con:
- España
- Portugal  
- Francia
- Italia
- Alemania
- México
- Argentina
- Colombia
- Chile

### Precios
```
Gratis
Sin compras in-app
Sin anuncios
```

---

## 📋 Checklist Pre-Publicación

- [ ] AAB firmado con keystore de producción
- [ ] Keystore guardado en lugar seguro (con backup)
- [ ] Política de privacidad publicada online
- [ ] Términos de servicio publicados online
- [ ] Email de soporte configurado
- [ ] Icono 512x512 preparado
- [ ] Feature graphic 1024x500 preparado
- [ ] Mínimo 2 screenshots por tipo de dispositivo
- [ ] Video demostrativo para background location (obligatorio)
- [ ] Cuestionario de clasificación completado
- [ ] Data Safety form completado
- [ ] Declaraciones de permisos sensibles escritas

---

## 🚀 Pasos para Subir a Play Store

1. **Accede a Play Console**: https://play.google.com/console

2. **Crear nueva aplicación**:
   - Nombre: CoFarmer - Smart Farm Automation
   - Idioma: Español (España) o English
   - Tipo: App
   - Gratis

3. **Configurar ficha de Play Store**:
   - Subir icono, screenshots, feature graphic
   - Completar descripciones
   - Añadir URLs

4. **Política de la app**:
   - Completar cuestionario de contenido
   - Completar Data Safety
   - Añadir política de privacidad

5. **Declaraciones de permisos**:
   - Ir a "App content" → "Sensitive app permissions"
   - Completar declaración de Background Location
   - Subir video demostrativo

6. **Crear release**:
   - Ir a "Release" → "Production"
   - Subir el AAB
   - Completar notas de versión

7. **Enviar para revisión**

---

## 📞 Contacto Play Store

Si hay problemas con la revisión:
- Email de desarrollador: soporte@enkitek.eu
- Responder a rechazos dentro de 7 días
- Usar el formulario de apelación si es necesario

---

## 🔄 Actualizaciones Futuras

Para actualizar la app:

1. Incrementar `VERSION_CODE` en `app/build.gradle.kts`
2. Actualizar `VERSION_NAME` si es apropiado
3. Ejecutar: `.\gradlew bundleFullRelease`
4. Subir nuevo AAB a Play Console
5. Usar **SIEMPRE** el mismo keystore

---

*Documento generado el 27 de Enero de 2026*
*Versión: 1.0.0 (Version Code: 2)*
