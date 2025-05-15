package com.example.android.githubaccountfinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.network.GitHubApiService
import com.example.android.githubaccountfinder.data.paging.GetUsersSource
import com.example.android.githubaccountfinder.data.paging.SearchUserSource
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: GitHubApiService) {
    private val pageSize = 30

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