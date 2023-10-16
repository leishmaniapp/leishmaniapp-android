package com.leishmaniapp.background.infrastructure

import retrofit2.Response
import retrofit2.http.POST

interface DemoApi {
    @POST("")
    suspend fun postMetadata(): Response<Post>
}