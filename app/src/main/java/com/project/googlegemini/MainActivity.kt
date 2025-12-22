package com.project.googlegemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.googlegemini.ui.ChatScreen
import com.project.googlegemini.ui.ConversationsScreen
import com.project.googlegemini.ui.SearchScreen
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
    val startDestination = if (apiKeyManager.hasApiKey()) "conversations" else "setup"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        // Setup Screen (first time)
        composable("setup") {
            SetupScreen(
                onApiKeySaved = {
                    navController.navigate("conversations") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        // Conversations List Screen (home screen)
        composable("conversations") {
            ConversationsScreen(
                onNavigateToChat = { conversationId ->
                    navController.navigate("chat/$conversationId")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                }
            )
        }

        // Search Screen
        composable("search") {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToChat = { conversationId ->
                    navController.navigate("chat/$conversationId")
                }
            )
        }

        // Chat Screen with conversation ID
        composable(
            route = "chat/{conversationId}",
            arguments = listOf(navArgument("conversationId") { type = NavType.LongType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: 0L
            val viewModel: ChatViewModel = viewModel(
                factory = ChatViewModel.Factory(
                    application = context.applicationContext as android.app.Application,
                    conversationId = conversationId
                )
            )
            val apiKey = apiKeyManager.getApiKey() ?: ""

            // Initialize Gemini with API key
            if (apiKey.isNotEmpty()) {
                viewModel.initializeGemini(apiKey)
            }

            ChatScreen(
                viewModel = viewModel,
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onChangeApiKey = {
                    navController.navigate("setup") {
                        popUpTo("conversations") { inclusive = true }
                    }
                }
            )
        }
    }
}
