package com.emerald.postoffice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.emerald.postoffice.data.SessionManager
import com.emerald.postoffice.data.api.RetrofitClient
import com.emerald.postoffice.data.model.CreateShipmentRequest
import com.emerald.postoffice.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShipmentScreen(
    sessionManager: SessionManager,
    onNavigateToPayment: (Double, String) -> Unit = { _, _ -> },
    onShipmentCreated: () -> Unit,
    onBack: () -> Unit
) {
    val userId by sessionManager.userId.collectAsState(initial = 0)
    var recipient by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("Bangkok") }
    var destination by remember { mutableStateOf("") }
    var parcelType by remember { mutableStateOf("Parcel") }
    var service by remember { mutableStateOf("Standard") }
    var weight by remember { mutableStateOf("") }
    var dims by remember { mutableStateOf("") }
    var contents by remember { mutableStateOf("") }
    var insurance by remember { mutableStateOf(false) }
    var handling by remember { mutableStateOf("None") }
    // paymentMethod removed - now handled by PaymentScreen
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successTracking by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Price calculation
    val basePrice = when (service) {
        "Express" -> 150.0; "Same Day" -> 250.0; else -> 80.0
    }
    val weightSurcharge = weight.replace(Regex("[^0-9.]"), "").toDoubleOrNull()?.let { it * 20 } ?: 0.0
    val insuranceFee = if (insurance) 50.0 else 0.0
    val totalPrice = basePrice + weightSurcharge + insuranceFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Shipment") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (successTracking != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StatusDelivered.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = StatusDelivered, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Shipment Created!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Tracking: $successTracking", color = TextMuted)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onShipmentCreated, colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed)) {
                            Text("Back to Dashboard")
                        }
                    }
                }
                return@Scaffold
            }

            // Recipient
            SectionTitle("Recipient Information")
            OutlinedTextField(
                value = recipient, onValueChange = { recipient = it },
                label = { Text("Recipient Name") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = origin, onValueChange = { origin = it },
                label = { Text("Origin City") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = destination, onValueChange = { destination = it },
                label = { Text("Destination City") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Package details
            SectionTitle("Package Details")

            // Type selector
            Text("Type", fontSize = 13.sp, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Parcel", "Letter", "Express", "Registered").forEach { type ->
                    FilterChip(
                        selected = parcelType == type,
                        onClick = { parcelType = type },
                        label = { Text(type) }
                    )
                }
            }

            // Service selector
            Text("Service Level", fontSize = 13.sp, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Standard", "Express", "Same Day").forEach { svc ->
                    FilterChip(
                        selected = service == svc,
                        onClick = { service = svc },
                        label = { Text(svc) }
                    )
                }
            }

            OutlinedTextField(
                value = weight, onValueChange = { weight = it },
                label = { Text("Weight (e.g., 1.5 kg)") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = dims, onValueChange = { dims = it },
                label = { Text("Dimensions (e.g., 20x15x10 cm)") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = contents, onValueChange = { contents = it },
                label = { Text("Contents Description") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Handling
            Text("Special Handling", fontSize = 13.sp, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("None", "Fragile", "Keep Cold", "Keep Dry", "Keep Upright").forEach { h ->
                    FilterChip(
                        selected = handling == h,
                        onClick = { handling = h },
                        label = { Text(h, fontSize = 11.sp) }
                    )
                }
            }

            // Insurance
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Insurance (+50 THB)", modifier = Modifier.weight(1f))
                Switch(checked = insurance, onCheckedChange = { insurance = it })
            }

            // Price summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ThaiPostRed.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Base ($service)", fontSize = 13.sp); Text(String.format("%.2f THB", basePrice), fontSize = 13.sp)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Weight surcharge", fontSize = 13.sp); Text(String.format("%.2f THB", weightSurcharge), fontSize = 13.sp)
                    }
                    if (insurance) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Insurance", fontSize = 13.sp); Text("50.00 THB", fontSize = 13.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text(String.format("%.2f THB", totalPrice), fontWeight = FontWeight.Bold, color = ThaiPostRed)
                    }
                }
            }

            errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }

            // Proceed to Payment
            Button(
                onClick = {
                    if (recipient.isBlank() || destination.isBlank()) {
                        errorMessage = "Please fill in recipient and destination"; return@Button
                    }
                    val shipmentData = listOf(
                        recipient.trim(), origin.trim(), destination.trim(),
                        parcelType, service,
                        weight.ifBlank { "" }, dims.ifBlank { "" },
                        contents.ifBlank { "" },
                        if (insurance) "1" else "0", handling
                    ).joinToString("|")
                    onNavigateToPayment(totalPrice, shipmentData)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed)
            ) {
                Icon(Icons.Default.Payment, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Proceed to Payment", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark, modifier = Modifier.padding(top = 8.dp))
}
