package com.example.android.githubaccountfinder.data.network

import com.example.android.githubaccountfinder.data.model.GitHubRepository
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.model.GitHubUserDetail
import com.example.android.githubaccountfinder.data.model.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int
    ): List<GitHubUser>

    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
    ): GitHubUserDetail

    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<GitHubRepository>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): SearchUsersResponse
}
