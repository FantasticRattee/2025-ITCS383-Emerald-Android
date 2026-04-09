# 🏣 Post Office Online Shipping System (Android)
### 2025-ITCS383 · Arai-Kor-Dai / Emerald

> A native Android application that allows customers to prepare parcels and letters before visiting the post office — register, create shipments, pay online, and track deliveries right from their mobile devices.

---

## 🔑 Demo Credentials

| Field    | Value                  |
|----------|------------------------|
| Username | `SomchaiJ@gmail.com`   |
| Password | `Pass1234`             |

---

## 👥 Team Members

| Student ID | First Name      | Last Name          |
|------------|-----------------|--------------------|
| 6688139   | Naruebordint          | Veangnont    |
| 6688141    | Rattee  | Watperatam             |
| 6688155   | Nattaphong      | Jullayakiat           |
| 6688164   | Veerakron      | No-in       |
| 6688172    | Wirunya         | Kaewthong         |
| 6688239   | Piyada        |  Chalermnontakarn     |


---

## ✨ Main Features

**For Customers:**
- Register with identity verification
- Secure login using email and password
- Create shipping orders directly from the app (select parcel type, size, and weight)
- Enter receiver information and delivery address
- Automatic price calculation
- Pay online and generate a mobile shipping label
- Track parcel status in real-time
- View past shipment history

---

## 🗂️ Project Structure

The Android app is built using modern Android development practices (Kotlin, likely Jetpack Compose for UI, and Retrofit for networking).

```text
2025-ITCS383-EMERALD-ANDROID/
├── app/
│   ├── src/main/
│   │   ├── java/com/emerald/postoffice/
│   │   │   ├── data/
│   │   │   │   ├── api/                 # Networking (ApiService.kt, RetrofitClient.kt)
│   │   │   │   ├── model/               # Data Classes (Notification, Shipment, User)
│   │   │   │   ├── SessionManager.kt    # Local session/auth handling
│   │   │   │   └── ThaiPostalCodes.kt   # Postal code utility
│   │   │   ├── navigation/
│   │   │   │   └── AppNavigation.kt     # App routing/navigation graph
│   │   │   ├── ui/
│   │   │   │   ├── screens/             # UI Screens (Login, Dashboard, Tracking, etc.)
│   │   │   │   └── theme/               # Styling (Color.kt, Theme.kt)
│   │   │   ├── MainActivity.kt          # Entry point
│   │   │   └── PostOfficeApp.kt         # Application class
│   │   ├── res/                         # Resources (drawables, values, mipmap)
│   │   └── AndroidManifest.xml          # App configuration & permissions
│   ├── build.gradle.kts                 # App-level Gradle config
│   └── proguard-rules.pro               # ProGuard rules for minification
├── gradle/wrapper/                      # Gradle wrapper files
├── build.gradle.kts                     # Project-level Gradle config
└── settings.gradle.kts                  # Project settings
```

---

## 🚀 Getting Started (Run Locally)

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Latest version recommended)
- Java Development Kit (JDK) 17 or higher
- An Android Emulator or a physical Android device (Android 8.0+ recommended)
- The backend server running locally or deployed (to handle API requests)

### Detailed Steps

#### 1. Clone the Repository

```bash
git clone [https://github.com/ICT-Mahidol/2025-ITCS383-EMERALD-ANDROID.git](https://github.com/ICT-Mahidol/2025-ITCS383-EMERALD-ANDROID.git)
```

#### 2. Open the Project in Android Studio

1. Open Android Studio.
2. Select **Open** and navigate to the cloned `2025-ITCS383-EMERALD-ANDROID` directory.
3. Allow Android Studio to sync the project with Gradle files. This might take a few minutes as it downloads the required dependencies.

#### 3. Configure the API Connection

If you are running the backend locally, you will need to point the Android app to your local machine's IP address (not `localhost`, as that resolves to the emulator itself). 

1. Find your machine's local IP address (e.g., `192.168.1.x`). *If using the default Android Studio emulator to connect to localhost, you can use `10.0.2.2`.*
2. Open `app/src/main/java/com/emerald/postoffice/data/api/RetrofitClient.kt`.
3. Update the `BASE_URL` variable to point to your backend API:
   ```kotlin
   const val BASE_URL = "[http://10.0.2.2:3000/api/](http://10.0.2.2:3000/api/)" // Example for local emulator
   ```

#### 4. Run the Application

1. Select an emulator or connected physical device from the target device drop-down menu in the toolbar.
2. Click the green **Run** button (`Shift + F10`) to build and launch the app.

---

## 🛠️ Troubleshooting

| Issue | Solution |
|-------|----------|
| `Network Security Exception` | Since the backend uses HTTP (localhost), ensure `android:usesCleartextTraffic="true"` is in your `AndroidManifest.xml` under `<application>`. |
| `Connection Refused` | Ensure your backend server is actually running. If testing on a physical device, ensure both the phone and PC are on the same Wi-Fi network and use your PC's IPv4 address in `RetrofitClient.kt`. |
| `Gradle Sync Failed` | Check your internet connection, ensure you have the correct JDK version selected in Android Studio settings (`File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle`). |

---

## ⚙️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **UI Framework** | Jetpack Compose (Kotlin) |
| **Networking** | Retrofit2 & OkHttp |
| **Build System** | Gradle (Kotlin DSL) |
| **Backend/API** | Node.js + Express (Inherited from Web version) |

---

## 📋 System Requirements

- **Minimum SDK:** API Level 24 (Android 7.0) or higher recommended
- **Network:** Requires an active internet connection to communicate with the Post Office API
- **Design theme:** Thailand Post (white & red styling defined in `ui/theme`)
