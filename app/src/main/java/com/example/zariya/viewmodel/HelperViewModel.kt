package com.example.zariya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zariya.models.CreateApplicationRequest
import com.example.zariya.models.CreateApplicationResponse
import com.example.zariya.models.JobY
import com.example.zariya.pref.PrefDatastore
import com.example.zariya.repositories.HelperRepository
import com.example.zariya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelperViewModel @Inject constructor(
    private val helperRepository: HelperRepository,
    private val prefs: PrefDatastore
) : ViewModel(){
    val jobsLiveData: StateFlow<NetworkResult<List<JobY>>>
        get() = helperRepository.jobsLiveData

    val applicationLiveData: StateFlow<NetworkResult<CreateApplicationResponse>>
        get() = helperRepository.applicationLiveData

    val token: Flow<String> = prefs.getToken()
    val role: Flow<String> = prefs.getRole()

    fun fetchJobs(token: String) {
        viewModelScope.launch {
            helperRepository.fetchJobs(token)
        }
    }

    fun submitApplication(
        token: String,
        request: CreateApplicationRequest
    ) {
        viewModelScope.launch {
            helperRepository.submitApplication(token, request)
        }
    }
}