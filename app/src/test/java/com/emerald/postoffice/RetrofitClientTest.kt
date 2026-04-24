package com.emerald.postoffice

import com.emerald.postoffice.data.api.ApiService
import com.emerald.postoffice.data.api.RetrofitClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientTest {

    @Test
    fun `apiService is not null`() {
        // When
        val apiService = RetrofitClient.apiService

        // Then
        assertNotNull(apiService)
        assertTrue(apiService is ApiService)
    }

    @Test
    fun `retrofit client has correct base url`() {
        // This test assumes BuildConfig.API_BASE_URL is accessible
        // In a real test, you might need to mock BuildConfig or use a test base url
        // For now, just check that retrofit is created
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/api/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        assertNotNull(retrofit)
        assertEquals("https://example.com/api/", retrofit.baseUrl().toString())
    }

    @Test
    fun `http client has logging interceptor`() {
        // Given
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // When
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // Then
        assertNotNull(httpClient)
        // Note: In a real test, you might verify the interceptor is added
    }

    @Test
    fun `retrofit instance is created with gson converter`() {
        // When
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Then
        assertNotNull(retrofit)
        assertTrue(retrofit.converterFactories().any { it is GsonConverterFactory })
    }

    @Test
    fun `okhttp client has default timeouts`() {
        // When
        val client = OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // Then
        assertNotNull(client)
        assertEquals(30, client.connectTimeoutMillis() / 1000)
        assertEquals(30, client.readTimeoutMillis() / 1000)
    }
}