# Phase 2: AI & Smart Features - Implementation Guide

## ✅ Completed (70%)

### 1. Database Schema ✅
- **PromptTemplateEntity.kt** - Lưu trữ prompt templates
- **AIModelSettings.kt** - Cài đặt AI model và streaming
- **DefaultPromptTemplates.kt** - 20 templates mẫu cho 5 categories
- **ChatDao.kt** - Thêm PromptTemplateDao và AIModelSettingsDao
- **ChatDatabase.kt** - Update version 4, thêm 2 entities mới

### 2. UI Components ✅
- **PromptTemplatesScreen.kt** - Màn hình quản lý templates
  - Category filter (Code, Writing, Business, Creative, Learning)
  - Template cards với icon, usage count
  - Add/Edit/Delete/Favorite templates
  - Empty state

- **AIModelSelectionDialog.kt** - Dialog chọn AI model
  - 3 models: Gemini Pro, Flash, Pro Vision
  - Model comparison (tokens, speed, features)
  - Streaming toggle
  - Smart replies toggle

### 3. ViewModels ✅
- **PromptTemplatesViewModel.kt** - Quản lý templates
- **AIModelSettingsViewModel.kt** - Quản lý AI settings

---

## 🔧 Cần Hoàn Thiện (30%)

### 4. Tích hợp vào ChatScreen

#### A. Thêm Templates Button
**File: ChatScreen.kt**

```kotlin
// Trong TopAppBar actions, thêm:
actions = {
    // Templates button
    IconButton(onClick = { onNavigateToTemplates() }) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = "Templates"
        )
    }

    // AI Model button
    IconButton(onClick = { showAIModelDialog = true }) {
        Icon(
            imageVector = Icons.Default.SmartToy,
            contentDescription = "AI Model"
        )
    }

    // Existing menu...
}
```

#### B. Show AI Model Dialog
```kotlin
var showAIModelDialog by remember { mutableStateOf(false) }

if (showAIModelDialog) {
    AIModelSelectionDialog(
        onDismiss = { showAIModelDialog = false },
        onModelSelected = { model ->
            // Reinitialize AI với model mới
            viewModel.switchAIModel(model)
        }
    )
}
```

### 5. Update ChatViewModel - Streaming Support

**File: ChatViewModel.kt**

#### A. Thêm Model Selection
```kotlin
private val aiSettingsDao = database.aiModelSettingsDao()
private var currentModel: AIModel = AIModel.GEMINI_PRO

init {
    viewModelScope.launch {
        aiSettingsDao.getSettings().collect { settings ->
            settings?.let {
                currentModel = AIModel.fromModelName(it.selectedModel)
            }
        }
    }
}

fun switchAIModel(model: AIModel) {
    currentModel = model
    viewModelScope.launch {
        aiSettingsDao.updateSelectedModel(model.modelName)
    }
}
```

#### B. Implement Streaming Responses
```kotlin
suspend fun sendMessageWithStreaming(
    message: String,
    imageUris: List<String> = emptyList()
) {
    val settings = aiSettingsDao.getSettingsOnce() ?: AIModelSettings()

    if (settings.streamingEnabled) {
        // Streaming mode
        isLoading = true
        var streamedText = ""

        try {
            val response = generativeModel?.generateContentStream(
                content {
                    text(message)
                    imageUris.forEach { uriString ->
                        // Add images...
                    }
                }
            )

            response?.collect { chunk ->
                chunk.text?.let { newText ->
                    streamedText += newText
                    // Update UI with partial response
                    updateStreamingMessage(streamedText)
                }
            }

            // Save final message
            saveMessage(streamedText, isFromUser = false)

        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    } else {
        // Normal mode (existing code)
        sendMessage(message, imageUris)
    }
}

private fun updateStreamingMessage(text: String) {
    val currentMessages = _messages.value.toMutableList()
    val lastIndex = currentMessages.indexOfLast { !it.isFromUser }

    if (lastIndex >= 0) {
        currentMessages[lastIndex] = currentMessages[lastIndex].copy(text = text)
    } else {
        currentMessages.add(Message(text = text, isFromUser = false))
    }

    _messages.value = currentMessages
}
```

### 6. Smart Replies Feature

**File: ChatViewModel.kt**

```kotlin
private val _smartReplies = MutableStateFlow<List<String>>(emptyList())
val smartReplies: StateFlow<List<String>> = _smartReplies.asStateFlow()

suspend fun generateSmartReplies() {
    val settings = aiSettingsDao.getSettingsOnce() ?: AIModelSettings()
    if (!settings.smartRepliesEnabled) return

    val lastMessages = _messages.value.takeLast(3)
        .joinToString("\n") { "${if (it.isFromUser) "User" else "AI"}: ${it.text}" }

    try {
        val prompt = """
            Based on this conversation:
            $lastMessages

            Suggest 3 brief follow-up questions the user might ask (max 10 words each).
            Format: one per line, no numbers or bullets.
        """.trimIndent()

        val response = generativeModel?.generateContent(prompt)
        val suggestions = response?.text?.split("\n")
            ?.filter { it.isNotBlank() }
            ?.take(3) ?: emptyList()

        _smartReplies.value = suggestions
    } catch (e: Exception) {
        _smartReplies.value = emptyList()
    }
}
```

**ChatScreen.kt - Show Smart Replies:**

```kotlin
// Sau LazyColumn messages, thêm:
val smartReplies by viewModel.smartReplies.collectAsState()

if (smartReplies.isNotEmpty()) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(smartReplies) { reply ->
            SuggestionChip(
                onClick = {
                    userInput = reply
                    // Or send directly
                },
                label = { Text(reply) }
            )
        }
    }
}
```

### 7. Update MainActivity - Add Routes

**File: MainActivity.kt**

```kotlin
// Thêm route cho Templates
composable("templates") {
    PromptTemplatesScreen(
        onNavigateBack = { navController.popBackStack() },
        onSelectTemplate = { template ->
            // Save selected template ID và back
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("selected_template", template)
            navController.popBackStack()
        }
    )
}

// Trong ChatScreen route, handle template selection:
val selectedTemplate = navController.currentBackStackEntry
    ?.savedStateHandle
    ?.getStateFlow<PromptTemplateEntity?>("selected_template", null)
    ?.collectAsState()

selectedTemplate?.value?.let { template ->
    // Apply template to input
    LaunchedEffect(template) {
        // Set template text to input field
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<PromptTemplateEntity>("selected_template")
    }
}
```

### 8. ChatScreenWithDrawer - Add Navigation

**File: ChatScreenWithDrawer.kt**

```kotlin
// Thêm trong actions của drawer:
ChatScreenWithDrawer(
    viewModel = viewModel,
    onNavigateToSettings = onNavigateToSettings,
    onNavigateToTemplates = {
        navController.navigate("templates")
    },
    onSwitchConversation = onSwitchConversation
)
```

---

## 📋 Testing Checklist

### Templates
- [ ] Xem danh sách 20 templates mặc định
- [ ] Filter theo category
- [ ] Add custom template
- [ ] Edit template
- [ ] Delete template
- [ ] Toggle favorite
- [ ] Usage count tăng khi dùng
- [ ] Apply template vào chat

### AI Models
- [ ] Switch giữa Gemini Pro/Flash/Vision
- [ ] Model info hiển thị đúng
- [ ] Settings lưu persistent

### Streaming
- [ ] Toggle streaming on/off
- [ ] Streaming response hiển thị real-time
- [ ] Error handling khi streaming

### Smart Replies
- [ ] Toggle smart replies on/off
- [ ] Suggestions xuất hiện sau AI response
- [ ] Click suggestion để send
- [ ] Context-aware suggestions

---

## 🚀 Next Steps (Phase 2.1)

1. **Export Conversation** (PDF, TXT, Markdown)
2. **Auto-title with AI** - Tự động đặt tên conversation
3. **Code Syntax Highlighting** - Trong messages
4. **Markdown Rendering** - Format AI responses
5. **Voice Input Integration** - Better UX

---

## 💡 Tips

### Performance
- Streaming giảm perceived latency
- Smart replies cache trong 1 phút
- Templates lazy load

### UX Improvements
- Haptic feedback khi select model
- Animation khi streaming
- Toast message khi switch model
- Progress indicator cho streaming

### Error Handling
- Fallback to normal mode nếu streaming fail
- Retry logic cho smart replies
- Template validation before save

---

## 📝 Quick Integration Steps

1. **Add imports** - Icons.Default.Description
2. **ChatScreenWithDrawer** - Add onNavigateToTemplates param
3. **ChatScreen** - Add templates button in TopAppBar
4. **ChatScreen** - Add AI model dialog
5. **ChatViewModel** - Add streaming methods
6. **ChatViewModel** - Add smart replies
7. **MainActivity** - Add templates route
8. **Test all features!**

---

Build thành công! Database version = 4
Tất cả entities đã được tạo và DAO methods hoàn chỉnh.

**Còn lại:** Tích hợp UI vào ChatScreen và implement streaming logic.
