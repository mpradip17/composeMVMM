package com.connect.calladapter

import com.talentwood.calladapter.NetworkResponse
import com.talentwood.calladapter.NetworkResponseCall
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type
/**
 * Created by Pradeep
 */
class NetworkResponseAdapter<S : Any, E : Any>(
        private val successType: Type,
        private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<NetworkResponse<S>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }
}
