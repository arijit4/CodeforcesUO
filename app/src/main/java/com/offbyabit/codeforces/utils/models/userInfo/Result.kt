package com.offbyabit.codeforces.utils.models.userInfo

data class Result(
    val handle: String,
    val email: String? = null,
    val vkId: String? = null,
    val openId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val country: String? = null,
    val city: String? = null,
    val organization: String? = null,
    val contribution: Int,
    val rank: String? = null,
    val rating: Int,
    val maxRank: String,
    val maxRating: Int,
    val lastOnlineTimeSeconds: Int,
    val registrationTimeSeconds: Int,
    val friendOfCount: Int,
    val avatar: String,
    val titlePhoto: String
)