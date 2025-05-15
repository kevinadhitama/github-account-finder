package com.example.android.githubaccountfinder.ui.screen.users.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    private val _query = MutableStateFlow("")
    var users: Flow<PagingData<GitHubUser>> = _query.flatMapLatest { query: String ->
        if (query.isEmpty()) {
            repository.getUsers().flow
        } else {
            repository.getUsersByName(query).flow
        }
    }.cachedIn(viewModelScope)

    fun searchUsers(query: String) {
        _query.value = query
    }

    fun clearSearch() {
        _query.value = ""
    }
}
