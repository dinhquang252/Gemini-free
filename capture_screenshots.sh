#!/bin/bash

echo "=================================="
echo "  Gemini Free Screenshot Tool"
echo "=================================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Create directories
echo -e "${BLUE}Creating screenshot directories...${NC}"
mkdir -p screenshots/phone
mkdir -p screenshots/tablet-7
mkdir -p screenshots/tablet-10
echo -e "${GREEN}✓ Directories created${NC}"
echo ""

# Check if device is connected
echo -e "${BLUE}Checking for connected devices...${NC}"
if ! adb devices | grep -q "device$"; then
    echo -e "${YELLOW}⚠ No device connected!${NC}"
    echo "Please connect your Android device or start an emulator"
    exit 1
fi
echo -e "${GREEN}✓ Device found${NC}"
echo ""

# Build and install
echo -e "${BLUE}Building and installing app...${NC}"
./gradlew installRelease
if [ $? -ne 0 ]; then
    echo -e "${YELLOW}⚠ Build failed!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ App installed${NC}"
echo ""

# Launch app
echo -e "${BLUE}Launching app...${NC}"
adb shell am start -n com.project.googlegemini/.MainActivity
sleep 3
echo -e "${GREEN}✓ App launched${NC}"
echo ""

# Function to capture screenshot
capture_screen() {
    local filename=$1
    local description=$2

    echo -e "${BLUE}📸 Capturing: $description${NC}"
    adb shell screencap -p /sdcard/temp_screenshot.png
    adb pull /sdcard/temp_screenshot.png "screenshots/phone/$filename"
    adb shell rm /sdcard/temp_screenshot.png
    echo -e "${GREEN}✓ Saved: $filename${NC}"
    echo ""
}

# Interactive screenshot capture
echo "=================================="
echo "  Manual Screenshot Capture Mode"
echo "=================================="
echo ""
echo "Instructions:"
echo "1. Navigate to the screen you want to capture on your device"
echo "2. Press ENTER when ready to capture"
echo "3. Repeat for each screenshot"
echo ""

read -p "Ready to capture Screenshot 1 (Conversations List)? Press ENTER..."
capture_screen "01_conversations_list.png" "Conversations List"

read -p "Navigate to Chat screen. Ready to capture Screenshot 2? Press ENTER..."
capture_screen "02_chat_conversation.png" "Chat Conversation"

read -p "Ready to capture Screenshot 3? Press ENTER..."
capture_screen "03_screenshot.png" "Screenshot 3"

read -p "Ready to capture Screenshot 4? Press ENTER..."
capture_screen "04_screenshot.png" "Screenshot 4"

echo -e "${YELLOW}Do you want to capture more screenshots? (y/n)${NC}"
read -p "" answer

if [ "$answer" == "y" ]; then
    read -p "Ready to capture Screenshot 5? Press ENTER..."
    capture_screen "05_screenshot.png" "Screenshot 5"

    read -p "Ready to capture Screenshot 6? Press ENTER..."
    capture_screen "06_screenshot.png" "Screenshot 6"

    read -p "Ready to capture Screenshot 7? Press ENTER..."
    capture_screen "07_screenshot.png" "Screenshot 7"

    read -p "Ready to capture Screenshot 8? Press ENTER..."
    capture_screen "08_screenshot.png" "Screenshot 8"
fi

echo ""
echo "=================================="
echo -e "${GREEN}✓ Screenshot capture complete!${NC}"
echo "=================================="
echo ""
echo "Screenshots saved to: screenshots/phone/"
echo ""
echo "Next steps:"
echo "1. Review screenshots in screenshots/phone/"
echo "2. Rename files to meaningful names"
echo "3. Upload to Google Play Console"
echo ""
echo "To capture tablet screenshots:"
echo "1. Start tablet emulator"
echo "2. Run this script again"
echo "3. Move files to screenshots/tablet-7/ or screenshots/tablet-10/"
echo ""
