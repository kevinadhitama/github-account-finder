package com.example.android.githubaccountfinder.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.githubaccountfinder.data.model.GitHubRepository
import com.example.android.githubaccountfinder.data.network.GitHubApiService

class GetReposSource(
    private val apiService: GitHubApiService,
    private val username: String,
    private val pageSize: Int,
) : PagingSource<Int, GitHubRepository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubRepository> {
        val page = params.key ?: 1
        return try {
            val repos = apiService.getRepos(username, pageSize, page)

            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubRepository>): Int? {
        return state.anchorPosition
    }
}