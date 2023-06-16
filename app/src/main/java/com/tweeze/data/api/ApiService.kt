package com.tweeze.data.api

import retrofit2.http.GET

interface ApiService {
    @GET("exec?action=getTopDestination")
    suspend fun getPosts(): List<ApiDataItem>

    @GET("exec?action=getTopStay")
    suspend fun getStay(): List<ApiDataItem>

    @GET("exec?action=getTopCulinary")
    suspend fun getCulinary(): List<ApiDataItem>

    @GET("exec?action=getTopHotel")
    suspend fun getHotel(): List<ApiDataItem>

    @GET("exec?action=getTopAttraction")
    suspend fun getAttraction(): List<ApiDataItem>
}