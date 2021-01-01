package com.example.weather.data.api

import retrofit2.Response

abstract class BaseService {

    protected abstract fun parseCustomError(responseMessage: String, responseCode: Int, errorBodyJson: String): Exception

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return Result.Error(mapNetworkThrowable(t))
        }

        return if (!response.isSuccessful) {
            val errorBody = response.errorBody()
            Result.Error(parseCustomError(response.message(), response.code(), errorBody?.string() ?: ""))
        } else {
            return if (response.body() == null) {
                //Result.Error(NullBodyException(ApiError("response.body() can't be null")))
                Result.Error(Exception("response.body() can't be null"))
            } else {
                Result.Success(response.body()!!)
            }
        }
    }

    protected suspend fun <T : Any> apiCallResource(call: suspend () -> Response<T>): Resource<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return Resource.error(t.message.toString(), null)
        }

        return if (!response.isSuccessful) {
            val errorBody = response.errorBody()
            Resource.error(msg = errorBody?.string() ?: "", data = null)
        } else {
            return if (response.body() == null) {
                Resource.error("Não retornou resultado", null)
            } else {
                Resource.success(response.body())
            }
        }
    }

    private fun mapNetworkThrowable(throwable: Throwable): Exception {
        return Exception("Erro NetWork map: $throwable")
    }

    protected fun mapHttpThrowable(throwable: Throwable, code: Int, message: String): Exception {
        return Exception("Erro Http map")
    }
}