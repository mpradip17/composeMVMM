package com.talentwood.calladapter

import kotlinx.serialization.Serializable

/**
 * Created by Pradeep
 */
@Serializable
sealed class NetworkResponse<out T : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T>()

    /**
     * Success response with Error body
     */
    data class Failure<T : Any>(val body: T) : NetworkResponse<T>()

    /**
     * Failure response and Network Error  with Error Message
     */
    data class ApiError<String : Any>(val body: String) : NetworkResponse<Nothing>()

    /**
     * Failure response with Error Code and Description
     */
    data class NetworkError<Int : Any,String : Any>( val code: Int,val body: String) : NetworkResponse<Nothing>()
}