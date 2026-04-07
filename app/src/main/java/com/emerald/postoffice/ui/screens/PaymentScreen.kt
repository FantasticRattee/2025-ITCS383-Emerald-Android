package com.emerald.postoffice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emerald.postoffice.data.SessionManager
import com.emerald.postoffice.data.api.RetrofitClient
import com.emerald.postoffice.data.model.CreateShipmentRequest
import com.emerald.postoffice.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PaymentMethod(
    val id: String,
    val icon: ImageVector,
    val name: String,
    val desc: String
)

val paymentMethods = listOf(
    PaymentMethod("card", Icons.Default.CreditCard, "Credit / Debit", "Visa, Mastercard, JCB"),
    PaymentMethod("promptpay", Icons.Default.QrCode2, "PromptPay", "Scan QR to pay instantly"),
    PaymentMethod("transfer", Icons.Default.AccountBalance, "Bank Transfer", "Direct bank transfer"),
    PaymentMethod("cash", Icons.Default.Payments, "Pay at Counter", "Pay at any post office"),
    PaymentMethod("wallet", Icons.Default.AccountBalanceWallet, "e-Wallet", "TrueMoney, Rabbit LINE"),
    PaymentMethod("cod", Icons.Default.LocalShipping, "COD", "Cash on delivery (+30 THB)")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    sessionManager: SessionManager,
    amount: Double,
    shipmentData: String,
    onPaymentSuccess: () -> Unit,
    onViewLabel: (String, String) -> Unit = { _, _ -> },
    onBack: () -> Unit
) {
    val userId by sessionManager.userId.collectAsState(initial = 0)
    var selectedMethod by remember { mutableStateOf("card") }
    var cardNumber by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }
    var trackingNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val totalAmount = if (selectedMethod == "cod") amount + 30.0 else amount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (paymentSuccess) {
            // Success screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(StatusDelivered.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = StatusDelivered, modifier = Modifier.size(48.dp))
                }
                Spacer(Modifier.height(20.dp))
                Text("Payment Successful!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Your shipment is confirmed", color = TextMuted, fontSize = 14.sp)
                Spacer(Modifier.height(24.dp))

                // Receipt
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ReceiptRow("Tracking ID", trackingNumber, ThaiPostRed)
                        ReceiptRow("Amount Paid", String.format("%.2f THB", totalAmount), TextDark)
                        ReceiptRow("Method", paymentMethods.find { it.id == selectedMethod }?.name ?: selectedMethod, TextDark)
                        ReceiptRow("Status", "Confirmed", StatusDelivered)
                    }
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { onViewLabel(trackingNumber, shipmentData) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed)
                ) {
                    Icon(Icons.Default.Description, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("View Shipping Label", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onPaymentSuccess,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
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
                // Amount card
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
                            Text("Total Amount", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            Text(
                                String.format("%.2f THB", totalAmount),
                                color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold
                            )
                            if (selectedMethod == "cod") {
                                Text("(includes +30 THB COD fee)", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                            }
                        }
                        Icon(Icons.Default.Receipt, null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                    }
                }

                // Payment methods
                Text("Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                paymentMethods.forEach { method ->
                    val isSelected = selectedMethod == method.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = method.id }
                            .then(
                                if (isSelected) Modifier.border(2.dp, ThaiPostRed, RoundedCornerShape(12.dp))
                                else Modifier
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ThaiPostRed.copy(alpha = 0.05f) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) ThaiPostRed.copy(alpha = 0.1f)
                                        else Color(0xFFF3F4F6)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    method.icon, null,
                                    tint = if (isSelected) ThaiPostRed else TextMuted,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(method.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Text(method.desc, fontSize = 12.sp, color = TextMuted)
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedMethod = method.id },
                                colors = RadioButtonDefaults.colors(selectedColor = ThaiPostRed)
                            )
                        }
                    }
                }

                // Card details form (only for card method)
                if (selectedMethod == "card") {
                    Text("Card Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    // Card preview
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF1A1A2E), Color(0xFF2D2D5E))
                                    ),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Column {
                                Text("CREDIT CARD", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, letterSpacing = 2.sp)
                                Spacer(Modifier.height(24.dp))
                                Text(
                                    if (cardNumber.isBlank()) "---- ---- ---- ----"
                                    else cardNumber.chunked(4).joinToString(" "),
                                    color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp
                                )
                                Spacer(Modifier.height(20.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("CARD HOLDER", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                                        Text(cardName.uppercase().ifBlank { "YOUR NAME" }, color = Color.White, fontSize = 13.sp)
                                    }
                                    Column {
                                        Text("EXPIRES", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                                        Text(cardExpiry.ifBlank { "MM/YY" }, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = cardNumber, onValueChange = { cardNumber = it.filter { c -> c.isDigit() }.take(16) },
                        label = { Text("Card Number") },
                        placeholder = { Text("1234 5678 9012 3456") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cardName, onValueChange = { cardName = it },
                        label = { Text("Card Holder Name") },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = cardExpiry, onValueChange = {
                                val digits = it.filter { c -> c.isDigit() }.take(4)
                                cardExpiry = if (digits.length >= 3) digits.substring(0, 2) + "/" + digits.substring(2) else digits
                            },
                            label = { Text("Expiry") },
                            placeholder = { Text("MM/YY") },
                            modifier = Modifier.weight(1f), singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = cardCvv, onValueChange = { cardCvv = it.filter { c -> c.isDigit() }.take(3) },
                            label = { Text("CVV") },
                            placeholder = { Text("123") },
                            modifier = Modifier.weight(1f), singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // PromptPay QR section
                if (selectedMethod == "promptpay") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Scan QR to Pay", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(16.dp))
                            // QR placeholder
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .border(2.dp, Color(0xFFE8D0D0), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.QrCode2, null, modifier = Modifier.size(80.dp), tint = TextDark)
                                    Spacer(Modifier.height(8.dp))
                                    Text("PromptPay QR", fontSize = 12.sp, color = TextMuted)
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Amount", fontSize = 11.sp, color = TextMuted)
                                    Text(String.format("%.2f THB", totalAmount), fontWeight = FontWeight.Bold, color = ThaiPostRed, fontSize = 18.sp)
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Supports: KBank, SCB, KTB, BBL, BAY, TTB, GSB", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }

                // Bank Transfer section
                if (selectedMethod == "transfer") {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Bank Transfer Details", fontWeight = FontWeight.Bold)
                            HorizontalDivider()
                            ReceiptRow("Bank", "Kasikorn Bank (KBank)", TextDark)
                            ReceiptRow("Account", "xxx-x-xx567-x", TextDark)
                            ReceiptRow("Name", "Thailand Post Co., Ltd.", TextDark)
                            ReceiptRow("Amount", String.format("%.2f THB", totalAmount), ThaiPostRed)
                        }
                    }
                }

                errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }

                // Pay button
                Button(
                    onClick = {
                        isProcessing = true; errorMessage = null
                        scope.launch {
                            try {
                                val parts = shipmentData.split("|")
                                val generatedTrackId = "PO-${System.currentTimeMillis().toString().takeLast(10)}"
                                val methodName = paymentMethods.find { it.id == selectedMethod }?.name ?: selectedMethod

                                val req = CreateShipmentRequest(
                                    userId = userId,
                                    trackId = generatedTrackId,
                                    total = String.format("%.2f", totalAmount),
                                    pkg = parts.getOrElse(3) { "Parcel" },
                                    svc = parts.getOrElse(4) { "Standard" },
                                    sname = "User",
                                    sprov = parts.getOrElse(1) { "Bangkok" },
                                    rname = parts.getOrElse(0) { "" },
                                    rprov = parts.getOrElse(2) { "" },
                                    weight = parts.getOrElse(5) { null }?.ifBlank { "1 kg" } ?: "1 kg",
                                    dims = parts.getOrElse(6) { null }?.ifBlank { null },
                                    contents = parts.getOrElse(7) { null }?.ifBlank { "General" } ?: "General",
                                    insurance = if (parts.getOrElse(8) { "0" } == "1") "Yes" else "No",
                                    handling = parts.getOrElse(9) { "None" },
                                    paymentMethod = methodName,
                                    paymentRef = "REF-${System.currentTimeMillis().toString().takeLast(6)}"
                                )

                                // Simulate payment processing delay
                                delay(2000)

                                val resp = RetrofitClient.apiService.createShipment(req)
                                if (resp.isSuccessful && resp.body()?.success == true) {
                                    trackingNumber = resp.body()?.trackId ?: generatedTrackId
                                    paymentSuccess = true
                                } else {
                                    errorMessage = resp.body()?.error ?: "Payment failed"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                            } finally {
                                isProcessing = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Processing...", fontSize = 16.sp)
                    } else {
                        Icon(Icons.Default.Lock, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Pay ${String.format("%.2f THB", totalAmount)}",
                            fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Security note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Secure payment with 256-bit encryption", fontSize = 11.sp, color = TextMuted)
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, valueColor: Color) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = TextMuted)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
