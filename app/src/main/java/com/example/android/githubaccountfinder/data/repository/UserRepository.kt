package com.example.android.githubaccountfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.android.githubaccountfinder.data.model.GitHubRepository
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.model.GitHubUserDetail
import com.example.android.githubaccountfinder.data.network.GitHubApiService
import com.example.android.githubaccountfinder.data.paging.GetReposSource
import com.example.android.githubaccountfinder.data.paging.GetUsersSource
import com.example.android.githubaccountfinder.data.paging.SearchUserSource
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: GitHubApiService) {
    private val pageSize = 30

    suspend fun getUserById(id: Int): GitHubUserDetail {
        return apiService.getUser(id)
    }

    fun getReposByUserName(username: String): Pager<Int, GitHubRepository> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetReposSource(
                    apiService = apiService,
                    username = username,
                    pageSize = pageSize
                )
            }
        )
    }

    fun getUsers(): Pager<Int, GitHubUser> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetUsersSource(apiService = apiService, pageSize = pageSize) }
        )
    }

    fun getUsersByName(query: String): Pager<Int, GitHubUser> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchUserSource(
                    apiService = apiService,
                    query = query,
                    pageSize = pageSize
                )
            }
        )
    }
}