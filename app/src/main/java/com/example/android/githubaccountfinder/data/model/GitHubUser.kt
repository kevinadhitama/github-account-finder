package com.example.android.githubaccountfinder.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUser(
    @SerializedName("login")
    var login: String? = null,
    @SerializedName("id")
    var id: Int,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("html_url")
    var htmlUrl: String? = null,
)
