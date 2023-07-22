package com.nazdika.code.challenge.network

import com.nazdika.code.challenge.live_score.LiveScoreActivity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NazdikaApi {

    @GET("foot/match/live")
    suspend fun liveMatches(
        @Header("X-Seconds-From-UTC") timeZoneOffset: String,
        @Header("X-UDID") xUDID: String,
    ): Response<LiveScoreActivity.SoccerMatchesResultPojo>

}