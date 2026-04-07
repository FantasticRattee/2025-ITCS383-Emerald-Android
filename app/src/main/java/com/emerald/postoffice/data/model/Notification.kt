package com.emerald.postoffice.data.model

import com.google.gson.annotations.SerializedName

data class AppNotification(
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    val message: String = "",
    val type: String = "info",
    @SerializedName("is_read")
    val isRead: Int = 0,
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class NotificationResponse(
    val success: Boolean,
    val notifications: List<AppNotification>? = null
)

data class Activity(
    val id: Int = 0,
    @SerializedName("user_id")
    val userId: Int = 0,
    val type: String = "",
    val title: String = "",
    val subtitle: String = "",
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class ActivityResponse(
    val success: Boolean,
    val activities: List<Activity>? = null
)
