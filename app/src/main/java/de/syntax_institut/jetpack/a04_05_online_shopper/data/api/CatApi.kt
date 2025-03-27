package de.syntax_institut.jetpack.a04_05_online_shopper.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.syntax_institut.codingchallenges04_06.BuildConfig
import de.syntax_institut.jetpack.a04_05_online_shopper.data.model.Cat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private val apiKey = BuildConfig.API_KEY

private const val CAT_BASE_URL = "https://api.thecatapi.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val cat_loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
private val cat_okHttpClient = OkHttpClient.Builder()
    .addInterceptor(cat_loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(CAT_BASE_URL)
    .client(cat_okHttpClient)
    .build()

interface CatAPIService {

    // requests mit den endpunkten
    @GET("images/search")
    suspend fun getCatImagesWithParam(
        @Query("limit") limit: Int,
        @Query("api_key") key: String = apiKey
    ): List<Cat>

    @GET("images/search")
    suspend fun getCatImagesWithHeader(
        @Query("limit") limit: Int,
        @Query("has_breeds") hasBreeds: Boolean = true,
        @Header("x-api-key") key: String = apiKey
    ): List<Cat>

}

object CatApi {
    val retrofitService: CatAPIService by lazy { retrofit.create(CatAPIService::class.java) }
}