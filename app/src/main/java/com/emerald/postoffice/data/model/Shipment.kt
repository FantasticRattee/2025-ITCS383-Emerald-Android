package com.emerald.postoffice.data.model

import com.google.gson.annotations.SerializedName

data class Shipment(
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("tracking_number")
    val trackingNumber: String = "",
    val recipient: String = "",
    val origin: String = "",
    val destination: String = "",
    val status: String = "pending",
    val eta: String? = null,
    @SerializedName("last_update")
    val lastUpdate: String? = null,
    val amount: Double = 0.0,
    val type: String = "Parcel",
    val service: String = "Standard",
    val weight: String? = null,
    val dims: String? = null,
    val contents: String? = null,
    val insurance: Int = 0,
    val handling: String = "None",
    @SerializedName("created_at")
    val createdAt: String? = null,
    // joined from payments
    val method: String? = null
)

data class ShipmentListResponse(
    val success: Boolean,
    val shipments: List<Shipment>? = null
)

data class TrackResponse(
    val success: Boolean,
    val shipment: Shipment? = null,
    val message: String? = null
)

data class CreateShipmentRequest(
    val userId: Int,
    val trackId: String,
    val total: String,
    val pkg: String = "Parcel",
    val svc: String = "Standard",
    val sname: String,
    val sphone: String = "",
    val saddr: String = "",
    val sprov: String,
    val szip: String = "",
    val rname: String,
    val rphone: String = "",
    val raddr: String = "",
    val rprov: String,
    val rzip: String = "",
    val weight: String? = null,
    val dims: String? = null,
    val contents: String? = null,
    val declval: String = "0",
    val insurance: String = "No",
    val handling: String = "None",
    val paymentMethod: String = "Credit Card",
    val paymentRef: String = ""
)

data class CreateShipmentResponse(
    val success: Boolean,
    val shipmentId: Int? = null,
    val trackId: String? = null,
    val error: String? = null
)

data class MonthlyData(
    val month: String = "",
    val count: Int = 0
)

data class MonthlyResponse(
    val success: Boolean,
    val data: List<MonthlyData>? = null
)

data class HistoryItem(
    @SerializedName("tracking_number")
    val trackingNumber: String = "",
    val sAddr: String = "",
    val rAddr: String = "",
    val receiver: String = "",
    val status: String = "",
    val amount: String = "0",
    val paymentMethod: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)
