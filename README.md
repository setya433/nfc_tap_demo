# NFC Tap Demo

This project is a Flutter-based NFC application that integrates with a Laravel backend for authentication and data retrieval.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- Flutter (latest stable version)
- Android Studio or VS Code
- Dart SDK
- Python 3.x (for backend testing)
- A compatible NFC reader (e.g., Identiv uTrust 3700 F CL Reader)
- An NFC-compatible Android device
- Git for version control

## Setup Instructions

### 1. Clone the Repository
```sh
$ git clone https://github.com/yourusername/nfc_tap_demo.git
$ cd nfc_tap_demo
```

### 2. Install Dependencies
```sh
$ flutter pub get
```

### 3. Configure API Token
Edit `nfc_service.dart` and replace the placeholder token with your actual token:
```dart
String token = "your_api_token_here";
```

### 4. Run the Flutter App
```sh
$ flutter run
```

## NFC Integration
The app uses `flutter_nfc_kit` for NFC communication. It sends an API request to fetch the user’s `member_id`, then writes this ID to an NFC tag in NDEF format.

### Expected NFC Behavior:
- The app detects an NFC tag.
- It fetches `member_id` from the backend.
- The ID is written to the NFC tag.
- The data can be read and verified.

## Android HCE (Host Card Emulation)
The `MyHostApduService.java` class handles APDU communication, ensuring that the NFC reader can recognize and authenticate the device.

### Log Debugging
If the app fails to respond to NFC events, check the logs:
```sh
$ adb logcat -s MyHostApduService
```
EXSECUSION

- open HCE.py
- run flutter project
- tap mobile to nfc reader and run python HCE.py file

DONE
