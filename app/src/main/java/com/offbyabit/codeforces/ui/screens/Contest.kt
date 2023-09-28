package com.offbyabit.codeforces.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.contestList.ContestList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contest(navController: NavController) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://codeforces.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(CodeForcesAPI::class.java)
    val networkScope = CoroutineScope(Dispatchers.IO)

    var contestList by remember { mutableStateOf<ContestList?>(null) }
    LaunchedEffect(true) {
        networkScope.launch {
            try {
                val x = apiService.getContestList()
                if (x.status == "OK") {
                    contestList = x
                }
            } catch (_: Exception) {
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contests") }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
//            Text(
//                modifier = Modifier.padding(
//                    start = 32.dp,
//                    top = 32.dp
//                ),
//                text = "Contests",
//                fontFamily = FontFamily.Monospace,
//                style = MaterialTheme.typography.titleLarge
//            )
            if (contestList != null) {
                if (contestList!!.status == "OK") {
                    val contests = contestList!!.result
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()

                    ) {
                        itemsIndexed(contests) { index, item ->
                            if (index != 0) {
                                Divider()
                            }
                            ContestCard(contest = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContestCard(
    contest: com.offbyabit.codeforces.utils.models.contestList.Result
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        ConditionalText(
            value = contest.name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ConditionalText(
            pattern = "by $$$",
            value = contest.preparedBy
        )
        ConditionalText(
            pattern = "starts at: $$$",
            value = contest.startTimeSeconds?.toDateString(),
            overflow = TextOverflow.Ellipsis
        )
        ConditionalText(
            pattern = "will run for $$$ hours",
            value = (contest.durationSeconds / 3600).toString()
        )
    }
}

fun Int?.toDateString(
    format: String = "yyyy, MMM d h:mm a"
): String? {
    if (this == null) {
        return null
    }
    val timezone = TimeZone.currentSystemDefault()

    val y = Instant.fromEpochMilliseconds(this * 1000L)
        .toLocalDateTime(timezone)
        .toJavaLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern(format)

    return y.format(formatter)
}