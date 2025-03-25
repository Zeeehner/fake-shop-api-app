package de.syntax_institut.jetpack.a04_05_online_shopper.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.product.Product
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://fakestoreapi.com/"

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()


interface APIService {
    @GET("products")
    suspend fun getArticles(): List<Product>
}

object ShopAPI {
    val retrofitService: APIService by lazy { retrofit.create(APIService::class.java) }
}

fun main() = runBlocking {
    val apiService = ShopAPI.retrofitService
    try {
        val products = apiService.getArticles()
        println("API Response: ${products}")
    } catch (e: Exception) {
        println("API Error: ${e.message}")
    }
}