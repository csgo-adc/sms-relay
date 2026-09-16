# Automated signed releases

GitHub Actions always builds an APK artifact. To have it create an installable signed APK and attach it to a GitHub Release whenever you push a tag such as `v1.0.1`, add these repository Actions secrets under **Settings → Secrets and variables → Actions**:

| Secret | Value |
| --- | --- |
| `SMS_RELAY_KEYSTORE_BASE64` | Base64-encoded contents of your private `.p12` signing key |
| `SMS_RELAY_KEYSTORE_PASSWORD` | Signing-key store password |
| `SMS_RELAY_KEY_ALIAS` | `sms-relay` |
| `SMS_RELAY_KEY_PASSWORD` | Signing-key password |

The signing key and passwords must remain private. Never commit them to this repository or paste them into issues, release notes, or source files.

Without these secrets, the workflow still builds an unsigned release APK as a downloadable Actions artifact, but Android cannot install that APK as a production release.
