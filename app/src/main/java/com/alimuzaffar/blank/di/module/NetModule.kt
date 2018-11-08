package com.alimuzaffar.blank.di.module

import android.app.Application
import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import com.alimuzaffar.blank.BuildConfig
import com.alimuzaffar.blank.net.ApiInterface
import com.alimuzaffar.blank.net.mock.MockApiImpl
import com.alimuzaffar.blank.util.Prefs
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {
    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Suppress("UNCHECKED_CAST")
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> {
                    json, _, _ ->
                    if (json.asJsonPrimitive.isNumber)
                        Date(json.asJsonPrimitive.asLong) else
                        null
                })
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache, prefs: Prefs): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.method(original.method(), original.body())
            val auth = original.header("Authorization")

            if (!TextUtils.isEmpty(auth)) {
                requestBuilder.removeHeader("Authorization")
            }

            if (!TextUtils.isEmpty(prefs.accessToken)) {
                requestBuilder.header("Authorization", "Bearer " + prefs.accessToken)
            }

            val correlationId = UUID.randomUUID().toString()
            var hasAcceptHeaders = false
            if (original.headers() != null) {
                for (key in original.headers().names()) {
                    if (key.equals("Accept", ignoreCase = true)) {
                        hasAcceptHeaders = true
                        Log.d("NetModule", "HAS HEADERS")
                        break
                    }
                }
            }
            requestBuilder
                    .header("Correlation-Id", correlationId)

            if (!hasAcceptHeaders) {
                requestBuilder
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")

            }
            chain.proceed(requestBuilder.build())
        }
        return httpClient.cache(cache)
                .followRedirects(false)
                .followSslRedirects(false)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .build()

    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providesApiInterface(retrofit: Retrofit): ApiInterface {
        // Uncomment everything here if the API comes online
        // and remember to change BASE_URL
        return if (BuildConfig.FLAVOR.contains("mock")) {
            MockApiImpl()
        } else retrofit.create(ApiInterface::class.java)

    }

    @Provides
    @Singleton
    fun providesPrefs(application: Application): Prefs {
        return Prefs(application.applicationContext)
    }

    @Provides
    fun providesResources(application: Application): Resources {
        return application.resources
    }
}
