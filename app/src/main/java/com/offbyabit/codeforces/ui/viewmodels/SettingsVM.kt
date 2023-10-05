package com.offbyabit.codeforces.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class SettingsVM : ViewModel() {
    var isWrongId by mutableStateOf(true)

    fun changeHandle(handle: String) {
        val kv = MMKV.defaultMMKV()
        kv.encode("handle", handle)
//        kv.encode("key_color", handle)
    }

    fun checkIfHandleIsValid(handle: String) {
        isWrongId = true
        viewModelScope.launch(Dispatchers.IO) {
            val y = try {
                URL(
                    "https://codeforces.com/api/" +
                            "user.status?handle=$handle&count=1"
                ).readText()
                true
            } catch (_: Exception) {
                false
            }
//            val x = JSONObject(text).getString("status") == "OK"

            isWrongId = !y
        }
    }
}