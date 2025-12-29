package com.project.googlegemini.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.project.googlegemini.data.Message
import com.project.googlegemini.database.ConversationEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ConversationExporter {

    enum class ExportFormat {
        TXT, MARKDOWN
    }

    fun exportConversation(
        context: Context,
        conversation: ConversationEntity,
        messages: List<Message>,
        format: ExportFormat
    ): Uri? {
        val content = when (format) {
            ExportFormat.TXT -> generateTextFormat(conversation, messages)
            ExportFormat.MARKDOWN -> generateMarkdownFormat(conversation, messages)
        }

        val fileName = sanitizeFileName(conversation.title)
        val extension = when (format) {
            ExportFormat.TXT -> "txt"
            ExportFormat.MARKDOWN -> "md"
        }

        return saveToFile(context, content, "$fileName.$extension")
    }

    private fun generateTextFormat(conversation: ConversationEntity, messages: List<Message>): String {
        val sb = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Header
        sb.appendLine("=" .repeat(60))
        sb.appendLine("Conversation: ${conversation.title}")
        sb.appendLine("Created: ${dateFormat.format(Date(conversation.createdAt))}")
        sb.appendLine("Updated: ${dateFormat.format(Date(conversation.updatedAt))}")
        sb.appendLine("=" .repeat(60))
        sb.appendLine()

        // Messages
        messages.forEach { message ->
            val timestamp = dateFormat.format(Date(message.timestamp))
            val sender = if (message.isFromUser) "You" else "Gemini AI"

            sb.appendLine("[$timestamp] $sender:")
            sb.appendLine(message.text)

            if (message.imageUris.isNotEmpty()) {
                sb.appendLine("  [Attached ${message.imageUris.size} image(s)]")
            }

            sb.appendLine()
            sb.appendLine("-" .repeat(60))
            sb.appendLine()
        }

        // Footer
        sb.appendLine()
        sb.appendLine("=" .repeat(60))
        sb.appendLine("Exported from Gemini AI Chat")
        sb.appendLine("Total messages: ${messages.size}")
        sb.appendLine("=" .repeat(60))

        return sb.toString()
    }

    private fun generateMarkdownFormat(conversation: ConversationEntity, messages: List<Message>): String {
        val sb = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Header
        sb.appendLine("# ${conversation.title}")
        sb.appendLine()
        sb.appendLine("**Created:** ${dateFormat.format(Date(conversation.createdAt))}")
        sb.appendLine("**Updated:** ${dateFormat.format(Date(conversation.updatedAt))}")
        sb.appendLine("**Category:** ${conversation.category}")
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()

        // Messages
        messages.forEach { message ->
            val timestamp = dateFormat.format(Date(message.timestamp))
            val sender = if (message.isFromUser) "👤 **You**" else "🤖 **Gemini AI**"

            sb.appendLine("## $sender")
            sb.appendLine("*$timestamp*")
            sb.appendLine()
            sb.appendLine(message.text)

            if (message.imageUris.isNotEmpty()) {
                sb.appendLine()
                sb.appendLine("📎 *Attached ${message.imageUris.size} image(s)*")
            }

            sb.appendLine()
            sb.appendLine("---")
            sb.appendLine()
        }

        // Footer
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        sb.appendLine("*Exported from Gemini AI Chat*")
        sb.appendLine("*Total messages: ${messages.size}*")

        return sb.toString()
    }

    private fun sanitizeFileName(title: String): String {
        // Remove invalid file name characters
        val sanitized = title.replace(Regex("[^a-zA-Z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "_")
            .take(50) // Limit length

        return if (sanitized.isBlank()) {
            "conversation_${System.currentTimeMillis()}"
        } else {
            sanitized
        }
    }

    private fun saveToFile(context: Context, content: String, fileName: String): Uri? {
        return try {
            val cacheDir = File(context.cacheDir, "exports")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val file = File(cacheDir, fileName)
            file.writeText(content)

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shareExportedFile(context: Context, uri: Uri, format: ExportFormat) {
        val mimeType = when (format) {
            ExportFormat.TXT -> "text/plain"
            ExportFormat.MARKDOWN -> "text/markdown"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Share conversation"))
    }
}
