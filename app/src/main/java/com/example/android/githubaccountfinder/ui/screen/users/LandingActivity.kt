package com.example.android.githubaccountfinder.ui.screen.users

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.android.githubaccountfinder.data.model.GitHubUser
import com.example.android.githubaccountfinder.ui.screen.users.detail.UserDetailScreen
import com.example.android.githubaccountfinder.ui.screen.users.list.UserListScreen
import com.example.android.githubaccountfinder.ui.screen.users.list.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "landingScreen", route = "main") {

                composable("landingScreen") {
                    val parentEntry = remember(navController.currentBackStackEntry) {
                        navController.getBackStackEntry("main")
                    }
                    val viewModel: UserListViewModel = hiltViewModel(parentEntry)

                    UserListScreen(viewModel) { user ->
                        navController.navigate(user)
                    }
                }
                composable<GitHubUser> { backStackEntry ->
                    val user: GitHubUser = backStackEntry.toRoute()
                    UserDetailScreen(user) {
                        navController.popBackStack("landingScreen", false)
                    }
                }
            }
        }
    }
}
