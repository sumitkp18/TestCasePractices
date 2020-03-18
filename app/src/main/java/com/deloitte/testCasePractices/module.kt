package com.deloitte.testCasePractices

import com.deloitte.testCasePractices.repo.Repo
import com.deloitte.testCasePractices.repo.API
import com.deloitte.testCasePractices.repo.RepoImpl
import com.deloitte.testCasePractices.repo.URLConstants
import com.deloitte.testCasePractices.repo.database.DatabaseManager
import com.deloitte.testCasePractices.ui.FetchViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A file that defines the various module definitions
 */

private const val RETROFIT = "RETROFIT"
private const val DATABASE = "DATABASE"
private const val HTTP_CLIENT = "HTTP_CLIENT"
private const val HTTP_LOGGING = "HTTP_LOGGING"
private const val GSON = "GSON"

/**
 * Definition of ViewModel module
 */
val fetchVMModule = module {
    viewModel { FetchViewModel(get()) }
}

/**
 * Definition of repository module
 */
val repoModule = module {
    single(named(DATABASE)) { DatabaseManager() }
    single<Repo> {
        RepoImpl(createWebService<API>(get(named(RETROFIT))), get(named(DATABASE)))
    }
}

/**
 * Definition of the network module to be used for Dependency Injection using Koin
 */
val networkModule = module {
    single(named(RETROFIT)) { provideRetrofit(get(named(HTTP_CLIENT)), get(named(GSON))) }

    factory(named(HTTP_LOGGING)) { providesHttplogging() }

    factory(named(HTTP_CLIENT)) { providesOkHttpClient(get(named(HTTP_LOGGING))) }

    single(named(GSON)) { GsonBuilder().create() }
}

/**
 * provides the Retrofit instance
 * @param okHttpClient the okHttpClient
 * @param gson the Gson
 */
fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder().baseUrl(URLConstants.BASE_URL).client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
}

/**
 * provides the OkHttpClient instance
 * @param interceptor the interceptor
 */
fun providesOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS).addInterceptor(interceptor).build()
}

/**
 * Provides HttpLoggingInterceptor dependency.
 */
fun providesHttplogging(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

/**
 * Create an implementation of the API endpoints defined by the service interface.
 */
inline fun <reified T> createWebService(retrofit: Retrofit): T = retrofit.create(T::class.java)

