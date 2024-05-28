package com.talentwood.network

import com.talentwood.calladapter.NetworkResponse
import com.talentwood.model.request.LoginRequest
import com.talentwood.model.request.RegisterRequest
import com.talentwood.model.request.ResetPassRequest
import com.talentwood.model.response.AttributeResponse
import com.talentwood.model.response.BannerListResponse
import com.talentwood.model.response.CommonResponse
import com.talentwood.model.response.LoginResponse
import com.talentwood.model.response.MovieListResponse
import com.talentwood.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Pradeep
 */
interface ApiService {

    @POST("user/login")
    suspend fun fetchLogin(@Body request: LoginRequest): NetworkResponse<LoginResponse>

    @GET("user/attributes")
    suspend fun fetchAttributes(): NetworkResponse<AttributeResponse>

    @POST("user/signup")
    suspend fun fetchRegister(@Body request: RegisterRequest): NetworkResponse<CommonResponse>
    @POST("user/sendotp")
    suspend fun fetchOtp(@Query("userfield") userfield: String): NetworkResponse<CommonResponse>

    @POST("user/verifyotp")
    suspend fun validateOtp(@Query("userfield") userfield: String,@Query("userOtp") userOtp: Int): NetworkResponse<CommonResponse>

    @POST("user/forgetpassword")
    suspend fun fetchForgetPass(@Body request: ResetPassRequest): NetworkResponse<CommonResponse>
    @GET("home/allmovielist")
    suspend fun fetchAllMovie(): NetworkResponse<CommonResponse>

    @GET("home/movielist")
    suspend fun fetchMovies(): NetworkResponse<MovieListResponse>

    @GET("home/banners")
    suspend fun fetchBanners(): NetworkResponse<BannerListResponse>

    @GET("goldenHourMovie")
    suspend fun fetchGoldenHour(): NetworkResponse<CommonResponse>

}