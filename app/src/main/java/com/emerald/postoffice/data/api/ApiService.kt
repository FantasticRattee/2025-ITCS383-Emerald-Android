package com.emerald.postoffice.data.api

import com.emerald.postoffice.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // User
    @GET("user/profile/{id}")
    suspend fun getProfile(@Path("id") userId: Int): Response<ProfileResponse>

    @GET("user/stats/{id}")
    suspend fun getStats(@Path("id") userId: Int): Response<UserStats>

    // Shipments - returns arrays directly, not wrapped
    @GET("shipments/{userId}")
    suspend fun getShipments(@Path("userId") userId: Int): Response<List<Shipment>>

    @GET("shipments/recent")
    suspend fun getRecentShipments(): Response<List<Shipment>>

    @GET("shipments/track/{trackingNumber}")
    suspend fun trackShipment(@Path("trackingNumber") trackingNumber: String): Response<Shipment>

    @GET("shipments/monthly/{userId}")
    suspend fun getMonthlyData(@Path("userId") userId: Int): Response<List<MonthlyData>>

    @GET("shipments/history/{userId}")
    suspend fun getHistory(@Path("userId") userId: Int): Response<List<HistoryItem>>

    @POST("shipments")
    suspend fun createShipment(@Body request: CreateShipmentRequest): Response<CreateShipmentResponse>

    // Notifications
    @GET("notifications/{userId}")
    suspend fun getNotifications(@Path("userId") userId: Int): Response<List<AppNotification>>

    @PATCH("notifications/{userId}/read-all")
    suspend fun markAllRead(@Path("userId") userId: Int): Response<Map<String, Any>>

    // Activity
    @GET("activity/{userId}")
    suspend fun getActivity(@Path("userId") userId: Int): Response<List<Activity>>
}
