package com.project.googlegemini.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.googlegemini.ui.theme.GeminiBlue
import com.project.googlegemini.utils.ApiKeyManager
import com.project.googlegemini.utils.BackupManager
import com.project.googlegemini.utils.BackupStats
import com.project.googlegemini.utils.GoogleDriveManager
import com.project.googlegemini.utils.SetStatusBarColor
import com.project.googlegemini.utils.ThemePreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onChangeApiKey: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val apiKeyManager = remember { ApiKeyManager(context) }
    val apiKey = apiKeyManager.getApiKey() ?: ""

    val themePreferences = remember { ThemePreferences(context) }
    val isLightMode by themePreferences.isLightMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Set status bar color to match top bar
    SetStatusBarColor(
        color = MaterialTheme.colorScheme.primary,
        darkIcons = false
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // API Key Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "API Key",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = maskApiKey(apiKey),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                apiKeyManager.clearApiKey()
                                onChangeApiKey()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                            Text("Đổi", modifier = Modifier.padding(start = 4.dp))
                        }

                        OutlinedButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                            Text("Xóa", modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isLightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "Chế độ sáng",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isLightMode) "Đang bật" else "Đang tắt",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = isLightMode,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                themePreferences.setLightMode(isChecked)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cloud Backup Section
            BackupSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Info Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Về API Key",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• API key của bạn được lưu an toàn trên thiết bị này\n" +
                                "• Không ai khác có thể truy cập API key của bạn\n" +
                                "• Free tier: 15 requests/phút, 1,500 requests/ngày\n" +
                                "• Hoàn toàn miễn phí cho sử dụng cá nhân",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/apikey"))
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Quản lý API Key")
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gemini Chat v1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Powered by Gemini 2.5 Flash",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa API Key?") },
            text = { Text("Bạn có chắc muốn xóa API key? Bạn sẽ cần nhập lại để sử dụng ứng dụng.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        apiKeyManager.clearApiKey()
                        showDeleteDialog = false
                        onChangeApiKey()
                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
private fun BackupSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val driveManager = remember { GoogleDriveManager(context) }
    val backupManager = remember { BackupManager(context) }

    var isSignedIn by remember { mutableStateOf(driveManager.getSignedInAccount() != null) }
    var lastBackupInfo by remember { mutableStateOf("No backup yet") }
    var backupStats by remember { mutableStateOf<BackupStats?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var showRestoreConfirm by remember { mutableStateOf(false) }

    // Google Sign In launcher
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            isSignedIn = driveManager.getSignedInAccount() != null
            scope.launch {
                // Get last backup info after sign in
                driveManager.getLastBackupInfo().onSuccess {
                    lastBackupInfo = it
                }.onFailure {
                    lastBackupInfo = "No backup found"
                }
            }
        }
    }

    // Load backup stats
    LaunchedEffect(Unit) {
        backupManager.getBackupStats().onSuccess {
            backupStats = it
        }

        if (isSignedIn) {
            driveManager.getLastBackupInfo().onSuccess {
                lastBackupInfo = it
            }.onFailure {
                lastBackupInfo = "No backup found"
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Cloud Backup",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Backup Stats
            if (backupStats != null) {
                Text(
                    text = "${backupStats!!.conversationCount} conversations, ${backupStats!!.messageCount} messages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Last backup info
            if (isSignedIn) {
                Text(
                    text = lastBackupInfo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Status message
            if (statusMessage != null) {
                Text(
                    text = statusMessage!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (statusMessage!!.contains("Error") || statusMessage!!.contains("Failed"))
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Loading indicator
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            // Buttons
            if (!isSignedIn) {
                OutlinedButton(
                    onClick = {
                        signInLauncher.launch(driveManager.getSignInIntent())
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sign in with Google")
                }
            } else {
                // Backup & Restore buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isLoading = true
                            statusMessage = null
                            scope.launch {
                                // Export database to JSON
                                backupManager.exportToJson().onSuccess { json ->
                                    // Upload to Drive
                                    driveManager.uploadBackup(json).onSuccess { message ->
                                        statusMessage = "Backup successful!"
                                        lastBackupInfo = message
                                    }.onFailure { error ->
                                        statusMessage = "Error: ${error.message}"
                                    }
                                }.onFailure { error ->
                                    statusMessage = "Export failed: ${error.message}"
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Backup")
                    }

                    OutlinedButton(
                        onClick = { showRestoreConfirm = true },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Restore")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sign out button
                TextButton(
                    onClick = {
                        scope.launch {
                            driveManager.signOut()
                            isSignedIn = false
                            lastBackupInfo = "No backup yet"
                        }
                    }
                ) {
                    Text("Sign out", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    // Restore confirmation dialog
    if (showRestoreConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreConfirm = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Restore from Backup?") },
            text = {
                Text("This will replace all current conversations and messages with data from your backup. This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRestoreConfirm = false
                        isLoading = true
                        statusMessage = null
                        scope.launch {
                            // Download backup from Drive
                            driveManager.downloadBackup().onSuccess { json ->
                                // Import to database
                                backupManager.importFromJson(json).onSuccess {
                                    statusMessage = "Restore successful!"
                                    // Reload stats
                                    backupManager.getBackupStats().onSuccess {
                                        backupStats = it
                                    }
                                }.onFailure { error ->
                                    statusMessage = "Import failed: ${error.message}"
                                }
                            }.onFailure { error ->
                                statusMessage = "Download failed: ${error.message}"
                            }
                            isLoading = false
                        }
                    }
                ) {
                    Text("Restore", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun maskApiKey(apiKey: String): String {
    return if (apiKey.length > 12) {
        "${apiKey.take(8)}${"*".repeat(20)}${apiKey.takeLast(4)}"
    } else {
        "*".repeat(apiKey.length)
    }
}
