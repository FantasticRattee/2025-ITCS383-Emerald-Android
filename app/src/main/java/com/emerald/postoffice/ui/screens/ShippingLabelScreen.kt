package com.emerald.postoffice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emerald.postoffice.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingLabelScreen(
    trackingNumber: String,
    senderName: String,
    senderAddress: String,
    receiverName: String,
    receiverAddress: String,
    packageType: String,
    serviceLevel: String,
    weight: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shipping Label") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Label card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ThaiPostRed)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "THAILAND POST",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Official Shipping Label - Thailand Post Co., Ltd.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            }
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    Text(
                                        serviceLevel.uppercase() + when (serviceLevel) {
                                            "Express" -> " (1-2 DAYS)"
                                            "Same Day" -> " (TODAY)"
                                            else -> " (3-5 DAYS)"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(packageType.uppercase(), fontSize = 10.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    // Tracking number section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ThaiPostRed.copy(alpha = 0.08f))
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "TRACKING NUMBER",
                            fontSize = 11.sp,
                            color = ThaiPostRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            trackingNumber,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThaiPostRed,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(Modifier.height(12.dp))
                        // Barcode representation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                trackingNumber.replace("-", "").forEach { char ->
                                    val w = ((char.code % 4) + 1).dp
                                    Box(
                                        modifier = Modifier
                                            .width(w)
                                            .fillMaxHeight()
                                            .background(Color.Black)
                                    )
                                    Spacer(modifier = Modifier.width(1.dp))
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            trackingNumber.replace("-", "  "),
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        )
                    }

                    // Sender / Receiver
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // From
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, ThaiPostRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text("FROM", color = ThaiPostRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(senderName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(senderAddress, fontSize = 12.sp, color = TextMuted)
                        }
                        // To
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, ThaiPostRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .background(ThaiPostRed.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text("TO", color = ThaiPostRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(receiverName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(receiverAddress, fontSize = 12.sp, color = TextMuted)
                        }
                    }

                    // Parcel details
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ThaiPostRed)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text("PARCEL DETAILS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        LabelDetailRow("Package Type", packageType)
                        LabelDetailRow("Service Level", serviceLevel)
                        LabelDetailRow("Weight", weight.ifBlank { "N/A" })
                        HorizontalDivider()
                        Text(
                            "Handle with care. Do not bend or crush.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Share/Save buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Share functionality */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Share")
                }
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThaiPostRed)
                ) {
                    Icon(Icons.Default.Home, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Dashboard")
                }
            }
        }
    }
}

@Composable
fun LabelDetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = TextMuted)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
