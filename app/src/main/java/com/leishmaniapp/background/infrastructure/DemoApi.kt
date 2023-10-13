package com.leishmaniapp.background.infrastructure

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface DemoApi {
    @GET("posts/1")
    suspend fun getPost(): Response<Post>

    @POST("")
    suspend fun postPost(): Response<Post>
}