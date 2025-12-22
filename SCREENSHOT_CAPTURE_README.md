# 📸 Screenshot Capture Instructions - Gemini Free

Hướng dẫn chi tiết để chụp screenshots từ HTML mockups cho Google Play Store.

---

## 📋 Files đã tạo

### Phone Screenshots (1080 x 2340)
- ✅ `phone_mockup_01_conversations.html` - Conversations List
- ✅ `phone_mockup_02_chat.html` - Chat Conversation
- ✅ `phone_mockup_03_image_analysis.html` - Image Analysis
- ✅ `phone_mockup_04_settings.html` - Settings Screen

### 7-inch Tablet Screenshots (1200 x 1920)
- ✅ `tablet7_mockup_01_conversations.html` - Conversations List
- ✅ `tablet7_mockup_02_chat.html` - Chat Screen

### 10-inch Tablet Screenshots (2560 x 1600)
- ✅ `tablet10_mockup_01_conversations.html` - Conversations (Landscape)
- ✅ `tablet10_mockup_02_chat.html` - Chat (Landscape)

---

## 🚀 Method 1: Automated Capture (Recommended)

### Install Playwright
```bash
# Install Playwright
pip3 install playwright

# Install browser
playwright install chromium
```

### Run Automated Script
```bash
# Make script executable
chmod +x auto_capture_all.py

# Run script
python3 auto_capture_all.py
```

### Output
Screenshots will be saved to:
- `screenshots/phone/` - 4 phone screenshots
- `screenshots/tablet-7/` - 2 tablet 7-inch screenshots
- `screenshots/tablet-10/` - 2 tablet 10-inch screenshots

---

## 🖱️ Method 2: Manual Capture (Browser)

### For macOS/Linux Users

#### Phone Screenshots (1080 x 2340)

```bash
# 1. Open each HTML file in Chrome
open -a "Google Chrome" screenshots/phone_mockup_01_conversations.html

# 2. Press F12 (or Cmd+Option+I on Mac)
# 3. Click "Toggle device toolbar" (Cmd+Shift+M)
# 4. Set dimensions: 1080 x 2340
# 5. Right-click on page → "Capture screenshot"
# 6. Save to: screenshots/phone/01_conversations.png
```

Repeat for:
- `phone_mockup_02_chat.html` → `phone/02_chat.png`
- `phone_mockup_03_image_analysis.html` → `phone/03_image_analysis.png`
- `phone_mockup_04_settings.html` → `phone/04_settings.png`

#### 7-inch Tablet Screenshots (1200 x 1920)

```bash
# Same process but set dimensions to: 1200 x 1920
open -a "Google Chrome" screenshots/tablet7_mockup_01_conversations.html
```

Capture:
- `tablet7_mockup_01_conversations.html` → `tablet-7/01_conversations.png`
- `tablet7_mockup_02_chat.html` → `tablet-7/02_chat.png`

#### 10-inch Tablet Screenshots (2560 x 1600)

```bash
# Same process but set dimensions to: 2560 x 1600
open -a "Google Chrome" screenshots/tablet10_mockup_01_conversations.html
```

Capture:
- `tablet10_mockup_01_conversations.html` → `tablet-10/01_conversations.png`
- `tablet10_mockup_02_chat.html` → `tablet-10/02_chat.png`

---

## 🎨 Method 3: Screenshot with Browser DevTools Console

### Quick Capture via Console

1. Open HTML file in Chrome
2. Press F12 → Console
3. Paste and run:

```javascript
// For phone screenshots (1080 x 2340)
document.querySelector('.phone-screen').scrollIntoView();
// Then: Right-click element → Capture node screenshot

// For 7-inch tablet (1200 x 1920)
document.querySelector('.tablet-screen').scrollIntoView();
// Then: Right-click element → Capture node screenshot

// For 10-inch tablet (2560 x 1600)
document.querySelector('.tablet-screen').scrollIntoView();
// Then: Right-click element → Capture node screenshot
```

---

## 📏 Verify Screenshot Dimensions

After capturing, verify dimensions:

```bash
# macOS
sips -g pixelWidth -g pixelHeight screenshots/phone/01_conversations.png

# Linux
identify screenshots/phone/01_conversations.png
```

Expected output:
- Phone: `1080 x 2340`
- 7-inch Tablet: `1200 x 1920`
- 10-inch Tablet: `2560 x 1600`

---

## ✅ Checklist

### Phone Screenshots (REQUIRED - 2 to 8 images)
- [ ] 01_conversations.png (1080 x 2340)
- [ ] 02_chat.png (1080 x 2340)
- [ ] 03_image_analysis.png (1080 x 2340)
- [ ] 04_settings.png (1080 x 2340)

### 7-inch Tablet (OPTIONAL)
- [ ] 01_conversations.png (1200 x 1920)
- [ ] 02_chat.png (1200 x 1920)

### 10-inch Tablet (OPTIONAL)
- [ ] 01_conversations.png (2560 x 1600)
- [ ] 02_chat.png (2560 x 1600)

---

## 🔍 Troubleshooting

### Issue: "Playwright not installed"
```bash
pip3 install playwright
playwright install chromium
```

### Issue: "File too large" (> 8 MB)
Screenshots should be PNG and well under 8 MB. If not:
```bash
# Compress PNG (macOS/Linux)
pngquant screenshots/phone/01_conversations.png --force --ext .png

# Or use ImageOptim (macOS)
# Or use online tools like tinypng.com
```

### Issue: Wrong dimensions
Make sure to:
1. Set correct viewport in DevTools
2. Capture the exact element (not full page)
3. Verify with `sips -g` or `identify` command

---

## 📤 Upload to Google Play Console

After capturing screenshots:

1. Go to Google Play Console
2. Navigate to: **Store presence** → **Main store listing**
3. Scroll to **Graphics**
4. Upload screenshots:
   - **Phone screenshots**: Upload all 4 from `screenshots/phone/`
   - **7-inch tablet** (optional): Upload from `screenshots/tablet-7/`
   - **10-inch tablet** (optional): Upload from `screenshots/tablet-10/`

---

## 📝 Notes

- **File format**: PNG or JPEG
- **Max size**: 8 MB each
- **Aspect ratio**: 16:9 or 9:16 (phone/7-inch), 16:10 (10-inch landscape)
- **Dimensions**:
  - Phone: 1080 x 2340 (within 320-3840 range) ✅
  - 7-inch: 1200 x 1920 (within 320-3840 range) ✅
  - 10-inch: 2560 x 1600 (within 1080-7680 range) ✅

All dimensions meet Google Play Store requirements! 🎉

---

## 🎯 Quick Start

**Fastest way (automated):**
```bash
pip3 install playwright
playwright install chromium
python3 auto_capture_all.py
```

**Manual way (if automated fails):**
1. Open each HTML file in Chrome
2. F12 → Toggle device toolbar
3. Set dimensions (see above)
4. Right-click → Capture screenshot
5. Save to appropriate folder

---

**Good luck! 🚀**
