# 🔐 Google Play Permissions Declaration

## Photo and Video Permissions

### Permission: `android.permission.READ_MEDIA_IMAGES`

---

## ✅ Declaration for Google Play Console

### Read media images

**Question:** Describe your app's use of the READ_MEDIA_IMAGES permission

**Answer (Copy this):**

```
Our app uses READ_MEDIA_IMAGES permission to enable users to select and upload photos
for AI image analysis. Users can choose up to 5 images using the Android photo picker
to ask questions about the image content (e.g., "What do you see in this photo?",
"Describe this image").

The app uses the system photo picker (ActivityResultContracts.PickMultipleVisualMedia)
for one-time image selection. Images are only accessed when explicitly selected by the
user and are processed by Google's Gemini AI API for visual analysis. No images are
stored on our servers - they are only temporarily loaded from the selected URIs and
sent to Gemini API for processing.

Core functionality: AI-powered image analysis and visual question answering.
```

---

## 📋 Alternative Shorter Version (if character limit)

```
This permission enables users to select photos via the Android photo picker for
AI-powered image analysis. Users can upload 1-5 images to ask questions like
"What's in this photo?" or "Describe this image." We use the system photo picker
(PickMultipleVisualMedia) for one-time access. Images are processed by Google
Gemini AI API only when user explicitly selects them. No images are stored.
```

---

## 🎯 Key Points Covered

✅ **Why we need it:** AI image analysis feature
✅ **How we use it:** Android photo picker (system UI)
✅ **User control:** User explicitly selects images
✅ **One-time access:** Not continuous/background access
✅ **Data handling:** Images sent to Gemini API, not stored
✅ **Core functionality:** Essential for visual AI features

---

## 📱 Implementation Details (For Reference)

### In Code (ChatScreen.kt)

```kotlin
// Uses Android Photo Picker API
val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
) { uris ->
    if (uris.isNotEmpty()) {
        selectedImageUris = uris
    }
}

// Triggered only by user action
imagePickerLauncher.launch(
    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
)
```

### In ChatViewModel.kt

```kotlin
// Images loaded only from selected URIs
private fun loadBitmap(uriString: String): Bitmap? {
    return try {
        val uri = Uri.parse(uriString)
        val inputStream = getApplication<Application>()
            .contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        null
    }
}

// Sent to Gemini API for analysis
val inputContent = content {
    bitmaps.forEach { bitmap -> image(bitmap) }
    text(userMessage)
}
```

---

## ✅ Google Play Policy Compliance

### We comply with policies by:

1. **Using Photo Picker:** ✅
   - We use `ActivityResultContracts.PickMultipleVisualMedia`
   - System UI for image selection
   - No direct media storage access

2. **One-time Access:** ✅
   - Images accessed only when user selects them
   - No background access
   - No continuous media scanning

3. **User Control:** ✅
   - User must explicitly tap camera/image button
   - Clear UI indicating image selection
   - User can see selected images before sending

4. **Data Privacy:** ✅
   - Images not stored on our servers
   - Only sent to Google Gemini AI API
   - Temporary in-memory processing only

5. **Core Functionality:** ✅
   - Image analysis is a main feature
   - Clearly described in app listing
   - Essential for AI visual understanding

---

## 🔒 Privacy Policy Update

Make sure your privacy policy mentions:

```
Image Upload and Analysis:
- Users can optionally upload photos for AI analysis
- Images are selected via the device photo picker
- Images are sent to Google Gemini AI API for processing
- We do not store uploaded images on our servers
- Images are processed temporarily and then discarded
```

**✅ Already included in `privacy_policy.html`**

---

## 📝 Additional Questions (May appear)

### Q: Does your app access photos in the background?
**A:** No, images are only accessed when the user explicitly selects them through the photo picker UI.

### Q: Does your app store or share selected photos?
**A:** Images are sent to Google Gemini AI API for analysis only. We do not store images on our servers. See our privacy policy for details.

### Q: Why can't you use the photo picker exclusively?
**A:** We DO use the photo picker exclusively (ActivityResultContracts.PickMultipleVisualMedia). The permission is required by the Android system for reading image data from selected URIs.

### Q: Is this permission required for core functionality?
**A:** Yes, image analysis is a core feature of our AI assistant app, prominently featured in our app listing and screenshots.

---

## 🎯 Submission Checklist

Before submitting permissions declaration:

- [ ] Copy the declaration text above
- [ ] Paste into Google Play Console
- [ ] Verify privacy policy mentions image handling
- [ ] Confirm screenshots show image feature
- [ ] Ensure app description mentions "Image Analysis"
- [ ] Save and submit

---

## 📍 Where to Submit

### Google Play Console Path:
```
App content → Privacy & security → Photo and video permissions
→ Manage → READ_MEDIA_IMAGES → Describe usage
```

---

## ✅ Expected Outcome

Google Play should approve because:
- ✅ We use the recommended photo picker
- ✅ Clear core functionality (AI image analysis)
- ✅ User-initiated, one-time access
- ✅ Transparent data handling
- ✅ Privacy policy compliance

---

## 📞 If Rejected

If Google rejects, emphasize:
1. Photo picker usage (not direct media access)
2. Core functionality (not secondary feature)
3. Screenshots showing the feature
4. Privacy policy transparency

---

**Good luck! This should be approved quickly. 🍀**
