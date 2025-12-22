#!/usr/bin/env python3
"""
Screenshot capture tool for Gemini Free app mockups
Captures HTML mockups as PNG screenshots with exact dimensions
"""

import os
import sys
from pathlib import Path

def capture_with_browser(html_file, output_file, width, height):
    """
    Capture screenshot using macOS built-in tools
    This is a fallback method that opens the file in browser
    """
    print(f"📸 Please capture {html_file} manually:")
    print(f"   1. Open {html_file} in Chrome/Firefox")
    print(f"   2. Press F12 (DevTools)")
    print(f"   3. Toggle device toolbar (Cmd+Shift+M on Mac)")
    print(f"   4. Set dimensions to {width}x{height}")
    print(f"   5. Right-click on page → 'Capture screenshot'")
    print(f"   6. Save as: {output_file}")
    print()

def main():
    print("=" * 70)
    print("  Gemini Free - Screenshot Capture Tool")
    print("=" * 70)
    print()

    # Get project directory
    project_dir = Path(__file__).parent
    screenshots_dir = project_dir / "screenshots"

    # Phone screenshots (1080 x 2340)
    phone_mockups = [
        ("phone_mockup_01_conversations.html", "phone/01_conversations.png"),
        ("phone_mockup_02_chat.html", "phone/02_chat.png"),
        ("phone_mockup_03_image_analysis.html", "phone/03_image_analysis.png"),
        ("phone_mockup_04_settings.html", "phone/04_settings.png"),
    ]

    print("📱 PHONE SCREENSHOTS (1080 x 2340)")
    print("-" * 70)
    for html_file, output_file in phone_mockups:
        html_path = screenshots_dir / html_file
        output_path = screenshots_dir / output_file

        if not html_path.exists():
            print(f"❌ File not found: {html_file}")
            continue

        print(f"\n🎯 Capturing: {html_file}")
        print(f"   → {output_file}")
        print(f"   Dimensions: 1080 x 2340 px")

        # Open in default browser
        os.system(f'open "{html_path}"')
        print(f"   ✓ Opened in browser")
        print(f"   📸 Use browser DevTools to capture screenshot")
        print(f"   💾 Save to: {output_path}")
        print()
        input("   Press ENTER when done...")

    print("\n" + "=" * 70)
    print("✅ Phone screenshots guide completed!")
    print("=" * 70)

    # Instructions for automated capture
    print("\n📝 For automated capture, you can use:")
    print()
    print("Method 1: Browser DevTools (Recommended)")
    print("  • Open each HTML file in Chrome/Firefox")
    print("  • Press F12 → Toggle device toolbar (Cmd+Shift+M)")
    print("  • Set dimensions to 1080 x 2340")
    print("  • Right-click → Capture screenshot")
    print()
    print("Method 2: Playwright (Advanced)")
    print("  • Install: pip install playwright")
    print("  • Run: playwright install")
    print("  • Use the automated script below")
    print()

    # Generate Playwright script
    print("\n📄 Automated Playwright Script:")
    print("-" * 70)
    playwright_script = '''
from playwright.sync_api import sync_playwright

def capture_screenshots():
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()

        screenshots = [
            ("phone_mockup_01_conversations.html", "phone/01_conversations.png", 1080, 2340),
            ("phone_mockup_02_chat.html", "phone/02_chat.png", 1080, 2340),
            ("phone_mockup_03_image_analysis.html", "phone/03_image_analysis.png", 1080, 2340),
            ("phone_mockup_04_settings.html", "phone/04_settings.png", 1080, 2340),
        ]

        for html_file, output_file, width, height in screenshots:
            print(f"Capturing {html_file}...")
            page.set_viewport_size({"width": width, "height": height})
            page.goto(f"file://{os.path.abspath('screenshots/' + html_file)}")
            page.screenshot(path=f"screenshots/{output_file}", full_page=True)
            print(f"  ✓ Saved to {output_file}")

        browser.close()
        print("\\n✅ All screenshots captured!")

if __name__ == "__main__":
    capture_screenshots()
'''
    print(playwright_script)
    print("-" * 70)

    # Save Playwright script
    playwright_file = project_dir / "auto_capture_playwright.py"
    with open(playwright_file, 'w') as f:
        f.write(playwright_script)
    print(f"\n💾 Saved automated script to: {playwright_file}")
    print()
    print("To use automated capture:")
    print("  1. pip install playwright")
    print("  2. playwright install chromium")
    print(f"  3. python {playwright_file}")
    print()

if __name__ == "__main__":
    main()
