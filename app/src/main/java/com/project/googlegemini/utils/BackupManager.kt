package com.project.googlegemini.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.googlegemini.database.ChatDatabase
import com.project.googlegemini.database.ConversationEntity
import com.project.googlegemini.database.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class BackupData(
    val conversations: List<ConversationEntity>,
    val messages: List<MessageEntity>,
    val backupTime: Long = System.currentTimeMillis(),
    val version: String = "1.0"
)

class BackupManager(private val context: Context) {

    private val gson = Gson()
    private val database = ChatDatabase.getDatabase(context)

    /**
     * Export database to JSON string
     */
    suspend fun exportToJson(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Get all conversations and messages
            val conversations = database.conversationDao().getAllConversations()
            val conversationsList = mutableListOf<ConversationEntity>()

            conversations.collect { list ->
                conversationsList.addAll(list)
                return@collect
            }

            val allMessages = mutableListOf<MessageEntity>()
            conversationsList.forEach { conversation ->
                val messages = database.messageDao().getMessagesForConversation(conversation.id)
                messages.collect { messageList ->
                    allMessages.addAll(messageList)
                    return@collect
                }
            }

            // Create backup data
            val backupData = BackupData(
                conversations = conversationsList,
                messages = allMessages
            )

            // Convert to JSON
            val json = gson.toJson(backupData)
            Result.success(json)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Import database from JSON string
     */
    suspend fun importFromJson(json: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Parse JSON
            val backupData = gson.fromJson(json, BackupData::class.java)

            // Clear existing data
            clearDatabase()

            // Import conversations with original IDs
            backupData.conversations.forEach { conversation ->
                database.conversationDao().insertConversation(conversation)
            }

            // Import messages
            backupData.messages.forEach { message ->
                database.messageDao().insertMessage(message)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Clear all database data
     */
    private suspend fun clearDatabase() = withContext(Dispatchers.IO) {
        // Get all conversations
        val conversations = database.conversationDao().getAllConversations()
        conversations.collect { list ->
            list.forEach { conversation ->
                database.conversationDao().deleteConversationById(conversation.id)
            }
            return@collect
        }
    }

    /**
     * Get backup statistics
     */
    suspend fun getBackupStats(): Result<BackupStats> = withContext(Dispatchers.IO) {
        try {
            val conversations = database.conversationDao().getAllConversations()
            var conversationCount = 0
            var messageCount = 0

            conversations.collect { list ->
                conversationCount = list.size
                list.forEach { conversation ->
                    val messages = database.messageDao().getMessagesForConversation(conversation.id)
                    messages.collect { messageList ->
                        messageCount += messageList.size
                        return@collect
                    }
                }
                return@collect
            }

            Result.success(BackupStats(conversationCount, messageCount))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class BackupStats(
    val conversationCount: Int,
    val messageCount: Int
)
