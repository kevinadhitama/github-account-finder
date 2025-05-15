package com.example.android.githubaccountfinder.ui.screen.users.detail

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.android.githubaccountfinder.R
import com.example.android.githubaccountfinder.data.model.GitHubRepository
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.data.model.GitHubUserDetail
import com.example.android.githubaccountfinder.ui.common.SharedAttr
import com.example.android.githubaccountfinder.ui.common.SharedUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(user: GitHubUser, viewModel: UserDetailViewModel, onBackClick: (() -> Unit)) {
    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) {
                Column {
                    TopAppBar(
                        colors = SharedAttr.getAppBarColors(),
                        title = { Text(user.login) },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
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
            LaunchedEffect(user) {
                viewModel.fetchUser(user.id)
            }

            val uiState by viewModel.uiState
            when (uiState) {
                is UserDetailScreenState.Error -> {
                    ErrorUserDetailScreen(
                        (uiState as UserDetailScreenState.Error).message,
                    )
                }

                UserDetailScreenState.Loading -> {}

                is UserDetailScreenState.Success -> {
                    LoadedUserDetailScreen(
                        viewModel = viewModel,
                        user = (uiState as UserDetailScreenState.Success).data
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorUserDetailScreen(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Error Icon",
            tint = Color.Red,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.something_went_wrong_please_try_again_later) + "\n" + message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.label_try_again))
            }
        }
    }
}

@Composable
private fun LoadedUserDetailScreen(viewModel: UserDetailViewModel, user: GitHubUserDetail) {
    UserProfileCard(user)
    HorizontalDivider()
    UserRepoList(viewModel)
}

@Composable
private fun UserProfileCard(user: GitHubUserDetail) {
    Card(
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .fillMaxWidth(),
        colors = SharedAttr.getCardColors(),
        elevation = CardDefaults.elevatedCardElevation()

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.Companion
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Companion.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = user.login,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            user.name?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Row {
                    Text(
                        text = user.following.toString() + " ",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.label_following),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row {
                    Text(
                        text = user.followers.toString() + " ",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.label_followers),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun UserRepoList(viewModel: UserDetailViewModel) {
    val lazyPagingItems = viewModel.repos.collectAsLazyPagingItems()
    Crossfade(targetState = lazyPagingItems.loadState.refresh is LoadState.Loading) { isLoading ->
        if (isLoading) {
            LazyColumn(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(8) { UserDetailItemsUi.ShimmerRepoRow() }
            }
        } else {
            LazyColumn(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    val repo = lazyPagingItems[index]
                    if (repo != null) {
                        RepoRow(repo)
                    }
                }
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item {
                        UserDetailItemsUi.ShimmerRepoRow()
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoRow(repo: GitHubRepository) {
    val context = LocalContext.current
    Row(
        Modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, repo.htmlUrl.toUri())
                context.startActivity(intent)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Row {
                Text(
                    repo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))

                repo.language?.let {
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        it, style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            repo.description?.let {
                Text(
                    it, style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Stars"
                )
                Text(
                    " " + repo.stargazersCount.toString() + " ",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.label_stars),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }

        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Chevron",
            tint = Color.Gray
        )

    }
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
}