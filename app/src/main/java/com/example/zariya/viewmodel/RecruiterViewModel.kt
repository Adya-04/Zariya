package com.example.zariya.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zariya.models.Application
import com.example.zariya.models.JobPostRequest
import com.example.zariya.models.JobPostResponse
import com.example.zariya.models.JobX
import com.example.zariya.pref.PrefDatastore
import com.example.zariya.repositories.RecruiterRepository
import com.example.zariya.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruiterViewModel @Inject constructor(
    private val repository: RecruiterRepository,
    private val prefs: PrefDatastore
): ViewModel() {

    val token: Flow<String> = prefs.getToken()

    val jobsLiveData : StateFlow<NetworkResult<List<JobX>>>
        get() = repository.jobsLiveData

    val appliedCandidatesLiveData : StateFlow<NetworkResult<List<Application>>>
        get() = repository.appliedCandidatesLiveData

    val approvedCandidatesLiveData : StateFlow<NetworkResult<List<Application>>>
        get() = repository.approvedCandidatesLiveData

    val hiredCandidatesLiveData : StateFlow<NetworkResult<List<Application>>>
        get() = repository.hiredCandidatesLiveData

    fun fetchJobs(token: String) {
        viewModelScope.launch {
            repository.fetchJobs(token)
        }
    }

    val jobPostLiveData : StateFlow<NetworkResult<JobPostResponse>>
        get() = repository.jobPostLiveData

    fun postJob(token: String, request: JobPostRequest) {
        viewModelScope.launch {
            repository.postJob(token, request)
        }
    }

    fun fetchCandidates(token: String, jobId: String) {
        viewModelScope.launch {
            Log.d("TAG", "fetchingCandidates: $jobId")
                repository.fetchCandidates(token, jobId)
        }
    }
}