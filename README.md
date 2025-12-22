# Ứng Dụng Chat với Google Gemini AI

Ứng dụng Android chat sử dụng **Google Gemini 2.5 Flash** với giao diện đẹp mắt được xây dựng bằng Jetpack Compose.

## Tính năng

- ✨ Chat real-time với **Gemini 2.5 Flash** (model mới nhất, nhanh và thông minh)
- 🎨 Giao diện Material Design 3 hiện đại
- 🔐 **Mỗi người dùng tự nhập API key riêng** (an toàn để publish lên store)
- 💾 Lưu API key an toàn trên thiết bị
- ⚙️ Màn hình cài đặt để quản lý API key
- 🗑️ Xóa lịch sử chat
- 📜 Tự động cuộn xuống tin nhắn mới
- 💰 **Hoàn toàn miễn phí** với free tier

## Yêu cầu

- Android Studio Hedgehog trở lên
- Android SDK 26 trở lên
- Google AI API Key (miễn phí - người dùng tự tạo khi cài app)

## Cách cài đặt cho Developer

### Bước 1: Clone repository

```bash
git clone https://github.com/your-username/GoogleGemini.git
cd GoogleGemini
```

### Bước 2: Mở project trong Android Studio

1. Mở Android Studio
2. File → Open
3. Chọn thư mục project
4. Đợi Gradle sync

### Bước 3: Build và chạy

1. Build project
   - Build → Make Project (Ctrl+F9 / Cmd+F9)
2. Chạy trên emulator hoặc thiết bị thực
   - Run → Run 'app' (Shift+F10 / Ctrl+R)

### Bước 4: Nhập API Key lần đầu

Khi chạy app lần đầu, bạn sẽ thấy màn hình setup:

1. App sẽ hiển thị màn hình hướng dẫn
2. Nhấn **"Lấy API Key (MIỄN PHÍ)"**
3. Trình duyệt sẽ mở https://aistudio.google.com/apikey
4. Đăng nhập Google và nhấn **"Create API Key"**
5. Copy API key
6. Quay lại app và dán vào ô input
7. Nhấn **"Lưu và Bắt đầu"**

**Xong!** Bạn có thể bắt đầu chat với Gemini.

## Hướng dẫn cho người dùng cuối

### Lần đầu sử dụng

1. **Cài đặt app** từ Google Play Store
2. **Mở app** - Bạn sẽ thấy màn hình chào mừng
3. **Nhấn "Lấy API Key"** - App sẽ mở trình duyệt
4. **Đăng nhập Google** và tạo API key miễn phí
5. **Copy API key** và dán vào app
6. **Nhấn "Lưu và Bắt đầu"**
7. **Bắt đầu chat!**

### Quản lý API Key

- Nhấn icon **⚙️ Settings** ở góc trên bên phải
- Bạn có thể:
  - Xem API key hiện tại (được ẩn bớt để bảo mật)
  - Đổi sang API key khác
  - Xóa API key

### Lưu ý quan trọng

- ⚠️ **API key là của riêng bạn** - Mỗi người dùng có quota riêng
- ⚠️ **Không chia sẻ API key** với người khác
- ⚠️ API key được lưu **an toàn trên thiết bị** của bạn
- ⚠️ **Không cần lo quota chung bị hết** - Mỗi người dùng có quota riêng

## Cấu trúc dự án

```
app/src/main/java/com/project/googlegemini/
├── data/
│   └── Message.kt              # Model cho tin nhắn
├── ui/
│   ├── ChatScreen.kt           # Màn hình chat chính
│   ├── SetupScreen.kt          # Màn hình nhập API key lần đầu
│   ├── SettingsScreen.kt       # Màn hình cài đặt
│   └── theme/                  # Theme và colors
├── utils/
│   └── ApiKeyManager.kt        # Quản lý lưu trữ API key
├── viewmodel/
│   └── ChatViewModel.kt        # Logic xử lý chat
└── MainActivity.kt             # Activity chính với Navigation
```

## Công nghệ sử dụng

- **Jetpack Compose** - UI toolkit hiện đại
- **Material Design 3** - Thiết kế UI
- **Navigation Compose** - Điều hướng giữa các màn hình
- **Kotlin Coroutines** - Xử lý bất đồng bộ
- **ViewModel** - Quản lý state
- **SharedPreferences** - Lưu trữ API key an toàn
- **Google AI SDK** - Tích hợp Gemini AI
- **Gemini 2.5 Flash** - Model AI mới nhất

## Về Gemini 2.5 Flash

**Gemini 2.5 Flash** là model AI thế hệ mới nhất từ Google với:
- ⚡ **Cực kỳ nhanh** - Response time dưới 1 giây
- 🧠 **Thông minh** - Hiểu ngữ cảnh tốt hơn các phiên bản trước
- 💰 **Miễn phí** - Free tier rất hào phóng cho cá nhân
- 🌍 **Đa ngôn ngữ** - Hỗ trợ tiếng Việt tốt

## Quota và giới hạn miễn phí

### Free Tier (Mỗi API key):
- **15 requests/phút**
- **1,500 requests/ngày**
- **1 triệu tokens/tháng**

Với quota này, **mỗi người dùng** có thể:
- ✅ Chat bình thường hàng ngày
- ✅ Sử dụng cho mục đích cá nhân
- ✅ Không lo bị giới hạn bởi người dùng khác

### Theo dõi usage của bạn:
- Truy cập: https://aistudio.google.com/apikey
- Xem tab **"Usage"** để theo dõi số requests đã sử dụng

## Tại sao cách này tốt để publish app?

### ✅ Ưu điểm cho Developer:
1. **Không lo quota bị hết** - Mỗi user dùng API key riêng
2. **Không mất phí** - User tự chịu trách nhiệm quota của họ
3. **An toàn** - Không hardcode API key trong app
4. **Dễ scale** - Không giới hạn số lượng người dùng
5. **Tuân thủ Google ToS** - Đúng cách Google khuyến nghị

### ✅ Ưu điểm cho User:
1. **Miễn phí** - Free tier rất hào phóng
2. **Riêng tư** - API key của họ, quota của họ
3. **Kiểm soát** - Có thể theo dõi usage của chính mình
4. **Linh hoạt** - Có thể thay đổi/xóa API key bất cứ lúc nào

## Khắc phục sự cố

### App yêu cầu API key ngay khi mở
- ✅ Đây là behavior bình thường cho lần đầu sử dụng
- ✅ Làm theo hướng dẫn trên màn hình để lấy API key miễn phí

### Lỗi "API key not valid"
- ✅ Kiểm tra API key đã copy đầy đủ chưa
- ✅ API key phải bắt đầu bằng "AIza"
- ✅ Thử copy lại từ Google AI Studio

### Lỗi "Quota exceeded"
- ✅ Bạn đã vượt quota miễn phí của API key
- ✅ Đợi 1 phút (cho rate limit) hoặc ngày hôm sau (cho daily limit)
- ✅ Hoặc tạo API key mới (nếu cần)

### Quên API key / Muốn đổi API key
- ✅ Vào Settings (icon ⚙️)
- ✅ Nhấn "Đổi" hoặc "Xóa"
- ✅ Nhập API key mới

## Chi phí

**App hoàn toàn miễn phí:**
- ✅ Không có in-app purchase
- ✅ Không có quảng cáo
- ✅ Free tier của Gemini đủ cho 99% người dùng cá nhân

**User chỉ cần:**
- Tài khoản Google (miễn phí)
- Tạo API key (miễn phí, không cần thẻ tín dụng)

## Publishing lên Google Play Store

App này **SẴN SÀNG** để publish vì:

1. ✅ **Không hardcode API key** - Mỗi user tự nhập
2. ✅ **Tuân thủ Google ToS** - Không vi phạm quota chung
3. ✅ **Scalable** - Không giới hạn số lượng người dùng
4. ✅ **Privacy-friendly** - Mỗi user quản lý API key riêng
5. ✅ **No backend needed** - App chạy hoàn toàn client-side

### Checklist trước khi publish:

- [ ] Cập nhật version code và version name
- [ ] Generate signed APK/AAB
- [ ] Tạo store listing (mô tả, screenshots, v.v.)
- [ ] Thêm Privacy Policy (nếu cần)
- [ ] Test trên nhiều thiết bị khác nhau
- [ ] Review Google Play policies

## Đóng góp

Nếu bạn muốn đóng góp cho dự án:
1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## Giấy phép

Dự án này được tạo ra cho mục đích học tập và có thể sử dụng tự do.

## Liên hệ

Nếu có bất kỳ câu hỏi nào, vui lòng tạo issue trong repository.

## Credits

- **Google Gemini AI** - https://ai.google.dev/
- **Jetpack Compose** - https://developer.android.com/compose
- **Material Design 3** - https://m3.material.io/
- **Navigation Compose** - https://developer.android.com/jetpack/compose/navigation

---

**Made with ❤️ using Jetpack Compose and Gemini AI**
