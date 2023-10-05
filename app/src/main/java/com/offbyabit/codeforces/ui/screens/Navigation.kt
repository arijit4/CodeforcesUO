package com.offbyabit.codeforces.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.offbyabit.codeforces.ui.viewmodels.ContestsVM
import com.offbyabit.codeforces.ui.viewmodels.HomeVM
import com.offbyabit.codeforces.ui.viewmodels.SettingsVM
import com.tencent.mmkv.MMKV

@Composable
fun Navigation(navController: NavHostController) {
    val homeVM = viewModel<HomeVM>()
    val contestsVM = viewModel<ContestsVM>()
    val settingsVM = viewModel<SettingsVM>()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            val handle = (MMKV.defaultMMKV().decodeString("handle") ?: "YouKn0wWho")
            Home(
                handle = handle,
                viewModel = homeVM,
                navController = navController
            )
        }
        composable(
            route = Screen.ContestScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.run {
                        when (navController.previousBackStackEntry?.destination?.route) {
                            Screen.SettingsScreen.route -> Right
                            else -> Left
                        }
                    },
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.run {
                        when (navController.currentBackStackEntry?.destination?.route) {
                            Screen.HomeScreen.route -> Right
                            else -> Left
                        }
                    },
                    animationSpec = tween(700)
                )
            }
        ) {
            Contest(
                viewModel = contestsVM,
                navController = navController
            )
        }
        composable(
            route = Screen.SettingsScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            Settings(
                viewModel = settingsVM,
                navController = navController
            )
        }
    }
}

sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object ContestScreen : Screen("ContestScreen")
    object SettingsScreen : Screen("SettingsScreen")
}