package com.nazdika.code.challenge.live_score.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazdika.code.challenge.androidId
import com.nazdika.code.challenge.live_score.LiveScoreActivity
import com.nazdika.code.challenge.live_score.repo.MatchesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val matchesRepo: MatchesRepo,
) : ViewModel() {

    private val _liveMatches = MutableLiveData<LiveScoreActivity.SoccerMatchesResultPojo>()
    val liveMatches: LiveData<LiveScoreActivity.SoccerMatchesResultPojo> = _liveMatches

    private val _liveMatchesError = MutableLiveData<String>()
    val liveMatchesError: LiveData<String> = _liveMatchesError

    init {

        viewModelScope.launch(Dispatchers.IO) {

            val result = matchesRepo.liveMatches(
                getTimeZoneOffsetFromUTC().toString(),
                androidId
            )

            if (result.isSuccessful) {
                // show the reuslt
                _liveMatches.postValue(result.body)

            } else {
                // show toast message
                _liveMatchesError.postValue("خطا در دریافت اطلاعات")

            }
        }

    }


    private fun getTimeZoneOffsetFromUTC(): Int {
        val tz: TimeZone = TimeZone.getDefault()
        val now = Date()
        return tz.getOffset(now.time) / 1000
    }

}