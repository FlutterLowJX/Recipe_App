package com.example.recipeapp.db.retrofit

import com.example.recipeapp.db.response.RecipeResponseBody
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {
    @GET("/recipe.php")
    @Headers("Accept: application/json", "Content-type:application/json")
    suspend fun getRecipeList(): Response<RecipeResponseBody>
    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val interceptor = HttpLoggingInterceptor()
                HttpLoggingInterceptor().level = HttpLoggingInterceptor.Level.BODY
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://lowjungxuan.000webhostapp.com")
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
                return retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}

