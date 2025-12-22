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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConversationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository

    init {
        val database = ChatDatabase.getDatabase(application)
        repository = ChatRepository(database.conversationDao(), database.messageDao())
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
}
