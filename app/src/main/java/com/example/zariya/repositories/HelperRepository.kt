package com.example.zariya.repositories

import com.example.zariya.models.CreateApplicationRequest
import com.example.zariya.models.CreateApplicationResponse
import com.example.zariya.models.JobY
import com.example.zariya.service.HelperApi
import com.example.zariya.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.SocketTimeoutException
import javax.inject.Inject

class HelperRepository @Inject constructor(private val helperApi: HelperApi) {
    private val _jobsLiveData = MutableStateFlow<NetworkResult<List<JobY>>>(NetworkResult.Start())
    val jobsLiveData: StateFlow<NetworkResult<List<JobY>>>
        get() = _jobsLiveData

    private val _applicationLiveData =
        MutableStateFlow<NetworkResult<CreateApplicationResponse>>(NetworkResult.Start())
    val applicationLiveData: StateFlow<NetworkResult<CreateApplicationResponse>>
        get() = _applicationLiveData

    suspend fun fetchJobs(token: String) {
        _jobsLiveData.value = NetworkResult.Loading()
        try {
            val response = helperApi.getJobsForHelpers("Bearer $token")
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _jobsLiveData.value = NetworkResult.Success(responseBody.jobs)
                } else {
                    _jobsLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
//                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
//                _jobsLiveData.value = NetworkResult.Error(errObj.getString("message"))
                _jobsLiveData.value = NetworkResult.Error("Network Request failed")
            } else {
                _jobsLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _jobsLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _jobsLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }

    suspend fun submitApplication(
        token: String,
        request: CreateApplicationRequest
    ) {
        _applicationLiveData.value = NetworkResult.Loading()
        try {
            val response = helperApi.createApplication("Bearer $token", request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _applicationLiveData.value = NetworkResult.Success(responseBody)
                } else {
                    _applicationLiveData.value = NetworkResult.Error("Response body is null")
                }
            } else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _applicationLiveData.value = NetworkResult.Error(errObj.getString("message"))
            } else {
                _applicationLiveData.value = NetworkResult.Error("Something went wrong")
            }
        } catch (e: SocketTimeoutException) {
            _applicationLiveData.value = NetworkResult.Error("Please try again!")
        } catch (e: Exception) {
            _applicationLiveData.value = NetworkResult.Error("Unexpected error occurred")
        }
    }
}