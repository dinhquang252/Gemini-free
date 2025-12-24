# Hướng dẫn Push code lên GitHub

Code đã được commit local thành công! Để push lên GitHub, bạn cần authentication.

## Cách 1: Sử dụng GitHub CLI (Khuyến nghị)

```bash
# Install GitHub CLI nếu chưa có
brew install gh

# Login
gh auth login

# Push code
git push -u origin main
```

## Cách 2: Sử dụng Personal Access Token

1. Truy cập: https://github.com/settings/tokens/new
2. Tạo token với quyền `repo`
3. Copy token

```bash
# Push với token
git push https://<YOUR_TOKEN>@github.com/dinhquang252/Gemini-free.git main
```

## Cách 3: Sử dụng SSH (Nếu đã setup SSH key)

```bash
# Đổi remote sang SSH
git remote set-url origin git@github.com:dinhquang252/Gemini-free.git

# Push
git push -u origin main
```

## Cách 4: Push từ GitHub Desktop hoặc Android Studio

- Mở project trong Android Studio
- Vào VCS > Git > Push
- Nhập credentials khi được yêu cầu

## Kiểm tra sau khi push

Truy cập: https://github.com/dinhquang252/Gemini-free

## Files đã commit

- **102 files** với **12,421 dòng code**
- Tất cả features: Cloud Backup, Search, Voice Input, Dark Mode, etc.
- Documentation files
- Screenshots và graphics

## Branch

- **main** branch (default)

Chúc bạn thành công! 🚀
