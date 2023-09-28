package com.offbyabit.codeforces.utils.models

import com.offbyabit.codeforces.utils.models.contestList.ContestList
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.offbyabit.codeforces.utils.models.userRatings.UserRatings
import retrofit2.http.GET
import retrofit2.http.Query

interface CodeForcesAPI {
    // https://codeforces.com/api/user.info?handles=Fefer_Ivan
    @GET("user.info")
    suspend fun getUserInfo(@Query("handles") handle: String): UserInfo

    // https://codeforces.com/api/user.rating?handle=Fefer_Ivan
    @GET("user.rating")
    suspend fun getUserRatings(@Query("handle") handle: String): UserRatings

    // https://codeforces.com/api/contest.list?gym=true
    @GET("contest.list")
    suspend fun getContestList(
//        @Query("gym") gym: Boolean = true
    ): ContestList
}