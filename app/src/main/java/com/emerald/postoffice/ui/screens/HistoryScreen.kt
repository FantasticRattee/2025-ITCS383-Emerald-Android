package com.emerald.postoffice.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emerald.postoffice.data.SessionManager
import com.emerald.postoffice.data.api.RetrofitClient
import com.emerald.postoffice.data.model.HistoryItem
import com.emerald.postoffice.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(sessionManager: SessionManager, onBack: () -> Unit) {
    val userId by sessionManager.userId.collectAsState(initial = 0)
    var history by remember { mutableStateOf<List<HistoryItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        if (userId > 0) {
            try {
                val resp = RetrofitClient.apiService.getHistory(userId)
                if (resp.isSuccessful) history = resp.body() ?: emptyList()
            } catch (_: Exception) {}
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ThaiPostRed)
            }
        } else if (history.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Receipt, null, tint = TextMuted, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("No transactions yet", color = TextMuted)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(history) { item ->
                    HistoryCard(item)
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryItem) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val statusColor = when (item.status.lowercase()) {
        "delivered" -> StatusDelivered; "in transit" -> StatusInTransit
        "processing" -> StatusProcessing; else -> StatusPending
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(item.trackingNumber))
                        Toast.makeText(context, "Copied: ${item.trackingNumber}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(item.trackingNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.ContentCopy, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                }
                AssistChip(
                    onClick = {},
                    label = { Text(item.status.replaceFirstChar { it.uppercase() }, fontSize = 11.sp) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = statusColor.copy(alpha = 0.1f), labelColor = statusColor),
                    border = null
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("${item.sAddr} -> ${item.rAddr}", fontSize = 12.sp, color = TextMuted)
            Text("To: ${item.receiver}", fontSize = 12.sp, color = TextMuted)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(item.paymentMethod ?: "N/A", fontSize = 12.sp, color = TextMuted)
                Text("${item.amount} THB", fontWeight = FontWeight.Bold, color = ThaiPostRed)
            }
        }
    }
}
