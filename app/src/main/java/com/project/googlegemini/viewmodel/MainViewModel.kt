package com.project.googlegemini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.googlegemini.database.ChatDatabase
import com.project.googlegemini.database.ConversationEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ChatDatabase.getDatabase(application)
    private val conversationDao = database.conversationDao()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Get all categories
    val categories: StateFlow<List<String>> = conversationDao.getAllCategories()
        .map { dbCategories ->
            listOf("All") + dbCategories.filter { it != "All" }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("All")
        )

    // Get conversations based on selected category
    val conversations: StateFlow<List<ConversationEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == "All") {
                conversationDao.getAllConversations()
            } else {
                conversationDao.getConversationsByCategory(category)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    suspend fun getLatestConversation(): ConversationEntity? {
        return conversationDao.getLatestConversation()
    }

    suspend fun createConversation(category: String = "All"): Long {
        val conversation = ConversationEntity(
            title = "New Chat",
            category = category
        )
        return conversationDao.insertConversation(conversation)
    }
}
