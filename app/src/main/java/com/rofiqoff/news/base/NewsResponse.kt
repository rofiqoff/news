package com.rofiqoff.news.base

sealed interface NewsResponse<out T> {
    object Loading : NewsResponse<Nothing>

    open class Error(
        open val message: String,
    ) : NewsResponse<Nothing>

    class Success<T>(
        val data: T,
    ) : NewsResponse<T>
}
