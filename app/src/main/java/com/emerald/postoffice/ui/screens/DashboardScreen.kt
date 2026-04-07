package com.emerald.postoffice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emerald.postoffice.data.SessionManager
import com.emerald.postoffice.data.api.RetrofitClient
import com.emerald.postoffice.data.model.Shipment
import com.emerald.postoffice.data.model.UserStats
import com.emerald.postoffice.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    sessionManager: SessionManager,
    onNavigateToCreateShipment: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val userId by sessionManager.userId.collectAsState(initial = 0)
    val userName by sessionManager.userName.collectAsState(initial = "")
    var stats by remember { mutableStateOf(UserStats()) }
    var recentShipments by remember { mutableStateOf<List<Shipment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId > 0) {
            try {
                val statsResp = RetrofitClient.apiService.getStats(userId)
                if (statsResp.isSuccessful) stats = statsResp.body() ?: UserStats()

                val shipmentsResp = RetrofitClient.apiService.getShipments(userId)
                if (shipmentsResp.isSuccessful) recentShipments = shipmentsResp.body() ?: emptyList()
            } catch (_: Exception) {}
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Welcome back,", fontSize = 12.sp, color = TextMuted)
                        Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Ship") },
                    selected = false,
                    onClick = onNavigateToCreateShipment
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Track") },
                    selected = false,
                    onClick = onNavigateToTracking
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Receipt, contentDescription = null) },
                    label = { Text("History") },
                    selected = false,
                    onClick = onNavigateToHistory
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ThaiPostRed)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stats cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Total", "${stats.total}", Icons.Default.Inventory2, ThaiPostRed, Modifier.weight(1f))
                    StatCard("Delivered", stats.delivered, Icons.Default.CheckCircle, StatusDelivered, Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("In Transit", stats.transit, Icons.Default.LocalShipping, StatusInTransit, Modifier.weight(1f))
                    StatCard("Pending", stats.pending, Icons.Default.PendingActions, StatusPending, Modifier.weight(1f))
                }

                // Total spend
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ThaiPostRed)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Spent", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            Text(
                                "${stats.totalSpend} THB",
                                color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(40.dp))
                    }
                }

                // Quick actions
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton("New Shipment", Icons.Default.Add, ThaiPostRed, Modifier.weight(1f), onNavigateToCreateShipment)
                    ActionButton("Track Parcel", Icons.Default.Search, StatusInTransit, Modifier.weight(1f), onNavigateToTracking)
                    ActionButton("History", Icons.Default.History, StatusPending, Modifier.weight(1f), onNavigateToHistory)
                }

                // Recent shipments
                Text("Recent Shipments", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (recentShipments.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "No shipments yet",
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            color = TextMuted
                        )
                    }
                } else {
                    recentShipments.take(5).forEach { shipment ->
                        ShipmentCard(shipment)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextDark)
                Text(label, fontSize = 12.sp, color = TextMuted)
            }
        }
    }
}

@Composable
fun ActionButton(label: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = color)
        }
    }
}

@Composable
fun ShipmentCard(shipment: Shipment) {
    val statusColor = when (shipment.status.lowercase()) {
        "delivered" -> StatusDelivered
        "in transit" -> StatusInTransit
        "processing" -> StatusProcessing
        else -> StatusPending
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(shipment.trackingNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${shipment.origin} -> ${shipment.destination}", fontSize = 12.sp, color = TextMuted)
                Spacer(modifier = Modifier.height(2.dp))
                Text(shipment.recipient, fontSize = 12.sp, color = TextMuted)
            }
            Column(horizontalAlignment = Alignment.End) {
                AssistChip(
                    onClick = {},
                    label = { Text(shipment.status.replaceFirstChar { it.uppercase() }, fontSize = 11.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor.copy(alpha = 0.1f),
                        labelColor = statusColor
                    ),
                    border = null
                )
                Text(
                    String.format("%.2f THB", shipment.amount),
                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark
                )
            }
        }
    }
}
