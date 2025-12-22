#!/usr/bin/env python3
"""
Automated screenshot capture using Playwright
Captures all phone and tablet mockups
"""

import os
import sys
from pathlib import Path

try:
    from playwright.sync_api import sync_playwright
except ImportError:
    print("❌ Playwright not installed!")
    print("Install with: pip install playwright")
    print("Then run: playwright install chromium")
    sys.exit(1)

def capture_screenshots():
    project_dir = Path(__file__).parent
    screenshots_dir = project_dir / "screenshots"

    # Define all screenshots to capture
    screenshots = [
        # Phone screenshots (1080 x 2340) - 16:9 aspect ratio
        ("phone_mockup_01_conversations.html", "phone/01_conversations.png", 1080, 2340),
        ("phone_mockup_02_chat.html", "phone/02_chat.png", 1080, 2340),
        ("phone_mockup_03_image_analysis.html", "phone/03_image_analysis.png", 1080, 2340),
        ("phone_mockup_04_settings.html", "phone/04_settings.png", 1080, 2340),

        # 7-inch tablet screenshots (1200 x 1920) - 16:9 aspect ratio
        ("tablet7_mockup_01_conversations.html", "tablet-7/01_conversations.png", 1200, 1920),
        ("tablet7_mockup_02_chat.html", "tablet-7/02_chat.png", 1200, 1920),

        # 10-inch tablet screenshots (2560 x 1600 landscape) - 16:10 aspect ratio
        ("tablet10_mockup_01_conversations.html", "tablet-10/01_conversations.png", 2560, 1600),
        ("tablet10_mockup_02_chat.html", "tablet-10/02_chat.png", 2560, 1600),
    ]

    print("=" * 80)
    print("  Gemini Free - Automated Screenshot Capture")
    print("=" * 80)
    print()

    with sync_playwright() as p:
        print("🚀 Launching browser...")
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        captured = 0
        skipped = 0

        for html_file, output_file, width, height in screenshots:
            html_path = screenshots_dir / html_file
            output_path = screenshots_dir / output_file

            if not html_path.exists():
                print(f"⚠️  Skipped: {html_file} (not found)")
                skipped += 1
                continue

            # Ensure output directory exists
            output_path.parent.mkdir(parents=True, exist_ok=True)

            print(f"📸 Capturing: {html_file}")
            print(f"   Dimensions: {width} x {height} px")

            try:
                # Set viewport and capture
                page.set_viewport_size({"width": width, "height": height})
                page.goto(f"file://{html_path.absolute()}")

                # Wait a moment for rendering
                page.wait_for_timeout(500)

                # Capture screenshot
                page.screenshot(path=str(output_path), full_page=False)

                print(f"   ✅ Saved: {output_file}")
                print()
                captured += 1

            except Exception as e:
                print(f"   ❌ Error: {str(e)}")
                print()
                skipped += 1

        browser.close()

        print("=" * 80)
        print(f"✅ Screenshot capture complete!")
        print(f"   Captured: {captured}")
        print(f"   Skipped: {skipped}")
        print("=" * 80)
        print()
        print("📂 Screenshots saved to:")
        print(f"   Phone: {screenshots_dir / 'phone'}")
        print(f"   7-inch Tablet: {screenshots_dir / 'tablet-7'}")
        print(f"   10-inch Tablet: {screenshots_dir / 'tablet-10'}")
        print()

if __name__ == "__main__":
    try:
        capture_screenshots()
    except KeyboardInterrupt:
        print("\n\n⚠️  Interrupted by user")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Error: {str(e)}")
        sys.exit(1)
