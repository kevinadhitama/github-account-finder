package com.example.android.githubaccountfinder.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.network.GitHubApiService

class GetUsersSource(
    private val apiService: GitHubApiService,
    private val pageSize: Int,
) : PagingSource<Int, GitHubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubUser> {
        val since = params.key ?: 1
        return try {
            val users = apiService.getUsers(perPage = pageSize, since = since)

            LoadResult.Page(
                data = users,
                prevKey = if (since == 1) null else users[0].id,
                nextKey = users[users.size - 1].id
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubUser>): Int? {
        return state.anchorPosition
    }
}