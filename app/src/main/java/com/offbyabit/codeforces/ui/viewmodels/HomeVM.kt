package com.offbyabit.codeforces.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offbyabit.codeforces.themeChangeCount
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.Constants
import com.offbyabit.codeforces.utils.models.Rank
import com.offbyabit.codeforces.utils.models.RankStrings
import com.offbyabit.codeforces.utils.models.getKeyColor
import com.offbyabit.codeforces.utils.models.getRank
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.offbyabit.codeforces.utils.models.userRatings.UserRatings
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeVM : ViewModel() {
    var userInfo: UserInfo? by mutableStateOf(null)
        private set
    var userRatings: UserRatings? by mutableStateOf(null)
        private set
    private var _handle: String by mutableStateOf(
        MMKV.defaultMMKV().decodeString(Constants.PrefTags.handle) ?: "YouKn0wWho"
    )

    var handle: String
        get() = _handle
        set(value) {
            var successful = false
            _handle = value

            viewModelScope.launch(Dispatchers.IO) {
                successful = updateInfoAndRatings(value)

//                if (successful) {
                MMKV.defaultMMKV().encode(
                    Constants.PrefTags.rankName,
                    userInfo?.result?.get(0)?.rank ?: RankStrings.Master
                )
                Log.d("theme-debug", "changing")
                themeChangeCount.value++
//                }
            }
        }

    private fun updateInfoAndRatings(value: String = _handle): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://codeforces.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(CodeForcesAPI::class.java)
        var successful = true
        viewModelScope.launch {
            try {
                userInfo = apiService.getUserInfo(value).run {
                    when (this.status) {
                        "OK" -> this
                        else -> null
                    }
                }
            } catch (_: Exception) {
                successful = successful and false
            }
        }

        viewModelScope.launch {
            try {
                userRatings = apiService.getUserRatings(value).run {
                    when (this.status) {
                        "OK" -> this
                        else -> null
                    }
                }
            } catch (_: Exception) {
                successful = successful and false

            }
        }
        return successful
    }

    init {
        updateInfoAndRatings()
    }
}