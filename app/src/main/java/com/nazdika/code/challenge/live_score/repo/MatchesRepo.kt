package com.nazdika.code.challenge.live_score.repo

import com.nazdika.code.challenge.live_score.LiveScoreActivity
import com.nazdika.code.challenge.network.ApiClient
import com.nazdika.code.challenge.network.SimpleResponse

class MatchesRepo constructor(
    private val apiClient: ApiClient,
) {

    suspend fun liveMatches(
        timeZoneOffset: String,
        xUDID: String,
    ): SimpleResponse<LiveScoreActivity.SoccerMatchesResultPojo> {
        return apiClient.liveMatches(timeZoneOffset, xUDID)
    }

}