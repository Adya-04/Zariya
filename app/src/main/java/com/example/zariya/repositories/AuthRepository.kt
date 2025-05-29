package com.example.zariya.repositories

import android.util.Log
import com.example.zariya.models.CompanyRegResponse
import com.example.zariya.models.CompanyRegisRequest
import com.example.zariya.models.LoginRequest
import com.example.zariya.models.LoginResponse
import com.example.zariya.models.SignupRequest
import com.example.zariya.models.SignupResponse
import com.example.zariya.service.AuthApi
import com.example.zariya.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApi: AuthApi) {

    private val _loginResultLiveData =
        MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Start())
    val loginResultLiveData: StateFlow<NetworkResult<LoginResponse>>
        get() = _loginResultLiveData

    private val _signupLiveData =
        MutableStateFlow<NetworkResult<SignupResponse>>(NetworkResult.Start())
    val signupLiveData: StateFlow<NetworkResult<SignupResponse>>
        get() = _signupLiveData

    private val _registerCompanyLiveData =
        MutableStateFlow<NetworkResult<CompanyRegResponse>>(NetworkResult.Start())
    val registerCompanyLiveData: StateFlow<NetworkResult<CompanyRegResponse>>
        get() = _registerCompanyLiveData

    suspend fun login(loginRequest: LoginRequest) {
        _loginResultLiveData.value = NetworkResult.Loading()
        try {
            val response = authApi.loginUser(loginRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _loginResultLiveData.value = NetworkResult.Success(responseBody)
                } else {
                    _loginResultLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _loginResultLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _loginResultLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _loginResultLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            Log.d("TAG", "login: ${e.message}")
            _loginResultLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }

    suspend fun signup(signupRequest: SignupRequest) {
        _signupLiveData.value = NetworkResult.Loading()
        try {
            val response = authApi.signup(signupRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _signupLiveData.value = NetworkResult.Success(responseBody)
                } else {
                    _signupLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _signupLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _signupLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _signupLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _signupLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }

    suspend fun registerCompany(token : String,registerCompanyRequest: CompanyRegisRequest) {
        _registerCompanyLiveData.value = NetworkResult.Loading()
        try {
            val response = authApi.registerCompany("Bearer $token",registerCompanyRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _registerCompanyLiveData.value = NetworkResult.Success(responseBody)
                } else {
                    _registerCompanyLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _registerCompanyLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _registerCompanyLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _registerCompanyLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _registerCompanyLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }
}