package com.example.zariya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zariya.models.CompanyRegResponse
import com.example.zariya.models.CompanyRegisRequest
import com.example.zariya.models.LoginRequest
import com.example.zariya.models.LoginResponse
import com.example.zariya.models.SignupRequest
import com.example.zariya.models.SignupResponse
import com.example.zariya.pref.PrefDatastore
import com.example.zariya.repositories.AuthRepository
import com.example.zariya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefs: PrefDatastore
) : ViewModel() {
    val loginResultLiveData: StateFlow<NetworkResult<LoginResponse>>
        get() = authRepository.loginResultLiveData

    val signupLiveData: StateFlow<NetworkResult<SignupResponse>>
        get() = authRepository.signupLiveData

    val registerCompanyLiveData: StateFlow<NetworkResult<CompanyRegResponse>>
        get() = authRepository.registerCompanyLiveData

    val token: Flow<String> = prefs.getToken()
    val role: Flow<String> = prefs.getRole()

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.login(loginRequest)
        }
    }

    fun signup(signupRequest: SignupRequest) {
        viewModelScope.launch {
            authRepository.signup(signupRequest)
        }
    }

    fun registerCompany(token: String, registerCompanyRequest: CompanyRegisRequest) {
        viewModelScope.launch {
            authRepository.registerCompany(token, registerCompanyRequest)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            prefs.saveToken(token)
        }
    }

    fun saveRole(role: String){
        viewModelScope.launch {
            prefs.saveRole(role)
        }
    }
}