package com.rafael0112.asessment3.network

import com.rafael0112.asessment3.model.OpStatus
import com.rafael0112.asessment3.model.WikulStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://wikul-api.michael-kaiser.my.id/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WikulApiService {
    @GET("wikul")
    suspend fun getWikul(
        @Header("Authorization") token: String
    ): WikulStatus

    @Multipart
    @POST("wikul")
    suspend fun postWikul(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("wikul/{id_wikul}")
    suspend fun updateWikul(
        @Header("Authorization") token: String,
        @Part("_method") method: RequestBody,
        @Path("id_wikul") id_wikul: Long,
        @Part("name") name: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): OpStatus

    @DELETE("wikul/{id_wikul}")
    suspend fun deleteWikul(
        @Header("Authorization") token: String,
        @Path("id_wikul") id_wikul: Long
    ): OpStatus

    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): OpStatus
}

object WikulApi {
    val service: WikulApiService by lazy {
        retrofit.create(WikulApiService::class.java)
    }

    fun getImageUrl(id: Long): String {
        return "${BASE_URL}wikul/image/$id?timestamp=${System.currentTimeMillis()}"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }