package dev.rakamin.newsapp.network

import dev.rakamin.newsapp.model.NewsResponse
import retrofit2.http.Query
import retrofit2.http.GET

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getAllNews(
            @Query("apiKey") apiKey: String,
            @Query("currentPage") currentPage: Int,
            @Query("pageSize") pageSize: Int
        ): NewsResponse
    }

