package com.project.googlegemini.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.googlegemini.database.ChatDatabase
import com.project.googlegemini.database.ChatRepository
import com.project.googlegemini.database.SearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository

    init {
        val database = ChatDatabase.getDatabase(application)
        repository = ChatRepository(database.conversationDao(), database.messageDao())
    }

    var searchQuery by mutableStateOf("")
        private set

    var searchResults by mutableStateOf<List<SearchResult>>(emptyList())
        private set

    var isSearching by mutableStateOf(false)
        private set

    private var searchJob: Job? = null

    fun updateSearchQuery(query: String) {
        searchQuery = query

        // Cancel previous search job
        searchJob?.cancel()

        if (query.isBlank()) {
            searchResults = emptyList()
            isSearching = false
            return
        }

        // Debounce search
        searchJob = viewModelScope.launch {
            isSearching = true
            delay(300) // Wait 300ms before searching
            performSearch(query)
            isSearching = false
        }
    }

    private suspend fun performSearch(query: String) {
        searchResults = repository.searchConversationsWithSnippets(query)
    }

    fun clearSearch() {
        searchQuery = ""
        searchResults = emptyList()
        isSearching = false
    }
}
