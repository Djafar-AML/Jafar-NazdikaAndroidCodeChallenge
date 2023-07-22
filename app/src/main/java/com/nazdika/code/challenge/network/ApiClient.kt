package com.nazdika.code.challenge.network

import com.nazdika.code.challenge.live_score.LiveScoreActivity
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val nazdikaApi: NazdikaApi
) {

    suspend fun liveMatches(
        timeZoneOffset: String,
        xUDID: String,
    ): SimpleResponse<LiveScoreActivity.SoccerMatchesResultPojo> {
        return safeApiCall {
            nazdikaApi.liveMatches(timeZoneOffset, xUDID)
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {

        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }

    }

}