package com.nazdika.code.challenge.live_score

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.nazdika.code.challenge.TodayMatchesAdapter
import com.nazdika.code.challenge.databinding.ActivityLiveScoreBinding
import com.nazdika.code.challenge.live_score.vm.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import java.util.*

@AndroidEntryPoint
class LiveScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveScoreBinding
    private lateinit var todayMatchesAdapter: TodayMatchesAdapter
    private val matchesViewModel: MatchesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todayMatchesAdapter = TodayMatchesAdapter(applicationContext)
        binding.rvTodayMatches.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvTodayMatches.adapter = todayMatchesAdapter
        observeLiveData()
    }

    private fun observeLiveData() {

        matchesViewModel.liveMatches.observe(this) { todayMatches ->
            binding.progressbar.visibility = View.GONE
            onTodayMatchesResponse(todayMatches)
        }

        matchesViewModel.liveMatchesError.observe(this) {
            binding.progressbar.visibility = View.GONE
            Toast.makeText(
                this@LiveScoreActivity,
                it,
                Toast.LENGTH_LONG
            )
                .show()
        }

    }

    private fun onTodayMatchesResponse(todayMatches: SoccerMatchesResultPojo?) {
        runOnUiThread {
            if (todayMatches != null) {
                if (todayMatches.success) {
                    showTodayMatches(todayMatches.data?.competitionMatches ?: return@runOnUiThread)
                } else {
                    Toast.makeText(this, "Error in load data!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showTodayMatches(competitionMatches: List<CompetitionMatchPojo?>) {
        todayMatchesAdapter.addItems(buildList {
            competitionMatches.forEach { competitionMatch ->
                add(
                    CompetitionMatchModel(
                        competitionMatch?.competitionId,
                        competitionMatch?.persianName,
                        competitionMatch?.logo,
                        competitionMatch?.localizedName
                    )
                )
                addAll(competitionMatch?.matches?.map {
                    MatchModel(
                        CompetitionMatchModel(
                            competitionMatch.competitionId,
                            competitionMatch.persianName,
                            competitionMatch.logo,
                            competitionMatch.localizedName
                        ),
                        it.matchId,
                        it.homeTeamScore,
                        it.awayTeamScore,
                        it.homeTeamPen,
                        it.awayTeamPen,
                        it.matchStarted,
                        it.liveUrl,
                        it.liveStreamUrl,
                        it.hasVideo,
                        it.matchEnded,
                        it.hasLive,
                        it.shortDateText,
                        it.hasDetails,
                        it.status,
                        TeamModel(
                            it.homeTeam?.teamId,
                            it.homeTeam?.englishName,
                            it.homeTeam?.persianName,
                            it.homeTeam?.logo,
                            it.homeTeam?.localizedName
                        ),
                        TeamModel(
                            it.awayTeam?.teamId,
                            it.awayTeam?.englishName,
                            it.awayTeam?.persianName,
                            it.awayTeam?.logo,
                            it.awayTeam?.localizedName
                        )
                    )
                } ?: return@forEach)
            }
        })
    }

    data class SoccerMatchesResultPojo(
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("data")
        val data: SoccerMatchesResultDataPojo? = null
    )

    data class SoccerMatchesResultDataPojo(
        @SerializedName("competition_matches")
        val competitionMatches: List<CompetitionMatchPojo?>? = null
    )

    data class CompetitionMatchPojo(
        @SerializedName("competition_id")
        val competitionId: Int? = null,
        @SerializedName("name_fa")
        val persianName: String? = null,
        @SerializedName("logo")
        val logo: String? = null,
        @SerializedName("localized_name")
        val localizedName: String? = null,
        @SerializedName("matches")
        val matches: List<MatchPojo>? = null
    )

    data class MatchPojo(
        @SerializedName("match_id")
        val matchId: Long? = null,
        @SerializedName("home_team_score")
        val homeTeamScore: Int? = null,
        @SerializedName("away_team_score")
        val awayTeamScore: Int? = null,
        @SerializedName("home_team_pen")
        val homeTeamPen: Int? = null,
        @SerializedName("away_team_pen")
        val awayTeamPen: Int? = null,
        @SerializedName("match_started")
        val matchStarted: Boolean? = null,
        @SerializedName("live_url")
        val liveUrl: String? = null,
        @SerializedName("live_stream_url")
        val liveStreamUrl: String? = null,
        @SerializedName("has_video")
        val hasVideo: Boolean? = null,
        @SerializedName("match_ended")
        val matchEnded: Boolean? = null,
        @SerializedName("has_live")
        val hasLive: Boolean? = null,
        @SerializedName("short_date_text")
        val shortDateText: String? = null,
        @SerializedName("has_details")
        val hasDetails: Boolean? = null,
        @SerializedName("status")
        val status: String? = null,
        @SerializedName("home_team")
        val homeTeam: TeamPojo? = null,
        @SerializedName("away_team")
        val awayTeam: TeamPojo? = null
    )

    data class TeamPojo(
        @SerializedName("team_id")
        val teamId: Long? = null,
        @SerializedName("name_en")
        val englishName: String? = null,
        @SerializedName("name_fa")
        val persianName: String? = null,
        @SerializedName("logo")
        val logo: String? = null,
        @SerializedName("localized_name")
        val localizedName: String? = null
    )

    interface ItemType {
        val itemType: Int
    }

    @Parcelize
    data class CompetitionMatchModel(
        val competitionId: Int? = null,
        val persianName: String? = null,
        val logo: String? = null,
        val localizedName: String? = null,
        override val itemType: Int = 0
    ) : ItemType, Parcelable

    @Parcelize
    data class MatchModel(
        val competition: CompetitionMatchModel? = null,
        val matchId: Long? = null,
        val homeTeamScore: Int? = null,
        val awayTeamScore: Int? = null,
        val homeTeamPen: Int? = null,
        val awayTeamPen: Int? = null,
        val matchStarted: Boolean? = null,
        val liveUrl: String? = null,
        val liveStreamUrl: String? = null,
        val hasVideo: Boolean? = null,
        val matchEnded: Boolean? = null,
        val hasLive: Boolean? = null,
        val shortDateText: String? = null,
        val hasDetails: Boolean? = null,
        val status: String? = null,
        val homeTeam: TeamModel? = null,
        val awayTeam: TeamModel? = null,
        override val itemType: Int = 1
    ) : ItemType, Parcelable

    @Parcelize
    data class TeamModel(
        val teamId: Long? = null,
        val englishName: String? = null,
        val persianName: String? = null,
        val logo: String? = null,
        val localizedName: String? = null
    ) : Parcelable
}