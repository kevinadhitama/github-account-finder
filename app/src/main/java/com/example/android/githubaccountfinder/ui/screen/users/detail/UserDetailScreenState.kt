package com.example.android.githubaccountfinder.ui.screen.users.detail

import com.example.android.githubaccountfinder.data.model.GitHubUserDetail

sealed class UserDetailScreenState {
    object Loading : UserDetailScreenState()
    data class Success(val data: GitHubUserDetail) : UserDetailScreenState()
    data class Error(val message: String) : UserDetailScreenState()
}