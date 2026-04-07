package com.emerald.postoffice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.emerald.postoffice.data.SessionManager
import com.emerald.postoffice.ui.screens.*
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard"
    const val CREATE_SHIPMENT = "create_shipment"
    const val PAYMENT = "payment/{amount}/{shipmentData}"
    const val TRACKING = "tracking"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    const val SHIPPING_LABEL = "shipping_label/{trackingNumber}/{shipmentData}"

    fun payment(amount: Double, shipmentData: String): String {
        val encoded = URLEncoder.encode(shipmentData, "UTF-8")
        return "payment/$amount/$encoded"
    }

    fun shippingLabel(trackingNumber: String, shipmentData: String): String {
        val encoded = URLEncoder.encode(shipmentData, "UTF-8")
        return "shipping_label/$trackingNumber/$encoded"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                sessionManager = sessionManager
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                sessionManager = sessionManager,
                onNavigateToCreateShipment = { navController.navigate(Routes.CREATE_SHIPMENT) },
                onNavigateToTracking = { navController.navigate(Routes.TRACKING) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CREATE_SHIPMENT) {
            CreateShipmentScreen(
                sessionManager = sessionManager,
                onNavigateToPayment = { amount, data ->
                    navController.navigate(Routes.payment(amount, data))
                },
                onShipmentCreated = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PAYMENT,
            arguments = listOf(
                navArgument("amount") { type = NavType.FloatType },
                navArgument("shipmentData") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
            val rawData = backStackEntry.arguments?.getString("shipmentData") ?: ""
            val shipmentData = URLDecoder.decode(rawData, "UTF-8")

            PaymentScreen(
                sessionManager = sessionManager,
                amount = amount,
                shipmentData = shipmentData,
                onPaymentSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onViewLabel = { tracking, data ->
                    navController.navigate(Routes.shippingLabel(tracking, data))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TRACKING) {
            TrackingScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                sessionManager = sessionManager,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                sessionManager = sessionManager,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.SHIPPING_LABEL,
            arguments = listOf(
                navArgument("trackingNumber") { type = NavType.StringType },
                navArgument("shipmentData") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tracking = backStackEntry.arguments?.getString("trackingNumber") ?: ""
            val rawData = backStackEntry.arguments?.getString("shipmentData") ?: ""
            val data = URLDecoder.decode(rawData, "UTF-8")
            val parts = data.split("|")

            ShippingLabelScreen(
                trackingNumber = tracking,
                senderName = "User",
                senderAddress = parts.getOrElse(1) { "Bangkok" },
                receiverName = parts.getOrElse(0) { "" },
                receiverAddress = parts.getOrElse(2) { "" },
                packageType = parts.getOrElse(3) { "Parcel" },
                serviceLevel = parts.getOrElse(4) { "Standard" },
                weight = parts.getOrElse(5) { "" },
                onBack = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }
    }
}
