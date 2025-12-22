package com.project.googlegemini.data

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUris: List<String> = emptyList() // URIs of images
)
