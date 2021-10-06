package com.makeus.jfive.famo.src.common

sealed class Resource<T>(val data:T?=null,val throwable: Throwable?=null){
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(throwable: Throwable?,data:T?=null) :Resource<T>(data,throwable)
    class Loading<T>:Resource<T>()
    class Loaded<T>:Resource<T>()
}
