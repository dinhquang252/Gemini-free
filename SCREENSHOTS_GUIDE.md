# 📸 Google Play Store Screenshots Guide

## Yêu cầu của Google Play Store

### 📱 Phone Screenshots (BẮT BUỘC)
- **Số lượng:** 2-8 ảnh
- **Định dạng:** JPEG hoặc PNG (24-bit)
- **Kích thước:** Tối thiểu 320px, tối đa 3840px
- **Tỷ lệ:** 16:9 hoặc 9:16 (khuyến nghị)
- **Recommended:** 1080 x 2340px (portrait) hoặc 2340 x 1080px (landscape)

### 📱 7-inch Tablet Screenshots (TÙY CHỌN)
- **Số lượng:** 1-8 ảnh (nếu có)
- **Kích thước:** Tối thiểu 320px, tối đa 3840px
- **Recommended:** 1200 x 1920px

### 📱 10-inch Tablet Screenshots (TÙY CHỌN)
- **Số lượng:** 1-8 ảnh (nếu có)
- **Kích thước:** Tối thiểu 1080px
- **Recommended:** 1920 x 2560px

---

## 🚀 Cách chụp Screenshots

### Phương án 1: Chụp từ thiết bị thật (KHUYẾN NGHỊ)

#### Bước 1: Cài đặt app lên điện thoại
```bash
cd /Users/quangtran/AndroidStudioProjects/GoogleGemini
./gradlew installRelease
```

#### Bước 2: Mở app và chụp màn hình
1. Mở app Gemini Free
2. Thao tác qua các màn hình quan trọng
3. Chụp màn hình (Power + Volume Down)

#### Bước 3: Lấy screenshots từ điện thoại
```bash
# Kéo screenshots về máy tính
adb pull /sdcard/Pictures/Screenshots/ ./screenshots/phone/
```

---

### Phương án 2: Chụp từ Android Emulator

#### Bước 1: Tạo Phone Emulator (Pixel 6)
```bash
# Mở Android Studio → Device Manager → Create Device
# Chọn: Pixel 6 (1080 x 2400)
# API Level: 34 (Android 14)
```

#### Bước 2: Chạy app trên emulator
```bash
./gradlew installRelease
adb shell am start -n com.project.googlegemini/.MainActivity
```

#### Bước 3: Chụp screenshots
- Nhấn nút Camera trong Device Manager
- Hoặc sử dụng: Android Studio → Device Manager → Camera icon
- Lưu vào: `screenshots/phone/`

---

#### Bước 4: Tạo 7-inch Tablet Emulator (Nexus 7)
```bash
# Device Manager → Create Device
# Chọn: Nexus 7 (1200 x 1920)
# API Level: 34
```

#### Bước 5: Tạo 10-inch Tablet Emulator (Pixel Tablet)
```bash
# Device Manager → Create Device
# Chọn: Pixel Tablet (2560 x 1600)
# API Level: 34
```

---

## 📸 Danh sách Screenshots cần chụp (khuyến nghị 4-8 ảnh)

### Screenshot 1: Màn hình chào mừng / Conversations List
**Mô tả:** Hiển thị danh sách các cuộc hội thoại với giao diện gradient đẹp
**Màn hình:** ConversationsScreen
**Nội dung:**
- Header với title "Gemini AI"
- Danh sách 3-4 conversations với tên khác nhau
- Floating action button "New Chat"

### Screenshot 2: Màn hình Chat với tin nhắn text
**Mô tả:** Cuộc hội thoại AI với câu hỏi và trả lời
**Màn hình:** ChatScreen
**Nội dung mẫu:**
- User: "What is artificial intelligence?"
- Gemini: "Artificial intelligence (AI) is the simulation of human intelligence..."
- User: "Tell me more about machine learning"
- Gemini: "Machine learning is a subset of AI that..."

### Screenshot 3: Tính năng Image Analysis
**Mô tả:** Upload và phân tích ảnh
**Màn hình:** ChatScreen
**Nội dung:**
- Tin nhắn có kèm 2-3 ảnh
- User: "What do you see in these images?"
- Gemini response mô tả ảnh

### Screenshot 4: Dark Mode
**Mô tả:** Giao diện dark mode đẹp
**Màn hình:** ChatScreen (dark mode)
**Cách bật:**
1. Vào Settings
2. Bật Dark Mode toggle
3. Quay lại ChatScreen và chụp

### Screenshot 5: Settings Screen
**Mô tả:** Màn hình cài đặt
**Màn hình:** SettingsScreen
**Hiển thị:**
- Theme toggle
- API Settings
- Conversation Settings
- About section

### Screenshot 6: Light Mode với gradient
**Mô tả:** Giao diện light mode với gradient đẹp
**Màn hình:** ChatScreen (light mode)
**Nội dung:**
- Conversation với gradient message bubbles
- Typing indicator

### Screenshot 7: Multiple Conversations
**Mô tả:** Quản lý nhiều cuộc hội thoại
**Màn hình:** ConversationsScreen
**Hiển thị:**
- 5-6 conversations với tên khác nhau
- Swipe actions (delete)

### Screenshot 8: Long Conversation
**Mô tả:** Cuộc hội thoại dài với nhiều tin nhắn
**Màn hình:** ChatScreen
**Nội dung:**
- 6-8 tin nhắn qua lại
- Scroll để thấy history

---

## 🎨 Tips để screenshots đẹp hơn

### 1. Nội dung mẫu chất lượng
Sử dụng các câu hỏi thực tế:
```
User: "Write a short poem about AI"
Gemini: "In circuits deep and data streams so vast..."

User: "Explain quantum computing simply"
Gemini: "Quantum computing uses quantum mechanics..."

User: "Help me plan a trip to Japan"
Gemini: "Here's a suggested 7-day Japan itinerary..."
```

### 2. Tạo conversations với tên hay
- "Creative Writing"
- "Coding Help"
- "Travel Planning"
- "Learning Python"
- "Recipe Ideas"

### 3. Clean UI
- Không để keyboard hiển thị (trừ khi cần)
- Scroll để content cân đối
- Không chụp lúc đang loading (trừ khi cần show typing indicator)

### 4. Highlight Features
- Screenshot 1-2: Chat functionality
- Screenshot 3: Image analysis
- Screenshot 4: Dark mode
- Screenshot 5: Settings/Customization
- Screenshot 6: Conversations management

---

## 📁 Cấu trúc thư mục screenshots

Tạo thư mục:
```bash
mkdir -p screenshots/phone
mkdir -p screenshots/tablet-7
mkdir -p screenshots/tablet-10
```

Đặt tên file:
```
screenshots/
├── phone/
│   ├── 01_conversations_list.png
│   ├── 02_chat_conversation.png
│   ├── 03_image_analysis.png
│   ├── 04_dark_mode.png
│   ├── 05_settings.png
│   ├── 06_light_mode.png
│   ├── 07_multiple_chats.png
│   └── 08_long_conversation.png
├── tablet-7/
│   ├── 01_conversations_list.png
│   ├── 02_chat_conversation.png
│   └── ... (tương tự)
└── tablet-10/
    ├── 01_conversations_list.png
    ├── 02_chat_conversation.png
    └── ... (tương tự)
```

---

## 🔧 Script tự động chụp screenshots

### Tạo file script
```bash
#!/bin/bash
# auto_screenshot.sh

echo "Starting automated screenshot capture..."

# Wait for device
adb wait-for-device

# Launch app
adb shell am start -n com.project.googlegemini/.MainActivity
sleep 2

# Screenshot 1: Conversations
adb shell screencap -p /sdcard/screen_01.png
sleep 1

# Navigate to new chat
adb shell input tap 800 2000  # Tap FAB
sleep 1

# Screenshot 2: Chat screen
adb shell screencap -p /sdcard/screen_02.png

# Pull screenshots
adb pull /sdcard/screen_01.png screenshots/phone/01_conversations.png
adb pull /sdcard/screen_02.png screenshots/phone/02_chat.png

# Cleanup
adb shell rm /sdcard/screen_*.png

echo "Screenshots saved to screenshots/phone/"
```

---

## ✅ Checklist trước khi upload

- [ ] Tối thiểu 2 phone screenshots (khuyến nghị 4-8)
- [ ] Screenshots rõ nét, không bị mờ
- [ ] Không chứa thông tin cá nhân
- [ ] Không chứa nội dung nhạy cảm
- [ ] Hiển thị đúng tính năng chính của app
- [ ] Kích thước đúng yêu cầu
- [ ] Định dạng PNG hoặc JPEG
- [ ] Đặt tên file có ý nghĩa

---

## 🎯 Lưu ý quan trọng

1. **Google Play yêu cầu BẮT BUỘC phone screenshots**
2. Tablet screenshots là TÙY CHỌN (nếu app support tablet)
3. Nên có 4-8 screenshots để showcase đầy đủ features
4. Screenshots phải là ảnh thật từ app, không phải mockup
5. Không được chứa watermark hoặc quảng cáo

---

## 🚀 Quick Start

### Cách nhanh nhất (dùng thiết bị thật):
```bash
# 1. Build và cài đặt
./gradlew installRelease

# 2. Mở app và chụp màn hình thủ công

# 3. Kéo ảnh về máy
adb pull /sdcard/Pictures/Screenshots/ ./screenshots/phone/
```

### Nếu không có thiết bị thật:
1. Mở Android Studio
2. Device Manager → Create Device → Pixel 6
3. Run app trên emulator
4. Dùng Camera tool để chụp screenshots

---

**Good luck với app submission! 🎉**
