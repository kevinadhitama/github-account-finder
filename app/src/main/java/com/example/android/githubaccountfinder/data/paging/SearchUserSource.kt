package com.example.android.githubaccountfinder.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.network.GitHubApiService

class SearchUserSource(
    private val apiService: GitHubApiService,
    private val query: String,
    private val pageSize: Int,
) : PagingSource<Int, GitHubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubUser> {
        val page = params.key ?: 1
        return try {
            val searchUserRes =
                apiService.searchUsers(query = query, perPage = pageSize, page = page)
            val users = searchUserRes.items

            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (users.isEmpty() || searchUserRes.totalCount <= page * pageSize) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubUser>): Int? {
        return state.anchorPosition
    }
}