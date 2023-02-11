package com.rofiqoff.news.base

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NoInternetError : NewsResponse.Error(message = "No Internet")

object TimeOutError : NewsResponse.Error(message = "Time Out")

data class HttpError(
    override val message: String,
    val code: Int,
) : NewsResponse.Error(message = message)

fun Exception.toError(): NewsResponse.Error {
    return try {
        when {
            this is IOException &&
                    (message?.contains("Failed to connect to", true) == true) -> {
                NoInternetError
            }
            this is SocketTimeoutException -> {
                TimeOutError
            }
            this is UnknownHostException -> {
                NoInternetError
            }
            this is HttpException -> {
                val error = Gson().fromJson(
                    response()?.errorBody()?.string().orEmpty(),
                    RawHttpError::class.java,
                )

                HttpError(
                    message = error.message ?: message(),
                    code = code(),
                )
            }
            else -> {
                NewsResponse.Error(message = message.orEmpty())
            }
        }
    } catch (e: Exception) {
        NewsResponse.Error(message = e.message.orEmpty())
    }
}
