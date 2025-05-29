package com.example.zariya.repositories

import com.example.zariya.models.Application
import com.example.zariya.models.ApplicationsResponse
import com.example.zariya.models.JobPostRequest
import com.example.zariya.models.JobPostResponse
import com.example.zariya.models.JobX
import com.example.zariya.service.RecruiterApi
import com.example.zariya.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.SocketTimeoutException
import javax.inject.Inject

class RecruiterRepository @Inject constructor(private val recruiterApi: RecruiterApi) {
    private val _jobsLiveData = MutableStateFlow<NetworkResult<List<JobX>>>(NetworkResult.Start())
    val jobsLiveData: StateFlow<NetworkResult<List<JobX>>>
        get() = _jobsLiveData

    private val _jobPostLiveData = MutableStateFlow<NetworkResult<JobPostResponse>>(NetworkResult.Start())
    val jobPostLiveData: StateFlow<NetworkResult<JobPostResponse>>
        get() = _jobPostLiveData

    // Candidates data
    private val _appliedCandidatesLiveData = MutableStateFlow<NetworkResult<List<Application>>>(NetworkResult.Start())
    val appliedCandidatesLiveData: StateFlow<NetworkResult<List<Application>>>
        get() = _appliedCandidatesLiveData

    private val _approvedCandidatesLiveData = MutableStateFlow<NetworkResult<List<Application>>>(NetworkResult.Start())
    val approvedCandidatesLiveData: StateFlow<NetworkResult<List<Application>>>
        get() = _approvedCandidatesLiveData

    private val _hiredCandidatesLiveData = MutableStateFlow<NetworkResult<List<Application>>>(NetworkResult.Start())
    val hiredCandidatesLiveData: StateFlow<NetworkResult<List<Application>>>
        get() = _hiredCandidatesLiveData


    suspend fun fetchJobs(token: String) {
        _jobsLiveData.value = NetworkResult.Loading()
        try {
            val response = recruiterApi.getJobsForRecruiters("Bearer $token")
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _jobsLiveData.value = NetworkResult.Success(responseBody.jobs)
                } else {
                    _jobsLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _jobsLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _jobsLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _jobsLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _jobsLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }


    suspend fun postJob(token: String, request: JobPostRequest) {
        _jobPostLiveData.value = NetworkResult.Loading()
        try {
            val response = recruiterApi.postJob("Bearer $token", request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _jobPostLiveData.value = NetworkResult.Success(responseBody)
                } else {
                    _jobPostLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _jobPostLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _jobPostLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _jobPostLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _jobPostLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }

    suspend fun fetchCandidates(token: String, jobId: String) {
        _appliedCandidatesLiveData.value = NetworkResult.Loading()
        _approvedCandidatesLiveData.value = NetworkResult.Loading()
        _hiredCandidatesLiveData.value = NetworkResult.Loading()

        try {
            // Fetch all three types of candidates
            val appliedResponse = recruiterApi.getAppliedCandidates("Bearer $token", jobId)
            val approvedResponse = recruiterApi.getApprovedCandidates("Bearer $token", jobId)
            val hiredResponse = recruiterApi.getHiredCandidates("Bearer $token", jobId)

            // Handle applied candidates
            handleCandidateResponse(appliedResponse, _appliedCandidatesLiveData, "applied")

            // Handle approved candidates
            handleCandidateResponse(approvedResponse, _approvedCandidatesLiveData, "approved")

            // Handle hired candidates
            handleCandidateResponse(hiredResponse, _hiredCandidatesLiveData, "hired")

        } catch (e: SocketTimeoutException) {
            setAllCandidateStates(NetworkResult.Error("Please try again!"))
        } catch (e: Exception) {
            setAllCandidateStates(NetworkResult.Error("Unexpected error occurred"))
        }
    }

    private suspend fun handleCandidateResponse(
        response: retrofit2.Response<ApplicationsResponse>,
        stateFlow: MutableStateFlow<NetworkResult<List<Application>>>,
        type: String
    ) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                stateFlow.value = NetworkResult.Success(responseBody.applications)
            } else {
                stateFlow.value = NetworkResult.Error("$type candidates response body is null")
            }
        } else if (response.errorBody() != null) {
            try {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                stateFlow.value = NetworkResult.Error(errObj.getString("message"))
            } catch (e: Exception) {
                stateFlow.value = NetworkResult.Error("Failed to load $type candidates")
            }
        } else {
            stateFlow.value = NetworkResult.Error("Failed to load $type candidates")
        }
    }

    private fun setAllCandidateStates(state: NetworkResult<List<Application>>) {
        _appliedCandidatesLiveData.value = state
        _approvedCandidatesLiveData.value = state
        _hiredCandidatesLiveData.value = state
    }
}