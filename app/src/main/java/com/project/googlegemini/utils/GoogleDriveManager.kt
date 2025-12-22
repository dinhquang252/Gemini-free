package com.project.googlegemini.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class GoogleDriveManager(private val context: Context) {

    private val appFolderName = "GeminiAIBackup"
    private val backupFileName = "gemini_backup.json"

    /**
     * Get Google Sign In options with Drive scope
     */
    fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
    }

    /**
     * Get sign in intent
     */
    fun getSignInIntent(): Intent {
        val client = GoogleSignIn.getClient(context, getSignInOptions())
        return client.signInIntent
    }

    /**
     * Get currently signed in account
     */
    fun getSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    /**
     * Sign out from Google account
     */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        val client = GoogleSignIn.getClient(context, getSignInOptions())
        client.signOut()
    }

    /**
     * Get Drive service instance
     */
    private fun getDriveService(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("Gemini AI")
            .build()
    }

    /**
     * Find or create app folder in Google Drive
     */
    private suspend fun findOrCreateAppFolder(driveService: Drive): String = withContext(Dispatchers.IO) {
        // Search for existing folder
        val result: FileList = driveService.files().list()
            .setQ("name='$appFolderName' and mimeType='application/vnd.google-apps.folder' and trashed=false")
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()

        if (result.files.isNotEmpty()) {
            return@withContext result.files[0].id
        }

        // Create new folder if not exists
        val folderMetadata = File().apply {
            name = appFolderName
            mimeType = "application/vnd.google-apps.folder"
        }

        val folder = driveService.files().create(folderMetadata)
            .setFields("id")
            .execute()

        folder.id
    }

    /**
     * Upload backup file to Google Drive
     */
    suspend fun uploadBackup(jsonData: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val account = getSignedInAccount()
                ?: return@withContext Result.failure(Exception("Not signed in"))

            val driveService = getDriveService(account)
            val folderId = findOrCreateAppFolder(driveService)

            // Check if backup file already exists
            val existingFiles = driveService.files().list()
                .setQ("name='$backupFileName' and '$folderId' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id)")
                .execute()

            val fileMetadata = File().apply {
                name = backupFileName
                parents = listOf(folderId)
            }

            val mediaContent = com.google.api.client.http.ByteArrayContent(
                "application/json",
                jsonData.toByteArray(Charsets.UTF_8)
            )

            val file = if (existingFiles.files.isNotEmpty()) {
                // Update existing file
                driveService.files().update(existingFiles.files[0].id, fileMetadata, mediaContent)
                    .setFields("id, name, modifiedTime")
                    .execute()
            } else {
                // Create new file
                driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, name, modifiedTime")
                    .execute()
            }

            Result.success("Backup uploaded successfully: ${file.modifiedTime}")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Download backup file from Google Drive
     */
    suspend fun downloadBackup(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val account = getSignedInAccount()
                ?: return@withContext Result.failure(Exception("Not signed in"))

            val driveService = getDriveService(account)
            val folderId = findOrCreateAppFolder(driveService)

            // Find backup file
            val result = driveService.files().list()
                .setQ("name='$backupFileName' and '$folderId' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name, modifiedTime)")
                .execute()

            if (result.files.isEmpty()) {
                return@withContext Result.failure(Exception("No backup found"))
            }

            val fileId = result.files[0].id

            // Download file content
            val outputStream = ByteArrayOutputStream()
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream)

            val content = outputStream.toString(Charsets.UTF_8.name())
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get last backup info
     */
    suspend fun getLastBackupInfo(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val account = getSignedInAccount()
                ?: return@withContext Result.failure(Exception("Not signed in"))

            val driveService = getDriveService(account)
            val folderId = findOrCreateAppFolder(driveService)

            val result = driveService.files().list()
                .setQ("name='$backupFileName' and '$folderId' in parents and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name, modifiedTime)")
                .execute()

            if (result.files.isEmpty()) {
                return@withContext Result.failure(Exception("No backup found"))
            }

            val modifiedTime = result.files[0].modifiedTime
            Result.success("Last backup: $modifiedTime")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
