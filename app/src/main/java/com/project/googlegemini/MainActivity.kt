package com.project.googlegemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.googlegemini.ui.ChatScreenWithDrawer
import com.project.googlegemini.ui.SetupScreen
import com.project.googlegemini.ui.SettingsScreen
import com.project.googlegemini.ui.theme.GoogleGeminiTheme
import com.project.googlegemini.utils.ApiKeyManager
import com.project.googlegemini.utils.ThemePreferences
import com.project.googlegemini.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themePreferences = ThemePreferences(this)
            val isLightMode by themePreferences.isLightMode.collectAsState(initial = false)

            GoogleGeminiTheme(darkTheme = !isLightMode) {
                GeminiApp()
            }
        }
    }
}

@Composable
fun GeminiApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val apiKeyManager = ApiKeyManager(context)

    // Determine start destination based on whether API key exists
    val startDestination = if (apiKeyManager.hasApiKey()) "chat/0" else "setup"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        // Setup Screen (first time)
        composable("setup") {
            SetupScreen(
                onApiKeySaved = {
                    navController.navigate("chat/0") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        // Chat Screen with conversation ID (main screen)
        composable(
            route = "chat/{conversationId}",
            arguments = listOf(navArgument("conversationId") { type = NavType.LongType })
        ) { backStackEntry ->
            val conversationIdArg = backStackEntry.arguments?.getLong("conversationId") ?: 0L
            val mainViewModel: com.project.googlegemini.viewmodel.MainViewModel = viewModel()
            var actualConversationId by remember { mutableStateOf(conversationIdArg) }

            // Auto-create or get latest conversation if ID is 0
            LaunchedEffect(conversationIdArg) {
                if (conversationIdArg == 0L) {
                    val latestConversation = mainViewModel.getLatestConversation()
                    if (latestConversation != null) {
                        actualConversationId = latestConversation.id
                    } else {
                        // Create new conversation if none exists
                        actualConversationId = mainViewModel.createConversation()
                    }
                } else {
                    actualConversationId = conversationIdArg
                }
            }

            if (actualConversationId > 0L) {
                val viewModel: ChatViewModel = viewModel(
                    key = "chat_$actualConversationId",
                    factory = ChatViewModel.Factory(
                        application = context.applicationContext as android.app.Application,
                        conversationId = actualConversationId
                    )
                )
                val apiKey = apiKeyManager.getApiKey() ?: ""

                // Initialize Gemini with API key
                if (apiKey.isNotEmpty()) {
                    viewModel.initializeGemini(apiKey)
                }

                ChatScreenWithDrawer(
                    viewModel = viewModel,
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    },
                    onSwitchConversation = { newConversationId ->
                        navController.navigate("chat/$newConversationId") {
                            popUpTo("chat/{conversationId}") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onChangeApiKey = {
                    navController.navigate("setup") {
                        popUpTo("chat/{conversationId}") { inclusive = true }
                    }
                }
            )
        }
    }
}
