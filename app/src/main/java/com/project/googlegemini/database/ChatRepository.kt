package com.project.googlegemini.database

import com.project.googlegemini.data.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ChatRepository(
    val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    val allConversations: Flow<List<ConversationEntity>> = conversationDao.getAllConversations()

    fun searchConversations(query: String): Flow<List<ConversationEntity>> {
        return if (query.isBlank()) {
            conversationDao.getAllConversations()
        } else {
            conversationDao.searchConversations(query)
        }
    }

    suspend fun searchConversationsWithSnippets(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        // Use first() to get first emission from Flow
        val conversations = conversationDao.searchConversations(query).first()

        return conversations.map { conversation ->
            val snippet = messageDao.getFirstMatchingMessage(conversation.id, query)
            SearchResult(conversation, snippet)
        }
    }

    fun getMessagesForConversation(conversationId: Long): Flow<List<Message>> {
        return messageDao.getMessagesForConversation(conversationId).map { entities ->
            entities.map { Message(it.text, it.isFromUser, it.timestamp, it.imageUris) }
        }
    }

    suspend fun createConversation(title: String): Long {
        val conversation = ConversationEntity(title = title)
        return conversationDao.insertConversation(conversation)
    }

    suspend fun updateConversationTimestamp(id: Long) {
        conversationDao.getConversationById(id)?.let {
            conversationDao.updateConversation(it.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun deleteConversation(id: Long) {
        conversationDao.deleteConversationById(id)
    }

    suspend fun saveMessage(conversationId: Long, message: Message) {
        val entity = MessageEntity(
            conversationId = conversationId,
            text = message.text,
            isFromUser = message.isFromUser,
            timestamp = message.timestamp,
            imageUris = message.imageUris
        )
        messageDao.insertMessage(entity)
        updateConversationTimestamp(conversationId)
    }
}
