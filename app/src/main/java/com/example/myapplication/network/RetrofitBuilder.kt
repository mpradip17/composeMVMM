package com.talentwood.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.talentwood.calladapter.NetworkResponseAdapterFactory
import com.talentwood.utils.TalentPreferences
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Header

/**
 * Created by Pradeep
 */
object RetrofitBuilder {
    private const val TIMEOUT = 30

    const val BASE_URL = "https://talentflicks.com/api/"
    private var kotlinXConverterFactory: Converter.Factory = getKotlinxSerialisationConverterFactory()

    private fun getKotlinxSerialisationConverterFactory(): Converter.Factory {
        val contentType = "application/json; charset=utf-8".toMediaType()
        val json = Json {
            coerceInputValues = true; ignoreUnknownKeys = true; prettyPrint = true; encodeDefaults =
            true
        }
        return json.asConverterFactory(contentType)
    }
    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(kotlinXConverterFactory)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(client)
            .build()
    }
    private fun getAuthRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = AuthInterceptor()
        val token = TalentPreferences(Application.getInstance()!!.applicationContext).getString(
            TalentPreferences.TOKEN
        )
        val client: OkHttpClient  = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                token?.let {
                    requestBuilder.addHeader("apitoken", it)
                }
                requestBuilder.addHeader("Content-Type", "application/json")
                chain.proceed(requestBuilder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(kotlinXConverterFactory)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(client)
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    fun getAuthorizedService(): ApiService = getAuthRetrofit().create(ApiService::class.java)

}