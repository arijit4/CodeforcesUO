package com.offbyabit.codeforces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.offbyabit.codeforces.ui.screens.Home
import com.offbyabit.codeforces.ui.screens.Navigation
import com.offbyabit.codeforces.ui.screens.Screen
import com.offbyabit.codeforces.ui.theme.CodeforcesUOTheme
import com.offbyabit.codeforces.ui.viewmodels.HomeVM
import com.offbyabit.codeforces.utils.models.CodeForcesAPI
import com.offbyabit.codeforces.utils.models.userInfo.UserInfo
import com.tencent.mmkv.MMKV

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MMKV.initialize(this)

            CodeforcesUOTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            var selectedIndex by remember { mutableIntStateOf(0) }
                            NavigationBar {
                                navigationItems.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = (index == selectedIndex),
                                        onClick = {
                                            selectedIndex = index
                                            navController.navigate(item.route)
                                        },
                                        label = { Text(item.title) },
                                        icon = {
                                            BadgedBox(
                                                badge = { }
                                            ) {
                                                Icon(
                                                    imageVector = item.run {
                                                        if (index == selectedIndex) {
                                                            selectedIcon
                                                        } else {
                                                            unselectedIcon
                                                        }
                                                    },
                                                    contentDescription = item.title
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        Column(modifier = Modifier.padding(it)) {
                            Navigation(navController)
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

private val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Screen.HomeScreen.route
    ),
    NavigationItem(
        title = "Contests",
        selectedIcon = Icons.Filled.Warning,
        unselectedIcon = Icons.Outlined.Warning,
        route = Screen.ContestScreen.route
    ),
    NavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = Screen.SettingsScreen.route
    )
)