package com.offbyabit.codeforces.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavHostController) {
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
            Home(navController)
        }
        composable(
            route = Screen.ContestScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.run {
                        when(navController.previousBackStackEntry?.destination?.route) {
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
            Contest(navController)
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
            Settings(navController)
        }
    }
}

sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object ContestScreen : Screen("ContestScreen")
    object SettingsScreen : Screen("SettingsScreen")
}