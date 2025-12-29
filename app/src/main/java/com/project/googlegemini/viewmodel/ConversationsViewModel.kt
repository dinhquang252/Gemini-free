package com.project.googlegemini.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.googlegemini.database.ChatDatabase
import com.project.googlegemini.database.ChatRepository
import com.project.googlegemini.database.ConversationEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConversationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository
    private val conversationDao: com.project.googlegemini.database.ConversationDao

    init {
        val database = ChatDatabase.getDatabase(application)
        repository = ChatRepository(database.conversationDao(), database.messageDao())
        conversationDao = database.conversationDao()
    }

    var searchQuery by mutableStateOf("")
        private set

    private val _searchQueryFlow = kotlinx.coroutines.flow.MutableStateFlow("")

    val conversations = _searchQueryFlow.flatMapLatest { query ->
        repository.searchConversations(query)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun getConversationsByCategory(category: String): Flow<List<ConversationEntity>> {
        return if (category == "All") {
            conversationDao.getAllConversations()
        } else {
            conversationDao.getConversationsByCategory(category)
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        _searchQueryFlow.value = query
    }

    fun createNewConversation(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.createConversation("New Chat")
            onCreated(id)
        }
    }

    fun renameConversation(id: Long, newTitle: String) {
        viewModelScope.launch {
            repository.conversationDao.getConversationById(id)?.let { conversation ->
                repository.conversationDao.updateConversation(
                    conversation.copy(
                        title = newTitle,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun deleteConversation(id: Long) {
        viewModelScope.launch {
            repository.deleteConversation(id)
        }
    }

    suspend fun getConversationById(id: Long): ConversationEntity? {
        return repository.conversationDao.getConversationById(id)
    }
}
