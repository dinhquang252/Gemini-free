package com.project.googlegemini.database

data class SearchResult(
    val conversation: ConversationEntity,
    val matchingMessageText: String? = null
)
