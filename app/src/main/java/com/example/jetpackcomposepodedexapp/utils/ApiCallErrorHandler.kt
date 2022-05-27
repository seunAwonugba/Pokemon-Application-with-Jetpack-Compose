package com.example.jetpackcomposepodedexapp.utils

sealed class ApiCallErrorHandler<T>(
    val data : T? = null,
    val message : String? =  null
) {
    //when success, do this
    class Success<T>(data: T) : ApiCallErrorHandler<T>(data,null)
    //when error, get the error message
    class Error<T>(message: String) : ApiCallErrorHandler<T>(null, message)

    //passes default null
    class Loading<T> : ApiCallErrorHandler<T>()
}