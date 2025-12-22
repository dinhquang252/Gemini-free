# 🚀 Google Play Store Submission Guide - Gemini Free

Tài liệu hướng dẫn đầy đủ để đẩy app **Gemini Free** lên Google Play Store.

---

## 📋 Checklist tổng quan

- [x] ✅ Short description
- [x] ✅ Full description
- [x] ✅ Feature graphic (1024x500)
- [ ] 📸 Phone screenshots (2-8 ảnh)
- [ ] 📸 7-inch tablet screenshots (tùy chọn)
- [ ] 📸 10-inch tablet screenshots (tùy chọn)
- [x] ✅ App icon (đã có trong project)
- [x] ✅ Privacy policy
- [x] ✅ Release APK (đã build)

---

## 📁 Files đã tạo sẵn

### 1. **google_play_store_listing.txt**
Chứa Short Description và Full Description
- ✅ Short description: 80 ký tự
- ✅ Full description: 4000 ký tự với emojis và formatting

### 2. **feature_graphic.html**
Template để tạo Feature Graphic (1024x500px)
- Mở file này bằng browser
- Press F11 (fullscreen)
- Chụp screenshot vùng gradient box
- Hoặc dùng browser dev tools để chụp chính xác kích thước

### 3. **privacy_policy.html**
Privacy Policy đầy đủ cho app
- ⚠️ **LƯU Ý:** Cần thay `[Your Email Address]` bằng email thật
- Cần host file này online (GitHub Pages, Google Sites, Firebase Hosting)

### 4. **SCREENSHOTS_GUIDE.md**
Hướng dẫn chi tiết cách chụp screenshots

### 5. **capture_screenshots.sh**
Script tự động chụp screenshots
```bash
./capture_screenshots.sh
```

---

## 🎯 Bước 1: Hoàn thiện Privacy Policy

### Cách 1: GitHub Pages (Khuyến nghị)
```bash
# 1. Tạo GitHub repository public
# Tên repo: gemini-free-privacy

# 2. Upload privacy_policy.html
git init
git add privacy_policy.html
git commit -m "Add privacy policy"
git branch -M main
git remote add origin https://github.com/[your-username]/gemini-free-privacy.git
git push -u origin main

# 3. Enable GitHub Pages
# Settings → Pages → Source: main branch

# 4. Link sẽ là:
# https://[your-username].github.io/gemini-free-privacy/privacy_policy.html
```

### Cách 2: Firebase Hosting (Nhanh)
```bash
# 1. Install Firebase CLI
npm install -g firebase-tools

# 2. Login
firebase login

# 3. Init hosting
firebase init hosting

# 4. Deploy
firebase deploy --only hosting

# Link: https://[project-id].web.app/privacy_policy.html
```

### Cách 3: Google Sites (Đơn giản nhất)
1. Vào https://sites.google.com
2. Tạo site mới
3. Copy nội dung từ `privacy_policy.html`
4. Paste vào site (chuyển sang text mode)
5. Publish → lấy link

---

## 🎨 Bước 2: Tạo Feature Graphic

### Option A: Từ HTML template
```bash
# 1. Mở feature_graphic.html bằng Chrome/Firefox
open feature_graphic.html

# 2. Press F11 để fullscreen

# 3. Chụp chính xác vùng 1024x500px:
#    - Dùng browser dev tools (F12)
#    - Console: document.querySelector('.feature-graphic').scrollIntoView()
#    - Right click → Capture node screenshot

# 4. Lưu thành: feature_graphic.png
```

### Option B: Tự thiết kế
Dùng Canva, Figma, hoặc Photoshop:
- Kích thước: 1024 x 500 pixels
- Format: PNG hoặc JPEG 24-bit
- Nội dung: App name, key features, gradient background
- Không có text quá nhỏ (phải đọc được)

---

## 📸 Bước 3: Chụp Screenshots

### A. Phone Screenshots (BẮT BUỘC - 2 đến 8 ảnh)

#### Cách 1: Script tự động (Khuyến nghị)
```bash
# Kết nối device hoặc mở emulator
# Pixel 6 emulator (1080 x 2400)

# Chạy script
./capture_screenshots.sh

# Follow instructions
```

#### Cách 2: Thủ công
```bash
# 1. Cài app
./gradlew installRelease

# 2. Mở app và navigate đến từng màn hình

# 3. Chụp bằng camera icon trong Android Studio Device Manager

# 4. Hoặc dùng adb:
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png screenshots/phone/01_screenshot.png
```

#### Danh sách screenshots cần chụp (khuyến nghị):
1. **Conversations List** - Màn hình chính với list conversations
2. **Chat Conversation** - Hội thoại AI với tin nhắn
3. **Image Analysis** - Upload và phân tích ảnh
4. **Dark Mode** - Giao diện dark mode
5. **Settings** - Màn hình settings
6. **Light Mode** - Giao diện light mode với gradient
7. **Multiple Chats** - Nhiều conversations
8. **Long Conversation** - Hội thoại dài

### B. Tablet Screenshots (TÙY CHỌN)

#### 7-inch Tablet (Nexus 7 - 1200 x 1920)
```bash
# 1. Tạo Nexus 7 emulator trong Android Studio
# Device Manager → Create Device → Nexus 7

# 2. Run app và chụp tương tự phone

# 3. Lưu vào screenshots/tablet-7/
```

#### 10-inch Tablet (Pixel Tablet - 2560 x 1600)
```bash
# 1. Tạo Pixel Tablet emulator
# Device Manager → Create Device → Pixel Tablet

# 2. Run app và chụp

# 3. Lưu vào screenshots/tablet-10/
```

---

## 📦 Bước 4: Chuẩn bị APK

### Build Release APK
```bash
# Build đã hoàn tất, file APK tại:
# app/build/outputs/apk/release/app-release.apk

# Kiểm tra APK đã sign
./gradlew assembleRelease

# Verify signing
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

### Thông tin Keystore
```
Store: gemini-release.keystore
Alias: gemini-key
Password: geminifree123
```

⚠️ **LƯU Ý:** Giữ file keystore này cẩn thận! Nếu mất, không thể update app!

---

## 🌐 Bước 5: Google Play Console Setup

### 5.1 Tạo App mới
1. Vào https://play.google.com/console
2. **Create app**
3. Điền thông tin:
   - **App name:** Gemini Free
   - **Default language:** English (US) hoặc Vietnamese
   - **App or game:** App
   - **Free or paid:** Free

### 5.2 Store listing
Navigate to: **Store presence → Main store listing**

#### App details
- **App name:** Gemini Free
- **Short description:** Copy từ `google_play_store_listing.txt` (Short Description section)
- **Full description:** Copy từ `google_play_store_listing.txt` (Full Description section)

#### Graphics
- **App icon:** Tự động lấy từ APK (ic_launcher)
- **Feature graphic:** Upload `feature_graphic.png` (1024 x 500)
- **Phone screenshots:** Upload 2-8 ảnh từ `screenshots/phone/`
- **7-inch tablet screenshots:** (Optional) Upload từ `screenshots/tablet-7/`
- **10-inch tablet screenshots:** (Optional) Upload từ `screenshots/tablet-10/`

#### Categorization
- **App category:** Productivity hoặc Tools
- **Tags:** AI, Chat, Assistant, Free

#### Contact details
- **Email:** [Your Email]
- **Website:** (Optional) Link đến GitHub hoặc landing page
- **Privacy policy URL:** Link từ Bước 1 (GitHub Pages/Firebase/Google Sites)

### 5.3 Content rating
1. Navigate to: **Policy → App content**
2. Click **Start questionnaire**
3. Điền thông tin:
   - **Email:** Your email
   - **Category:** Select appropriate category
   - Answer questions about content (No violence, No inappropriate content, etc.)
4. **Save** → **Get rating**

### 5.4 Target audience and content
1. **Target age:** 13+ (hoặc phù hợp)
2. **Store presence:** Optimize for families (optional)
3. **Ads:** Yes (because using AdMob)

### 5.5 Data safety
1. Navigate to: **Policy → Data safety**
2. Click **Start**
3. Answer questions:
   - **Does your app collect user data?** Yes
   - **Data types:**
     - Messages/Chat (stored on device)
     - Photos (optional, when user uploads)
   - **Is data shared with third parties?** Yes (Google Gemini AI, AdMob)
   - **Data security:**
     - Data encrypted in transit (Yes)
     - Users can request data deletion (Yes - by deleting conversations)
     - Privacy policy (Yes - provide link)

### 5.6 App access
- **Special access:** None required

---

## 📤 Bước 6: Upload APK/AAB

### Option A: Upload APK
```bash
# Đã có APK tại: app/build/outputs/apk/release/app-release.apk
```

1. Navigate to: **Release → Production**
2. **Create new release**
3. **Upload APK**: Upload `app-release.apk`
4. **Release name:** 1.0
5. **Release notes:**
```
Initial release of Gemini Free

Features:
- Unlimited AI chat powered by Google Gemini
- Image analysis support (up to 5 images)
- Beautiful gradient UI
- Dark mode & Light mode
- Conversation management
- Swipe to delete conversations
- Stop generation feature
```

### Option B: Build và Upload AAB (Khuyến nghị)
```bash
# Build Android App Bundle (Google khuyến nghị)
./gradlew bundleRelease

# File tại: app/build/outputs/bundle/release/app-release.aab
```

Upload AAB thay vì APK cho kích thước nhỏ hơn.

---

## ✅ Bước 7: Review và Publish

### 7.1 Review checklist
- [ ] Store listing hoàn chỉnh
- [ ] Graphics đầy đủ (icon, feature graphic, screenshots)
- [ ] Content rating đã có
- [ ] Data safety đã điền
- [ ] Privacy policy URL active
- [ ] APK/AAB đã upload
- [ ] Release notes đã viết

### 7.2 Submit for Review
1. Navigate to: **Publishing overview**
2. Review tất cả sections (phải có checkmark xanh)
3. Click **Send for review**
4. Đợi Google review (thường 1-3 ngày)

### 7.3 Sau khi được approve
- App sẽ live trên Google Play Store
- Share link: `https://play.google.com/store/apps/details?id=com.project.googlegemini`

---

## 📋 Thông tin App

```
Package name: com.project.googlegemini
Version code: 1
Version name: 1.0
Min SDK: 26 (Android 8.0)
Target SDK: 36 (Android 14)
```

---

## 🔧 Troubleshooting

### Lỗi "Privacy policy URL not accessible"
- Đảm bảo link public và accessible
- Test link trong incognito browser
- Link phải dùng HTTPS

### Lỗi "Screenshots wrong size"
- Phone: Tối thiểu 320px, max 3840px
- Recommended: 1080 x 2340px (portrait)
- Check file size < 8MB per screenshot

### Lỗi "Content rating required"
- Hoàn thành questionnaire trong **App content**
- Phải có rating trước khi publish

### Lỗi "Data safety required"
- Điền đầy đủ thông tin trong **Data safety** section
- Provide privacy policy URL

---

## 📞 Support

Nếu gặp vấn đề:
1. Check Google Play Console Help: https://support.google.com/googleplay/android-developer
2. Review rejection reasons carefully
3. Update và resubmit

---

## 🎉 Checklist cuối cùng

Trước khi submit, đảm bảo:

- [x] ✅ App build thành công
- [x] ✅ Keystore đã backup an toàn
- [ ] 📧 Email trong privacy policy đã update
- [ ] 🌐 Privacy policy URL public và working
- [ ] 🎨 Feature graphic đã tạo (1024x500)
- [ ] 📸 Screenshots đã chụp (2-8 ảnh phone)
- [ ] 📝 Short & Full description đã copy
- [ ] ⭐ Content rating đã có
- [ ] 🔒 Data safety đã điền
- [ ] 📦 APK/AAB đã upload
- [ ] ✍️ Release notes đã viết

---

**Good luck! 🚀**

Sau khi app được approve, đừng quên share link với bạn bè! 🎊
