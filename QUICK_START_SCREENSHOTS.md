# 🚀 Quick Start - Capture Screenshots

## Fastest Way: Run This Script!

```bash
cd /Users/quangtran/AndroidStudioProjects/GoogleGemini
./open_mockups_for_capture.sh
```

Script sẽ:
- ✅ Mở từng HTML mockup trong Chrome
- ✅ Hiển thị hướng dẫn chi tiết cho từng screenshot
- ✅ Đợi bạn chụp xong rồi mới chuyển sang file tiếp theo
- ✅ Tạo sẵn thư mục để save screenshots

---

## 📸 Manual Quick Steps

Nếu không muốn dùng script, làm manual:

### 1. Open HTML file
```bash
open -a "Google Chrome" screenshots/phone_mockup_01_conversations.html
```

### 2. In Chrome:
- Press **F12** (hoặc Cmd+Option+I)
- Click **Toggle device toolbar** (Cmd+Shift+M)
- Set dimensions:
  - Phone: `1080 x 2340`
  - 7-inch tablet: `1200 x 1920`
  - 10-inch tablet: `2560 x 1600`

### 3. Capture:
- Click 3-dot menu in device toolbar
- **"Capture screenshot"**
- Save to `screenshots/phone/01_conversations.png`

### Hoặc dùng Element Screenshot:
- F12 → Elements tab
- Right-click element `.phone-screen` hoặc `.tablet-screen`
- **"Capture node screenshot"**

---

## 📁 Files to Capture

### Phone (BẮT BUỘC - 4 ảnh)
1. `phone_mockup_01_conversations.html` → `phone/01_conversations.png`
2. `phone_mockup_02_chat.html` → `phone/02_chat.png`
3. `phone_mockup_03_image_analysis.html` → `phone/03_image_analysis.png`
4. `phone_mockup_04_settings.html` → `phone/04_settings.png`

### 7-inch Tablet (TÙY CHỌN - 2 ảnh)
1. `tablet7_mockup_01_conversations.html` → `tablet-7/01_conversations.png`
2. `tablet7_mockup_02_chat.html` → `tablet-7/02_chat.png`

### 10-inch Tablet (TÙY CHỌN - 2 ảnh)
1. `tablet10_mockup_01_conversations.html` → `tablet-10/01_conversations.png`
2. `tablet10_mockup_02_chat.html` → `tablet-10/02_chat.png`

---

## ✅ Verify Screenshots

```bash
# Check dimensions
sips -g pixelWidth -g pixelHeight screenshots/phone/*.png

# Check file sizes (must be < 8 MB)
ls -lh screenshots/phone/
```

Expected:
- Phone: 1080 x 2340 pixels
- 7-inch: 1200 x 1920 pixels
- 10-inch: 2560 x 1600 pixels
- All files < 8 MB

---

## 📤 Upload to Google Play

1. Go to: https://play.google.com/console
2. Navigate: **Store presence** → **Main store listing**
3. Scroll to: **Graphics**
4. Upload screenshots from `screenshots/` folder

---

## Need Help?

Read full documentation:
- `SCREENSHOT_CAPTURE_README.md` - Detailed instructions
- `GOOGLE_PLAY_SUBMISSION_GUIDE.md` - Complete submission guide

---

**Let's capture some beautiful screenshots! 📸**
