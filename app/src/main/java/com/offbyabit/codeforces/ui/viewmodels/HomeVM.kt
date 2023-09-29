package com.offbyabit.codeforces.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.offbyabit.codeforces.utils.models.userRatings.UserRatings
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeVM : ViewModel() {
    var userInfo: UserInfo? by mutableStateOf(null)
        private set
    var userRatings: UserRatings? by mutableStateOf(null)
        private set

    init {
        val handle = MMKV.defaultMMKV().decodeString("handle") ?: "YouKn0wWho"
        val retrofit = Retrofit.Builder()
            .baseUrl("https://codeforces.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(CodeForcesAPI::class.java)

        viewModelScope.launch {
            try {
                userInfo = apiService.getUserInfo(handle).run {
                    when (this.status) {
                        "OK" -> this
                        else -> null
                    }
                }
            } catch (_: Exception) {
            }
        }

        viewModelScope.launch {
            try {
                userRatings = apiService.getUserRatings(handle).run {
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