package com.gojek.simplechat.deps

import com.gojek.simplechat.api.SimpleChatApi
import com.gojek.simplechat.constants.Constant
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getSimpleChatApi(simpleChatRetrofit: Retrofit): SimpleChatApi {
        return simpleChatRetrofit.create(SimpleChatApi::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callFactory(getCallFactory(okHttpClient))
                .build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun getGson(): Gson {
        return Gson()
    }

    private fun getCallFactory(client: OkHttpClient): Call.Factory {
        return Call.Factory { request ->
            val originalUrl = request.url()
            val requestUrl = originalUrl.newBuilder()
                    .build()
            val newRequest = request.newBuilder().url(requestUrl).build()
            client.newCall(newRequest)
        }
    }
}
