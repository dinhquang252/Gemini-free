#!/bin/bash

# Script to open HTML mockups in Chrome for manual screenshot capture
# Instructions will be provided for each mockup

echo "═══════════════════════════════════════════════════════════════════════"
echo "  Gemini Free - Screenshot Capture Helper"
echo "═══════════════════════════════════════════════════════════════════════"
echo ""
echo "This script will open each HTML mockup in Chrome."
echo "Follow the instructions to capture screenshots."
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

PROJECT_DIR="/Users/quangtran/AndroidStudioProjects/GoogleGemini"
SCREENSHOTS_DIR="$PROJECT_DIR/screenshots"

# Create directories if they don't exist
mkdir -p "$SCREENSHOTS_DIR/phone"
mkdir -p "$SCREENSHOTS_DIR/tablet-7"
mkdir -p "$SCREENSHOTS_DIR/tablet-10"

echo "📂 Screenshot directories ready"
echo ""

# Function to capture screenshot
capture_mockup() {
    local html_file=$1
    local output_file=$2
    local width=$3
    local height=$4
    local description=$5

    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}📸 $description${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo "   File: $html_file"
    echo "   Dimensions: ${width} x ${height}"
    echo "   Save as: $output_file"
    echo ""
    echo "   Opening in Chrome..."

    # Open in Chrome
    open -a "Google Chrome" "$SCREENSHOTS_DIR/$html_file"

    echo ""
    echo "   ${YELLOW}INSTRUCTIONS:${NC}"
    echo "   1. Press F12 (or Cmd+Option+I)"
    echo "   2. Click 'Toggle device toolbar' icon (or Cmd+Shift+M)"
    echo "   3. Set dimensions to: ${width} x ${height}"
    echo "   4. Click the 3-dot menu in device toolbar"
    echo "   5. Select 'Capture screenshot'"
    echo "   6. Save to: $SCREENSHOTS_DIR/$output_file"
    echo ""
    echo "   ${YELLOW}Alternative method:${NC}"
    echo "   1. Press F12"
    echo "   2. Right-click on '.phone-screen' or '.tablet-screen' element in Elements tab"
    echo "   3. Select 'Capture node screenshot'"
    echo "   4. Save to: $SCREENSHOTS_DIR/$output_file"
    echo ""

    read -p "   Press ENTER when screenshot is saved..."
    echo ""
}

# Phone Screenshots
echo "═══════════════════════════════════════════════════════════════════════"
echo "  📱 PHONE SCREENSHOTS (1080 x 2340)"
echo "═══════════════════════════════════════════════════════════════════════"
echo ""

capture_mockup \
    "phone_mockup_01_conversations.html" \
    "phone/01_conversations.png" \
    "1080" \
    "2340" \
    "Phone - Conversations List"

capture_mockup \
    "phone_mockup_02_chat.html" \
    "phone/02_chat.png" \
    "1080" \
    "2340" \
    "Phone - Chat Conversation"

capture_mockup \
    "phone_mockup_03_image_analysis.html" \
    "phone/03_image_analysis.png" \
    "1080" \
    "2340" \
    "Phone - Image Analysis"

capture_mockup \
    "phone_mockup_04_settings.html" \
    "phone/04_settings.png" \
    "1080" \
    "2340" \
    "Phone - Settings"

# 7-inch Tablet Screenshots
echo ""
echo "═══════════════════════════════════════════════════════════════════════"
echo "  📱 7-INCH TABLET SCREENSHOTS (1200 x 1920)"
echo "═══════════════════════════════════════════════════════════════════════"
echo ""
echo "   ${YELLOW}Note: Tablet screenshots are OPTIONAL but recommended${NC}"
echo ""

read -p "   Do you want to capture 7-inch tablet screenshots? (y/n) " answer
if [ "$answer" == "y" ]; then
    capture_mockup \
        "tablet7_mockup_01_conversations.html" \
        "tablet-7/01_conversations.png" \
        "1200" \
        "1920" \
        "7-inch Tablet - Conversations List"

    capture_mockup \
        "tablet7_mockup_02_chat.html" \
        "tablet-7/02_chat.png" \
        "1200" \
        "1920" \
        "7-inch Tablet - Chat"
else
    echo "   Skipping 7-inch tablet screenshots"
fi

# 10-inch Tablet Screenshots
echo ""
echo "═══════════════════════════════════════════════════════════════════════"
echo "  📱 10-INCH TABLET SCREENSHOTS (2560 x 1600)"
echo "═══════════════════════════════════════════════════════════════════════"
echo ""
echo "   ${YELLOW}Note: Tablet screenshots are OPTIONAL but recommended${NC}"
echo ""

read -p "   Do you want to capture 10-inch tablet screenshots? (y/n) " answer
if [ "$answer" == "y" ]; then
    capture_mockup \
        "tablet10_mockup_01_conversations.html" \
        "tablet-10/01_conversations.png" \
        "2560" \
        "1600" \
        "10-inch Tablet - Conversations (Landscape)"

    capture_mockup \
        "tablet10_mockup_02_chat.html" \
        "tablet-10/02_chat.png" \
        "2560" \
        "1600" \
        "10-inch Tablet - Chat (Landscape)"
else
    echo "   Skipping 10-inch tablet screenshots"
fi

# Summary
echo ""
echo "═══════════════════════════════════════════════════════════════════════"
echo -e "  ${GREEN}✅ Screenshot capture complete!${NC}"
echo "═══════════════════════════════════════════════════════════════════════"
echo ""
echo "📂 Screenshots saved to:"
echo "   Phone: $SCREENSHOTS_DIR/phone/"
echo "   7-inch Tablet: $SCREENSHOTS_DIR/tablet-7/"
echo "   10-inch Tablet: $SCREENSHOTS_DIR/tablet-10/"
echo ""
echo "📋 Next steps:"
echo "   1. Verify all screenshots have correct dimensions"
echo "   2. Check file sizes (must be < 8 MB each)"
echo "   3. Upload to Google Play Console:"
echo "      → Store presence → Main store listing → Graphics"
echo ""
echo "🔍 Verify dimensions:"
echo "   sips -g pixelWidth -g pixelHeight $SCREENSHOTS_DIR/phone/*.png"
echo ""
echo "Good luck! 🚀"
echo ""
