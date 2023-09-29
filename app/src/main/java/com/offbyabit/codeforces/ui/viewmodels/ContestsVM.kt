package com.offbyabit.codeforces.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.contestList.ContestList
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.offbyabit.codeforces.utils.models.userRatings.UserRatings
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContestsVM: ViewModel() {
    var contestList by mutableStateOf<ContestList?>(null)
        private set

    init {
        Log.d("debug-tag", "re-fetched contests from the web")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://codeforces.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(CodeForcesAPI::class.java)

        viewModelScope.launch {
            try {
                contestList = apiService.getContestList().run {
                    when (this.status) {
                        "OK" -> this
                        else -> null
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}