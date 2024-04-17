package com.idmt.simplevoice.ui.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.os.trace
import com.google.gson.GsonBuilder
import com.upwork.googlesheetreader.network.model.spreadsheet.SpreadSheetResponse
import com.upwork.googlesheetreader.network.model.spreadsheetDetails.SpreadSheetDetails
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.GzipSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


/**
 * Retrofit API declaration for MovieDb Network API
 */
const val API_KEY = "AIzaSyD8rAG-D7tTFCJfJiO7JFMUc_fHD7e58QQ"

interface RetrofitNetworkApi {
    @GET(value = "/v4/spreadsheets/1SMrpeJC2isCTJotRYXBDNENNbDVzCcazonOOwUQ-Vf0?fields=sheets(properties(title))&key=${API_KEY}")
    suspend fun getSpreadSheetData(
    ): SpreadSheetResponse

    @GET(value = "/v4/spreadsheets/1SMrpeJC2isCTJotRYXBDNENNbDVzCcazonOOwUQ-Vf0/values/{name-sheet}!A2:E150?key=${API_KEY}")
    suspend fun getDetailsOfSpreadSheet(
        @Path("name-sheet") nameSheet: String
    ): SpreadSheetDetails

    @POST(value = "/macros/s/AKfycbzZLwVZ90eI-5gVZYZO74GAjK7v5y07ZzGhiqIBqF6bK2Va4812aoHil3c6I5OQTYy2/exec")
    suspend fun postDataToSpreadSheet(
        @Query("spreadsheetName") spreadsheetName: String,
        @Query("playerFirstName") playerFirstName: String,
        @Query("playerSecondName") playerSecondName: String,
        @Query("age") age: String,
        @Query("position") position: String

    ): retrofit2.Call<Unit>


}


object RetrofitMoviesNetworkApi {

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(LoggingInterceptor())
        .build()

    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .callFactory {
            okHttpCallFactory().newCall(it)
        }
        .baseUrl("https://sheets.googleapis.com")
        .build()


    private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .callFactory {
            okHttpCallFactory().newCall(it)
        }

        .baseUrl("https://script.google.com")
        .build()

    val retrofitService: RetrofitNetworkApi by lazy {
        retrofit.create(RetrofitNetworkApi::class.java)
    }
    val retrofitService2: RetrofitNetworkApi by lazy {
        retrofit2.create(RetrofitNetworkApi::class.java)
    }

    fun okHttpCallFactory(): Call.Factory = trace("") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()

                    .apply {

                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    },
            )
            .build()
    }

    suspend fun getSpreadSheetData() =
        retrofitService.getSpreadSheetData()

    suspend fun getSpreadSheetDataDetails(sheetname: String) =
        retrofitService.getDetailsOfSpreadSheet(sheetname)


    suspend fun postDataToSpreadSheet(
        spreadsheetName: String,
        playerFirstName: String,
        playerSecondName:String,
        age: String,
        position: String
    ) =
        retrofitService2.postDataToSpreadSheet(
            spreadsheetName,
            playerFirstName = playerFirstName,
            playerSecondName=playerSecondName,
            age = age,
            position
        )
}

class ErrorResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val code = response.code
        try {

            if (code in 400..500) {
                responseBody(response)?.also { errorString ->
                    Log.e("error", errorString)

                }
            }
        } catch (e: Exception) {
            Log.e("error", e.message.toString())

        }

        return chain.proceed(chain.request())
    }

    private fun responseBody(response: Response): String? {
        val responseBody = response.body ?: return null
        val contentLength = responseBody.contentLength()

        if (contentLength == 0L) {
            return null
        }

        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        var buffer = source.buffer()
        val headers = response.headers

        if ("gzip".equals(headers.get("Content-Encoding"), ignoreCase = true)) {
            var gzippedResponseBody: GzipSource? = null
            try {
                gzippedResponseBody = GzipSource(buffer.clone())
                buffer = okio.Buffer()
                buffer.writeAll(gzippedResponseBody)
            } finally {
                gzippedResponseBody?.close()
            }
        }

        val charset: Charset = responseBody.contentType()?.charset(UTF8) ?: UTF8
        return buffer.clone().readString(charset)
    }

    private companion object {
        val UTF8: Charset = Charset.forName("UTF-8")
    }
}


internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.d(
            "OkHttp", java.lang.String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.d(
            "OkHttp", java.lang.String.format(
                "Received response for %s in %.1fms%n%s",
                response.request.url, (t2 - t1) / 1e6, response.headers
            )
        )
        return response
    }
}

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}