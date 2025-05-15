package com.example.android.githubaccountfinder.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepository(
    @SerializedName("name")
    var name: String,
    @SerializedName("fork")
    var fork: Boolean,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("html_url")
    var htmlUrl: String,
    @SerializedName("stargazers_count")
    var stargazersCount: Int,
    @SerializedName("language")
    var language: String? = null,
)
