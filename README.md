# Toque

Aplicación Android que reenvía las notificaciones de llamadas VoIP (WhatsApp por ahora) como notificaciones propias, para que relojes inteligentes cuyas apps compañeras (Mi Fitness, Zepp Life, etc.) solo filtran por paquete puedan mostrar únicamente las llamadas sin recibir el spam de chats.

## Estado

Alpha — trabajo en curso. Solo WhatsApp, solo notificación (sin contestar desde el reloj).

## Uso

1. Instala Toque en tu teléfono.
2. Abre la app y concede el permiso de acceso a notificaciones.
3. En la app de tu reloj (Mi Fitness, Zepp Life, Huawei Health, etc.):
   - **Desactiva** las notificaciones de WhatsApp.
   - **Activa** las notificaciones de Toque.
4. Listo: las llamadas VoIP de WhatsApp vibrarán en tu reloj, pero los chats no.

## Compilar

```
./gradlew assembleDebug
```

## Instalar

```
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Capturar logs de descubrimiento

Con la app instalada y el permiso de acceso a notificaciones concedido:

```
adb logcat -s Toque:D
```

Genera eventos en WhatsApp (llamada entrante, videollamada, mensaje, grupo, estado) y observa las líneas con tag `Toque/Discovery`.

## Comparación con alternativas

| Proyecto | OSS | Zero-config | Propósito específico | Notas |
|---|---|---|---|---|
| **Toque** | Si (GPL-3.0) | Si | VoIP → wearable notification-only | Pequeño, sin dependencias extras |
| MacroDroid | No | No | Automatización general | Requiere configurar macro manualmente |
| FilterBox | No | No | Filtrado general de notificaciones | Tiene repost como llamada, pero es de pago |
| Notify for Amazfit (OneZeroBit) | No | Si | Companion app completa | Soporta muchas apps, pero cerrado y de pago |
| Filter-Notification-For-Smart-Band | Si | No | Mi Band 4 específico | Abandonado |
| NotiFilter (BURG3R5) | Si | No | Silenciar/throttle | No reenvía notificaciones |
| Gadgetbridge | Si (AGPL) | No | Companion genérico | Filtrado por canal, pero no soporta todos los relojes (ej. Xiaomi Mi Watch 2020) |

## Licencia

GPL-3.0 — ver [LICENSE](LICENSE).
