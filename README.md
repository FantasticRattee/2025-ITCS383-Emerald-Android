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

## 💻 Developer Setup (Run Locally)

Because this is a native mobile app, it requires the Node.js backend and MySQL database from the web repository to function locally.

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Latest version)
- Java Development Kit (JDK) 17 or highers
- [Node.js](https://nodejs.org/) v16 or higher
- [MySQL](https://dev.mysql.com/downloads/) installed and running
- The Backend repository cloned on your machine

### Step 1: Set Up the Database & Backend

1. Open a terminal and navigate to your backend repository folder.
2. Initialize the MySQL database:
   ```bash
   mysql -u root -p < implementations/backend/setup.sql
   ```
   *(Enter your MySQL root password when prompted).*
3. Configure your environment variables. Create a `.env` file in the backend folder:
   ```env
   DB_HOST=localhost
   DB_USER=root
   DB_PASS=your_mysql_password
   DB_NAME=postoffice
   PORT=3000
   ```
4. Start the local backend server:
   ```bash
   cd implementations/backend
   npm install
   node server.js
   ```
   *(Keep this terminal open)*.

### Step 2: Configure the Android App

1. Clone this Android repository:
   ```bash
   git clone [https://github.com/ICT-Mahidol/2025-ITCS383-EMERALD-ANDROID.git](https://github.com/ICT-Mahidol/2025-ITCS383-EMERALD-ANDROID.git)
   ```
2. Open the project in **Android Studio** and let Gradle sync.
3. Open `app/src/main/java/com/emerald/postoffice/data/api/RetrofitClient.kt`.
4. Update the `BASE_URL` to point to your local machine:
   - **For Android Studio Emulator:** Use `http://10.0.2.2:3000/api/`
   - **For Physical Android Device:** Use `http://YOUR_WIFI_IP:3000/api/` (e.g., `http://192.168.1.5:3000/api/`)

### Step 3: Run the App

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
