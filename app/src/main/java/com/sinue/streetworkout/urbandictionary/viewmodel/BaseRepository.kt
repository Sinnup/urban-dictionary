package com.sinue.streetworkout.urbandictionary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sinue.streetworkout.urbandictionary.networking.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

open class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data

    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : Result<T>{
        try {
            val response = call.invoke()
            if(response .isSuccessful) return Result.Success(response.body()!!)
        }
        catch (e: HttpException) {
            return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))

        } catch (e: Throwable) {
            return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
        }

        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}