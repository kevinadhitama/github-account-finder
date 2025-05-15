@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.android.githubaccountfinder.ui.screen.users.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.android.githubaccountfinder.R
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.ui.component.SharedAttr
import com.example.android.githubaccountfinder.ui.component.SharedUI
import com.example.android.githubaccountfinder.ui.component.SharedUI.ShimmerUserRow

@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onRowClick: (GitHubUser) -> Unit
) {
    @Composable
    fun UserRow(user: GitHubUser, onRowClick: () -> Unit) {
        Card(
            onClick = onRowClick,
            colors = SharedAttr.getCardColors(),
            elevation = CardDefaults.elevatedCardElevation()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.avatarUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.drawable.ic_baseline_person_24)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.user_profile_size))
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.login ?: "-",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = user.htmlUrl ?: "-",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Chevron",
                    tint = Color.Gray
                )
            }
        }
    }

    val lazyUsersPagingItem = viewModel.users.collectAsLazyPagingItems()

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) { // adds shadow effect under AppBar
                Column {
                    TopAppBar(
                        colors = SharedAttr.getAppBarColors(),
                        title = {
                            AnimatedContent(
                                targetState = isSearching,
                                label = "SearchBarTransition"
                            ) { searching ->
                                if (searching) {
                                    SharedUI.SearchBar(
                                        query = searchText,
                                        onQueryChange = { searchText = it },
                                        onSearchTriggered = {
                                            viewModel.searchUsers(searchText)
                                        }
                                    )
                                } else {
                                    Text("GitHub User List")
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isSearching = !isSearching
                                searchText = ""
                                viewModel.clearSearch()
                            }) {
                                Icon(
                                    imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Toggle Search"
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    ) { innerPadding ->
        SharedUI.Container(topPadding = innerPadding.calculateTopPadding()) {
            Crossfade(targetState = lazyUsersPagingItem.loadState.refresh is LoadState.Loading) { isLoading ->
                if (isLoading) {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(8) { ShimmerUserRow() }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(lazyUsersPagingItem.itemCount) { index ->
                            val user = lazyUsersPagingItem[index]
                            if (user != null) {
                                UserRow(user) {
                                    onRowClick(user)
                                }
                            }
                        }
                        if (lazyUsersPagingItem.loadState.append is LoadState.Loading) {
                            item {
                                ShimmerUserRow()
                            }
                        }
                    }
                }
            }
        }
    }
}