package com.offbyabit.codeforces.utils.models.contestList

data class Result(
    val id: Int,
    val name: String,
    val type: String,
    val phase: String,
    val frozen: Boolean,
    val durationSeconds: Int,
    val startTimeSeconds: Int? = null,
    val relativeTimeSeconds: Int? = null,
    val preparedBy: String? = null,
    val websiteUrl: String? = null,
    val description: String? = null,
    val difficulty: Int? = null,
    val kind: String? = null,
    val icpcRegion: String? = null,
    val country: String? = null,
    val city: String? = null,
    val season: String? = null
)