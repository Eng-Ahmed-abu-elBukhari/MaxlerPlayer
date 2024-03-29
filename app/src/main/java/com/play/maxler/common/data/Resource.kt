package com.play.maxler.common.data


/*sealed class Result<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
    class Loading<T>(data: T? = null) : Result<T>(data)
}*/

sealed class Resource<out T : Any> {
    object Loading  : Resource<Nothing>()
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}
