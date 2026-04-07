package com.emerald.postoffice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emerald.postoffice.data.api.RetrofitClient
import com.emerald.postoffice.data.model.Shipment
import com.emerald.postoffice.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(onBack: () -> Unit) {
    var trackingNumber by remember { mutableStateOf("") }
    var shipment by remember { mutableStateOf<Shipment?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searched by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Parcel") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search bar
            OutlinedTextField(
                value = trackingNumber,
                onValueChange = { trackingNumber = it; errorMessage = null },
                label = { Text("Enter Tracking Number") },
                placeholder = { Text("e.g., TH-2024-0089") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (trackingNumber.isNotEmpty()) {
                        IconButton(onClick = { trackingNumber = ""; shipment = null; searched = false }) {
                            Icon(Icons.Default.Clear, "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    if (trackingNumber.isBlank()) { errorMessage = "Enter a tracking number"; return@Button }
                    isLoading = true; errorMessage = null; shipment = null
                    scope.launch {
                        try {
                            val resp = RetrofitClient.apiService.trackShipment(trackingNumber.trim())
                            if (resp.isSuccessful && resp.body() != null) {
                                shipment = resp.body()
                            } else {
                                errorMessage = "Shipment not found"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Connection error"
                        } finally {
                            isLoading = false; searched = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                else { Icon(Icons.Default.Search, null); Spacer(Modifier.width(8.dp)); Text("Track") }
            }

            errorMessage?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(it, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            shipment?.let { s ->
                val statusColor = when (s.status.lowercase()) {
                    "delivered" -> StatusDelivered
                    "in transit" -> StatusInTransit
                    "processing" -> StatusProcessing
                    else -> StatusPending
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(s.trackingNumber, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            AssistChip(
                                onClick = {},
                                label = { Text(s.status.replaceFirstChar { it.uppercase() }) },
                                colors = AssistChipDefaults.assistChipColors(containerColor = statusColor.copy(alpha = 0.1f), labelColor = statusColor),
                                border = null
                            )
                        }
                        HorizontalDivider()
                        TrackingDetail("From", s.origin, Icons.Default.LocationOn)
                        TrackingDetail("To", s.destination, Icons.Default.Flag)
                        TrackingDetail("Recipient", s.recipient, Icons.Default.Person)
                        TrackingDetail("Type", "${s.type} - ${s.service}", Icons.Default.Inventory)
                        s.weight?.let { TrackingDetail("Weight", it, Icons.Default.Scale) }
                        s.eta?.let { TrackingDetail("ETA", it, Icons.Default.CalendarMonth) }
                        s.lastUpdate?.let { TrackingDetail("Last Update", it, Icons.Default.Update) }
                        HorizontalDivider()
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount", color = TextMuted)
                            Text(String.format("%.2f THB", s.amount), fontWeight = FontWeight.Bold, color = ThaiPostRed, fontSize = 18.sp)
                        }
                    }
                }
            }

            if (searched && shipment == null && errorMessage == null) {
                Text("No shipment found", color = TextMuted, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun TrackingDetail(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, null, tint = TextMuted, modifier = Modifier.size(20.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextMuted)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
