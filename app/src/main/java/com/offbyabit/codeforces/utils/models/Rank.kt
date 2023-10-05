package com.offbyabit.codeforces.utils.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

// https://codeforces.com/blog/entry/68288
// private as the colors don't make sense themselves ;)
private object Colors {
    val Newbie = Color(0xFF808080)
    val Pupil = Color(0xFF88CC22)
    val Apprentice = Color(0xFF008000)
    val Specialist = Color(0xFF03A89E)
    val Expert = Color(0xFF0000FF)
    val CandidateMaster = Color(0xFFAA00AA)
    val Master = Color(0xFFFF8C00)
    val InternationalMaster = Color(0xFFFF0000)
    val Grandmaster = Color(0xFFFF0000)
    val InternationalGrandmaster = Color(0xFFFF00FF)
    val LegendaryGrandmaster = Color(0xFFFF0000)
}

enum class Rank {
    Official,
    Newbie,
    Pupil,
    Apprentice,
    Specialist,
    Expert,
    CandidateMaster,
    Master,
    InternationalMaster,
    Grandmaster,
    InternationalGrandmaster,
    LegendaryGrandmaster
}

object RankStrings {
    const val Official = "Official"
    const val Newbie = "newbie"
    const val Pupil = "pupil"
    const val Apprentice = "apprentice"
    const val Specialist = "specialist"
    const val Expert = "expert"
    const val CandidateMaster = "candidate master"
    const val Master = "master"
    const val Grandmaster = "grandmaster"
    const val InternationalMaster = "international master"
    const val InternationalGrandmaster = "international grandmaster"
    const val LegendaryGrandmaster = "legendary grandmaster"
}

@Composable
fun Rank.getColor(): Color {
    return when (this) {
        Rank.Official -> MaterialTheme.colorScheme.onSurface
        Rank.Newbie -> Colors.Newbie
        Rank.Pupil -> Colors.Pupil
        Rank.Apprentice -> Colors.Apprentice
        Rank.Specialist -> Colors.Specialist
        Rank.Expert -> Colors.Expert
        Rank.CandidateMaster -> Colors.CandidateMaster
        Rank.Master -> Colors.Master
        Rank.Grandmaster -> Colors.Grandmaster
        Rank.InternationalMaster -> Colors.InternationalMaster
        Rank.InternationalGrandmaster -> Colors.InternationalGrandmaster
        Rank.LegendaryGrandmaster -> Colors.LegendaryGrandmaster
    }
}

fun Rank.getKeyColor(
    officialColor: Color? = null
): Color {
    return when (this) {
        Rank.Official -> officialColor ?: Color.White
        Rank.Newbie -> Colors.Newbie
        Rank.Pupil -> Colors.Pupil
        Rank.Apprentice -> Colors.Apprentice
        Rank.Specialist -> Colors.Specialist
        Rank.Expert -> Colors.Expert
        Rank.CandidateMaster -> Colors.Master
        Rank.Master -> Colors.Master
        Rank.Grandmaster -> Colors.Grandmaster
        Rank.InternationalMaster -> Colors.Grandmaster
        Rank.InternationalGrandmaster -> Colors.Grandmaster
        Rank.LegendaryGrandmaster -> Colors.Grandmaster
    }
}

fun Rank.getName(): String {
    return when (this) {
        Rank.Official -> RankStrings.Official.toTitleCase()
        Rank.Newbie -> RankStrings.Newbie.toTitleCase()
        Rank.Pupil -> RankStrings.Pupil.toTitleCase()
        Rank.Apprentice -> RankStrings.Apprentice.toTitleCase()
        Rank.Specialist -> RankStrings.Specialist.toTitleCase()
        Rank.Expert -> RankStrings.Expert.toTitleCase()
        Rank.CandidateMaster -> RankStrings.CandidateMaster.toTitleCase()
        Rank.Master -> RankStrings.Master.toTitleCase()
        Rank.Grandmaster -> RankStrings.Grandmaster.toTitleCase()
        Rank.InternationalMaster -> RankStrings.InternationalMaster.toTitleCase()
        Rank.InternationalGrandmaster -> RankStrings.InternationalGrandmaster.toTitleCase()
        Rank.LegendaryGrandmaster -> RankStrings.LegendaryGrandmaster.toTitleCase()
    }
}

fun String?.getRank(): Rank {
    return when (this) {
        RankStrings.Newbie -> Rank.Newbie
        RankStrings.Pupil -> Rank.Pupil
        RankStrings.Apprentice -> Rank.Apprentice
        RankStrings.Specialist -> Rank.Specialist
        RankStrings.Expert -> Rank.Expert
        RankStrings.CandidateMaster -> Rank.CandidateMaster
        RankStrings.Master -> Rank.Master
        RankStrings.Grandmaster -> Rank.Grandmaster
        RankStrings.InternationalMaster -> Rank.InternationalMaster
        RankStrings.InternationalGrandmaster -> Rank.InternationalGrandmaster
        RankStrings.LegendaryGrandmaster -> Rank.LegendaryGrandmaster
        else -> Rank.Official
    }
}

@Composable
fun String.annotateAsHandle(r: Rank): AnnotatedString {
    val w = when (r) {
        Rank.LegendaryGrandmaster -> {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append(this@annotateAsHandle[0])
                }
                withStyle(
                    style = SpanStyle(
                        color = Colors.LegendaryGrandmaster
                    )
                ) {
                    append(this@annotateAsHandle.substring(1))
                }
            }
        }

        Rank.InternationalGrandmaster -> {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Colors.InternationalGrandmaster
                    )
                ) {
                    append(this@annotateAsHandle[0])
                }
                withStyle(
                    style = SpanStyle(
                        color = Colors.Grandmaster
                    )
                ) {
                    append(this@annotateAsHandle.substring(0))
                }
            }
        }

        Rank.InternationalMaster -> {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Colors.InternationalMaster
                    )
                ) {
                    append(this@annotateAsHandle[0])
                }
                withStyle(
                    style = SpanStyle(
                        color = Colors.Master
                    )
                ) {
                    append(this@annotateAsHandle.substring(1))
                }
            }
        }

        else -> {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = r.getColor()
                    )
                ) {
                    append(this@annotateAsHandle)
                }
            }
        }
    }
    return w
}

@Composable
fun Rank.annotateRank(): AnnotatedString {
    val name = this.getName().toTitleCase()
    val x = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = this@annotateRank.getKeyColor()
            )
        ) {
            append(name)
        }
    }
    return x
}

@Composable
fun String.annotateAsRank(rank: Rank): AnnotatedString {
//    val name = rank.getName().toTitleCase()
    val x = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = rank.getKeyColor()
            )
        ) {
            append(this@annotateAsRank)
        }
    }
    return x
}

fun String.toTitleCase(): String {
    val words = split(" ")
    val capitalizedWords = words.map { s -> s.replaceFirstChar { it.uppercase() } }
    return capitalizedWords.joinToString(" ")
}