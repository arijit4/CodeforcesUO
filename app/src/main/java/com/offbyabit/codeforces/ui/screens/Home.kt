package com.offbyabit.codeforces.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.transform.CircleCropTransformation
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.Rank
import com.offbyabit.codeforces.utils.models.annotateAsHandle
import com.offbyabit.codeforces.utils.models.annotateAsRank
import com.offbyabit.codeforces.utils.models.annotateRank
import com.offbyabit.codeforces.utils.models.getKeyColor
import com.offbyabit.codeforces.utils.models.getRank
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.offbyabit.codeforces.utils.models.userRatings.UserRatings
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun Home(navController: NavController) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://codeforces.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(CodeForcesAPI::class.java)
    val networkScope = CoroutineScope(Dispatchers.IO)

    val handle = "YouKn0wWho"

    var userInfo by remember { mutableStateOf<UserInfo?>(null) }
    var userRatings by remember { mutableStateOf<UserRatings?>(null) }
    LaunchedEffect(true) {
        // user info
        networkScope.launch {
            try {
                val x = apiService.getUserInfo(handle)
                if (x.status == "OK") {
                    userInfo = x
                }
            } catch (_: Exception) {
            }
        }

        // user ratings
        networkScope.launch {
            try {
                val x = apiService.getUserRatings(handle)
                if (x.status == "OK") {
                    userRatings = x
                }
            } catch (_: Exception) {
            }
        }
    }

    var color = Color.White

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        if (userInfo != null) {
            if (userInfo!!.status == "OK") {
                val result = userInfo!!.result[0]
                val rank = result.rank.getRank()
                color = rank.getKeyColor()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(
                            top = 16.dp,
                            start = 12.dp,
                            end = 12.dp,
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(255.dp)
                            .clip(CircleShape)
                            .border(
                                width = 5.dp,
                                color = rank.getKeyColor(),
                                shape = CircleShape
                            )
//                            .padding(8.dp)
                            .border(
                                width = 5.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            ),
                        model = result.titlePhoto,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ConditionalText(
                            annotatedValue = rank.annotateRank(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = result.handle.annotateAsHandle(rank),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val dimmedTextPercentage = 0.76f
                        ConditionalText(
                            value = result.firstName.orEmpty() +
                                    result.lastName?.let { " $it" }.orEmpty() +
                                    result.city?.let { ", $it" }.orEmpty() +
                                    result.country?.let { ", $it" }.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = dimmedTextPercentage)
                        )
                        ConditionalText(
                            pattern = "From \"$$$\"",
                            value = result.organization,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = dimmedTextPercentage)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ConditionalText(
                            annotatedValue = buildAnnotatedString {
                                append("Rating: ")
                                append(
                                    result.rating.toString()
                                        .annotateAsRank(rank)
                                )
                            }
                        )
                        ConditionalText(
                            annotatedValue = buildAnnotatedString {
                                append("Contribution: ")
                                append(
                                    result.contribution.toString()
                                        .annotateAsHandle(r = Rank.Pupil) // for green color
                                )
                            }
                        )
                        ConditionalText(
                            pattern = "Friend of: $$$ users",
                            value = result.friendOfCount.toString()
                        )
                        ConditionalText(
                            pattern = "E-mail: $$$",
                            value = result.email
                        )
                    }
                }
            }
        }

        if (userRatings != null) {
            if (userRatings!!.status == "OK") {
                val ratings = userRatings!!.result
                val l = ratings.mapIndexed { index, m ->
                    entryOf(index, m.newRating)
                }
                ProvideChartStyle(rememberChartStyle(listOf(color))) {
                    Chart(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                bottom = 16.dp
                            )
                            .animateContentSize(),
                        chart = lineChart(),
                        model = entryModelOf(l),
                        startAxis = rememberStartAxis(
                            valueFormatter = { value, _ ->
                                value.toInt().toString()
                            }
                        ),
                        isZoomEnabled = true,
                        bottomAxis = rememberBottomAxis(),
                    )
                }
            }
        }
    }
}

@Composable
fun ConditionalText(
    pattern: String = "$$$",
    value: String? = "",
    annotatedValue: AnnotatedString? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val c = color.takeOrElse {
        LocalContentColor.current
    }
    if (!value.isNullOrEmpty() && value != "null") {
        Text(
            text = pattern.replace("$$$", value),
            style = style,
            color = c,
            overflow = overflow,
            maxLines = maxLines
        )
    } else if (annotatedValue != null && annotatedValue.text != "null") {
        Text(
            text = annotatedValue,
            style = style,
            color = c,
            overflow = overflow,
            maxLines = maxLines
        )
    }
}

@Composable
internal fun rememberChartStyle(chartColors: List<Color>): ChartStyle =
    rememberChartStyle(
        columnChartColors = chartColors,
        lineChartColors = chartColors
    )

@Composable
internal fun rememberChartStyle(
    columnChartColors: List<Color>,
    lineChartColors: List<Color>
): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color(defaultColors.axisLineColor),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}