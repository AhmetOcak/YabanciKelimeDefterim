package com.yabancikelimedefteri.core.helpers

sealed interface Response<out T> {
    class Success<T>(val data: T) : Response<T>
    class Error<T>(val message: String) : Response<T>
    object Loading : Response<Nothing>
}