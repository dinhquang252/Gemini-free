package com.project.googlegemini.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.project.googlegemini.data.Message
import com.project.googlegemini.database.ChatDatabase
import com.project.googlegemini.database.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import java.io.InputStream

class ChatViewModel(
    application: Application,
    val conversationId: Long
) : AndroidViewModel(application) {
    private val repository: ChatRepository
    private var generativeModel: GenerativeModel? = null

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _conversationTitle = MutableStateFlow("")
    val conversationTitle: StateFlow<String> = _conversationTitle.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    // Track current generation job for cancellation
    private var generationJob: Job? = null

    // Track number of user questions for ad display
    private var questionCount = 0
    var onShowAdRequest: (() -> Unit)? = null

    init {
        val database = ChatDatabase.getDatabase(application)
        repository = ChatRepository(database.conversationDao(), database.messageDao())

        // Load messages and conversation title from database
        loadMessages()
        loadConversationTitle()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            repository.getMessagesForConversation(conversationId).collect { messageList ->
                _messages.value = messageList
            }
        }
    }

    private fun loadConversationTitle() {
        viewModelScope.launch {
            repository.conversationDao.getConversationById(conversationId)?.let { conversation ->
                _conversationTitle.value = conversation.title
            }
        }
    }

    fun renameConversation(newTitle: String) {
        viewModelScope.launch {
            repository.conversationDao.getConversationById(conversationId)?.let { conversation ->
                repository.conversationDao.updateConversation(
                    conversation.copy(
                        title = newTitle,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                _conversationTitle.value = newTitle
            }
        }
    }

    fun initializeGemini(apiKey: String) {
        if (apiKey.isBlank()) return

        generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey
        )
    }

    private fun loadBitmap(uriString: String): Bitmap? {
        return try {
            val uri = Uri.parse(uriString)
            val inputStream: InputStream? = getApplication<Application>().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sendMessage(userMessage: String, imageUris: List<String> = emptyList()) {
        if (userMessage.isBlank() && imageUris.isEmpty()) return

        val message = Message(
            text = userMessage,
            isFromUser = true,
            imageUris = imageUris
        )

        // Increment question count
        questionCount++

        // Check if we should show an ad (every 5 questions)
        val shouldShowAd = questionCount % 5 == 0

        // Check if Gemini is initialized
        if (generativeModel == null) {
            val errorMessage = Message(
                text = "Vui lòng cấu hình API key trước. Vào Settings để thiết lập.",
                isFromUser = false
            )
            viewModelScope.launch {
                // Update conversation title BEFORE saving (check if this is first message)
                updateConversationTitleIfNeeded(userMessage)

                repository.saveMessage(conversationId, message)
                repository.saveMessage(conversationId, errorMessage)

                // Show ad if needed
                if (shouldShowAd) {
                    onShowAdRequest?.invoke()
                }
            }
            return
        }

        isLoading = true

        generationJob = viewModelScope.launch {
            try {
                // Update conversation title BEFORE saving (check if this is first message)
                updateConversationTitleIfNeeded(userMessage)

                // Save user message
                repository.saveMessage(conversationId, message)

                // Get Gemini response
                val response = if (imageUris.isNotEmpty()) {
                    // Load bitmaps from URIs
                    val bitmaps = imageUris.mapNotNull { loadBitmap(it) }

                    // Create multimodal content
                    val inputContent = content {
                        bitmaps.forEach { bitmap ->
                            image(bitmap)
                        }
                        text(userMessage.ifBlank { "Mô tả những gì bạn thấy trong ảnh" })
                    }

                    generativeModel?.generateContent(inputContent)
                } else {
                    // Text-only message
                    generativeModel?.generateContent(userMessage)
                }

                val geminiResponse = response?.text ?: "Không nhận được phản hồi"

                // Save Gemini response
                val geminiMessage = Message(
                    text = geminiResponse,
                    isFromUser = false
                )
                repository.saveMessage(conversationId, geminiMessage)

                // Show ad after response if needed
                if (shouldShowAd) {
                    onShowAdRequest?.invoke()
                }
            } catch (e: Exception) {
                // Save error message only if not cancelled
                if (e !is kotlinx.coroutines.CancellationException) {
                    val errorMessage = Message(
                        text = "Lỗi: ${e.message ?: "Không thể kết nối với Gemini"}",
                        isFromUser = false
                    )
                    repository.saveMessage(conversationId, errorMessage)

                    // Show ad even on error if needed
                    if (shouldShowAd) {
                        onShowAdRequest?.invoke()
                    }
                }
            } finally {
                isLoading = false
                generationJob = null
            }
        }
    }

    fun stopGeneration() {
        generationJob?.cancel()
        generationJob = null
        isLoading = false
    }

    private suspend fun updateConversationTitleIfNeeded(userMessage: String) {
        // Only update if this is the first user message
        val currentMessages = _messages.value
        val userMessagesCount = currentMessages.count { it.isFromUser }

        if (userMessagesCount == 0) {
            // Try to generate AI-powered title
            val title = generateSmartTitle(userMessage)

            // Update conversation title in database
            repository.conversationDao.getConversationById(conversationId)?.let { conversation ->
                repository.conversationDao.updateConversation(
                    conversation.copy(title = title)
                )
                // Update the StateFlow so UI updates immediately
                _conversationTitle.value = title
            }
        }
    }

    private suspend fun generateSmartTitle(userMessage: String): String {
        return try {
            // Use AI to generate a concise title (2-5 words)
            val titlePrompt = """
                Generate a very short title (2-5 words maximum) for this conversation topic:
                "$userMessage"

                Return ONLY the title, nothing else. No quotes, no explanation.
            """.trimIndent()

            val response = generativeModel?.generateContent(titlePrompt)
            val aiTitle = response?.text?.trim()?.take(50) ?: fallbackTitle(userMessage)

            // Clean up the AI response (remove quotes if any)
            aiTitle.replace("\"", "").replace("'", "").trim().ifBlank {
                fallbackTitle(userMessage)
            }
        } catch (e: Exception) {
            // Fallback to simple truncation if AI fails
            fallbackTitle(userMessage)
        }
    }

    private fun fallbackTitle(userMessage: String): String {
        return if (userMessage.length > 50) {
            userMessage.take(47) + "..."
        } else {
            userMessage
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.deleteConversation(conversationId)
        }
    }

    class Factory(
        private val application: Application,
        private val conversationId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(application, conversationId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
