package com.offbyabit.codeforces.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.offbyabit.codeforces.ui.viewmodels.SettingsVM
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    viewModel: SettingsVM,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingItem(
                title = "Change handle",
                description = "Maintain your profile by switching to the right handle"
            ) {
                showDialog = true
            }
            SettingItem(
                title = "About",
                description = "You're using the latest version :D"
            ) {

            }
        }
    }

    if (showDialog) {
        ChangeHandleDialog(
            viewModel = viewModel,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ChangeHandleDialog(
    viewModel: SettingsVM,
    onDismiss: () -> Unit
) {
    var handle by remember {
        mutableStateOf(
            MMKV.defaultMMKV().decodeString("handle") ?: "YouKn0wWho"
        )
    }

    AlertDialog(
        onDismissRequest = {
//            onDismiss()
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Button(
                enabled = !viewModel.isWrongId,
                onClick = {
                    viewModel.changeHandle(handle)
                    onDismiss()
                }
            ) {
                Text("Change")
            }
        },
        text = {
            val scope = rememberCoroutineScope()
            OutlinedTextField(
                isError = viewModel.isWrongId,
                label = {
                    Text("CodeForces Handle")
                },
                value = handle,
                onValueChange = {
                    handle = it

                    scope.launch(Dispatchers.IO) {
                        viewModel.checkIfHandleIsValid(handle)
                    }
                }
            )
        }
    )
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
            .padding(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.75f)
        )
    }
}