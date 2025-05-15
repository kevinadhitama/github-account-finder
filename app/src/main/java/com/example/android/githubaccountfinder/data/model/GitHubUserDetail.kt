package com.example.android.githubaccountfinder.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUserDetail(
    @SerializedName("login")
    var login: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("avatar_url")
    var avatarUrl: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("html_url")
    var htmlUrl: String,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("company")
    var company: String? = null,
    @SerializedName("blog")
    var blog: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("bio")
    var bio: String? = null,
    @SerializedName("twitter_username")
    var twitterUsername: String? = null,
    @SerializedName("public_repos")
    var publicRepos: Int,
    @SerializedName("public_gists")
    var publicGists: Int,
    @SerializedName("followers")
    var followers: Int,
    @SerializedName("following")
    var following: Int,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("updated_at")
    var updatedAt: String,
)