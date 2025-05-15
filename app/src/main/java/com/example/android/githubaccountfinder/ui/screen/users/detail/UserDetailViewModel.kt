package com.example.android.githubaccountfinder.ui.screen.users.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.githubaccountfinder.data.model.GitHubRepository
import com.example.android.githubaccountfinder.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private val _uiState = mutableStateOf<UserDetailScreenState>(UserDetailScreenState.Loading)
    val uiState: State<UserDetailScreenState> = _uiState
    var repos: Flow<PagingData<GitHubRepository>> = flow {  }
        private set

    fun fetchUser(id: Int) {
        viewModelScope.launch {
            try {
                val user = repository.getUserById(id)
                repos = repository.getReposByUserName(user.login).flow.cachedIn(viewModelScope)

                _uiState.value = UserDetailScreenState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UserDetailScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
