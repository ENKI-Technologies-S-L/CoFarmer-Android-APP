# Rebranding: CoFarmer → CoFarmer Assistant

Fecha: 2026-11-16 · Rama: `feature/cofarmer-assistant-rebrand` (sale de `release/2026.12`)

## Por qué

La app pasa a llamarse **CoFarmer Assistant** y adopta la nueva identidad visual de CoFarmer: el logo del eslabón «CO» con degradado, la tipografía Sora y la nueva paleta. Los recursos de marca están en `Branding_CoFarmer/`.

## Objetivos

- Nombre visible **CoFarmer Assistant** en el icono, el splash, la bienvenida, «Acerca de» y la ficha de Play.
- Icono de producción nuevo: el eslabón «CO» con degradado sobre fondo blanco.
- ~~Icono de la versión de desarrollo (`.debug`): el robot «DEV».~~ **Cambio (2026-11-16):** la versión debug usa el mismo icono que producción, para poder ver el logo real en el móvil.
- Paleta actualizada al degradado del logo.
- Sora en títulos y momentos de marca. El texto corrido sigue en Roboto.

## No objetivos

- **El mockup del panel oscuro** (`mockup-cofarmer.png`) se ignora por decisión del usuario. Además, ese panel es el frontend web que sirve el hub, no una pantalla de la app.
- Sora en el texto corrido (opción A, descartada).
- Cambiar «CoFarmer Hub», «CoFarmer Cloud» o los mensajes que hablan del servidor.
- Incluir esto en la versión 2026.12. Va en la versión siguiente.

## Antiobjetivos (lo que sería un fracaso aunque funcione)

- Un nombre cortado de forma fea bajo el icono en el launcher.
- Un icono con el «DEV» o el eslabón recortado por la máscara adaptativa (círculo o squircle).
- Textos que se cortan o se solapan en los ajustes o en la bienvenida por culpa de Sora.
- Mezclar «CoFarmer» y «CoFarmer Assistant» al azar dentro de una misma pantalla.

## Enfoques considerados

| | Enfoque | Trade-off |
|---|---|---|
| A | Cambio completo con Sora en toda la UI nativa (Compose y XML) | Uniformidad total, pero toca dos sistemas de estilos y es arriesgado (el intento con Lato de enero se abandonó) |
| **B ✅** | **Sora solo en títulos y momentos de marca, Roboto en el cuerpo** | Marca reconocible con poco riesgo; tipografía no del todo uniforme |
| C | Sin Sora: solo icono, splash, colores y nombre | Lo más rápido, pero pierde parte de la identidad |

**Elegido: B.** Es el uso habitual de una fuente geométrica de display como Sora.

## Diseño

### 1. Nombre (decidido por el usuario: «donde se habla de la app»)

- `app_name` = `CoFarmer Assistant` en `common/src/main/res/values/strings.xml`. El nombre bajo el icono y el de Wear salen de aquí.
- **Regla:** «CoFarmer» pasa a «CoFarmer Assistant» cuando el texto se refiere **a la app**. Por ejemplo, «Allow CoFarmer to send notifications», «CoFarmer does not have access to run in the background», «Welcome to CoFarmer» o «Install CoFarmer on your phone».
- **No cambian** las menciones al hub, al servidor o a la plataforma: «CoFarmer Hub», «CoFarmer Cloud», «CoFarmer URL», «Unable to connect to CoFarmer», «CoFarmer certificate», Documentation/Forum/Discord, zonas de CoFarmer…
- El cambio se aplica en inglés y en todos los idiomas que ya tengan esa cadena (es, fr, de, pt, ar, zh, hi, ko, ja).
- Los textos que ahora están escritos directamente en el código del splash («COFARMER», «Control your farm from anywhere», el `contentDescription` y «by ENKITEK») pasan a recursos de texto, para que se puedan traducir (*decisión mía*).
- `fastlane/metadata/android/en-US/title.txt` = `CoFarmer Assistant`. El nombre de la ficha en Play Console lo cambia el usuario a mano al publicar.
- **Riesgo:** con 18 caracteres, el nombre puede salir cortado en el launcher. Se comprobará en el Redmi Note 8 Pro (MIUI). Si se corta, lo consulto con el usuario antes de buscar otra solución.

### 2. Colores

| Recurso | Ahora | Nuevo |
|---|---|---|
| `cofarmer_primary` / `gradient_start` | `#04D288` | `#00C389` |
| `cofarmer_secondary` / `gradient_end` | `#0066CC` | `#1E6BD6` |
| `cofarmer_accent` | `#03B799` | `#0F97AF` (punto medio del degradado) |
| `colorPreferenceSuggestion` | `#1F04D288` | `#1F00C389` |

- Se aplica a `common/src/main/res/values/colors.xml` (y `values-night` si redefine estos colores), a los colores de Compose (`HAColors.kt` y `CoFarmerGreen`…) y a `wear/src/main/res/values/colors.xml` y `wear/.../theme/Color.kt`.
- **Contraste:** el blanco sobre `#00C389` da 2,3:1. Es igual de bajo que ahora, que da 2,0:1 con `#04D288`, así que no empeora. Mejorarlo queda fuera de este alcance, pero se apunta como pendiente (*decisión mía*).

### 3. Icono de producción

- Adaptive icon: fondo blanco (`colorLauncherBackground` = `#FFFFFF`) y el eslabón de `logo-cofarmer-vector.svg` convertido a VectorDrawable con el degradado `#00C389` → `#1E6BD6`. Va centrado dentro de la zona segura (66 dp del lienzo de 108 dp).
- Icono monocromo: el eslabón en un solo color (para los iconos temáticos de Android 13 y posteriores).
- Se regeneran los PNG antiguos (mdpi…xxxhdpi, para API 23–25), además de `ic_launcher_round`, el foreground de Assist (`ic_assist_launcher_foreground`) y los iconos de Wear (`wear/src/main/res/...ic_launcher*`).
- **Nota:** `minSdk` es 23 y los degradados en VectorDrawable requieren API 24. En API 23 se usan los PNG del mipmap. Hay que comprobar que ningún vector con degradado se infle por la vía nativa en API 23.

### 4. Icono debug: el robot «DEV» — DESCARTADO

**Cambio pedido por el usuario tras verlo en el móvil:** la versión debug usa el icono de producción (eslabón «CO»). El robot queda solo como recurso en `branding-assets/cofarmer/cofarmer-robot-dev.png`. Texto original:


- Solo en `app/src/debug/res/` (`ic_launcher`, `ic_launcher_round` y sus variantes de densidad).
- Fondo: el azul marino del propio PNG. Delante, el robot escalado para que **la ilustración entera, incluida la etiqueta «DEV», quede dentro de la zona segura**.
- Los PNG/WebP antiguos se generan a partir de `Robot codificador de bucles con DEV.png`.
- El icono «DevPlayground» que ya existe se queda como está.

### 5. Tipografía Sora (enfoque B)

- Se añaden `sora_semibold.ttf` y `sora_bold.ttf` (y `sora_medium.ttf` si hace falta) en `common/src/main/res/font/`, junto con la licencia `OFL.txt` (`branding-assets/fonts/OFL.txt`).
- **Compose:** en `HATextStyle.kt`, los estilos de título y encabezado usan la familia Sora. Los de cuerpo y etiquetas siguen en Roboto.
- **XML:** solo los títulos de la toolbar y las categorías de preferencias. Las filas de ajustes se quedan en Roboto.
- **Splash y bienvenida:** el nombre en Sora.

### 6. Splash (`BrandingSplashScreen.kt`) y bienvenida

```
            [eslabón CO con degradado, 120dp]

            CoFarmer Assistant          ← Sora Bold; "CoFarmer" en #333333,
                                          "Assistant" en Sora SemiBold con el degradado o #0F97AF
       Control your farm from anywhere  ← Roboto, gris

                 by ENKITEK
```

- `splash_logo_cofarmer` pasa a ser el nuevo eslabón. `splash_text_cofarmer` y `splash_tagline` se eliminan si dejan de usarse.
- En modo oscuro, «CoFarmer» va en blanco.
- La bienvenida usa el mismo logo y el título «Welcome to CoFarmer Assistant» en Sora.

### 7. Recursos de marca en el repo

- Los originales de `Branding_CoFarmer/` se mueven a `branding-assets/cofarmer/`: logos, SVG, el robot y la licencia de la fuente. `mockup-cofarmer.png` y `og-cofarmer.png` se dejan como referencia. El zip de la fuente no se sube; solo los TTF usados (*decisión mía*).

## Verificación

- `:app:assembleFullDebug`, `:wear:assembleDebug` y el lint de `common` sin errores.
- Tests de screenshot de Compose: actualizar las referencias si cambian por las fuentes y los colores, y revisar a ojo las diferencias.
- Instalar en el Redmi Note 8 Pro (Android 11, MIUI) y hacer capturas de: launcher (icono debug y nombre), splash, bienvenida, ajustes y «Acerca de», **en inglés y en español**.
- El icono de producción no se puede ver en un móvil sin firmar la build de release, así que se renderiza a PNG con la máscara circular y la squircle para revisarlo.

## Lo que queda a criterio de quien lo implemente

- La lista exacta de cadenas «de la app» frente a «del hub», aplicando la regla del punto 1. Se revisa en el diff.
- El tamaño exacto del logo en el splash y en la bienvenida, y el tratamiento final de «Assistant» (degradado o color plano).
- Las herramientas para convertir el SVG a VectorDrawable y para generar los PNG/WebP.
