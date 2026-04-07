package com.emerald.postoffice.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val role: String = "member",
    val address: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val userId: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val message: String? = null,
    val error: String? = null
)

data class RegisterResponse(
    val success: Boolean,
    val userId: Int? = null,
    val message: String? = null
)

data class ProfileResponse(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val role: String = "member",
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class UserStats(
    val total: Int = 0,
    val delivered: String = "0",
    val transit: String = "0",
    val pending: String = "0",
    val totalSpend: String = "0"
)
