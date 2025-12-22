#!/usr/bin/env python3
"""
Generate Feature Graphic and App Icon for Gemini Free
- Feature Graphic: 1024 x 500 px PNG
- App Icon: 512 x 512 px PNG
"""

from PIL import Image, ImageDraw, ImageFont
import math

# Gemini AI Brand Colors
GEMINI_BLUE = (66, 133, 244)
GEMINI_GREEN = (52, 168, 83)
GEMINI_YELLOW = (251, 188, 4)
GEMINI_RED = (234, 67, 53)

def create_gradient_background(width, height, color1, color2, direction='horizontal'):
    """Create gradient background"""
    base = Image.new('RGB', (width, height), color1)
    top = Image.new('RGB', (width, height), color2)
    mask = Image.new('L', (width, height))
    mask_data = []

    for y in range(height):
        for x in range(width):
            if direction == 'horizontal':
                mask_data.append(int(255 * (x / width)))
            elif direction == 'vertical':
                mask_data.append(int(255 * (y / height)))
            elif direction == 'diagonal':
                mask_data.append(int(255 * ((x + y) / (width + height))))

    mask.putdata(mask_data)
    base.paste(top, (0, 0), mask)
    return base

def add_gradient_overlay(image, colors):
    """Add multi-color gradient overlay"""
    width, height = image.size
    overlay = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    draw = ImageDraw.Draw(overlay)

    # Create multi-stop gradient
    num_colors = len(colors)
    for i in range(width):
        progress = i / width
        segment = progress * (num_colors - 1)
        idx = int(segment)
        local_progress = segment - idx

        if idx >= num_colors - 1:
            color = colors[-1]
        else:
            color1 = colors[idx]
            color2 = colors[idx + 1]
            color = tuple(int(color1[j] + (color2[j] - color1[j]) * local_progress) for j in range(3))

        draw.line([(i, 0), (i, height)], fill=color)

    return overlay

def draw_letter_g(draw, x, y, size, colors):
    """Draw gradient letter G"""
    # Simplified G shape using arcs and rectangles
    # This is a simplified version - for production use a proper font

    # Outer circle
    bbox_outer = [x, y, x + size, y + size]
    draw.ellipse(bbox_outer, outline=colors[0], width=int(size * 0.15))

    # Inner circle (to make it hollow)
    margin = int(size * 0.15)
    bbox_inner = [x + margin, y + margin, x + size - margin, y + size - margin]
    draw.ellipse(bbox_inner, fill=(255, 255, 255))

    # Cut out part to make it G (right side opening)
    cut_width = int(size * 0.4)
    cut_height = int(size * 0.6)
    cut_x = x + int(size * 0.6)
    cut_y = y + int(size * 0.2)
    draw.rectangle([cut_x, cut_y, cut_x + cut_width, cut_y + cut_height], fill=(255, 255, 255))

    # Middle bar
    bar_y = y + int(size * 0.5)
    bar_x = x + int(size * 0.5)
    draw.rectangle([bar_x, bar_y - int(size * 0.075), x + size, bar_y + int(size * 0.075)], fill=colors[1])

def generate_feature_graphic():
    """Generate Feature Graphic 1024 x 500 px"""
    print("🎨 Generating Feature Graphic (1024 x 500)...")

    width, height = 1024, 500

    # Create gradient background
    image = Image.new('RGB', (width, height), (255, 255, 255))

    # Create multi-color gradient
    gradient = add_gradient_overlay(image, [GEMINI_BLUE, GEMINI_GREEN, GEMINI_YELLOW, GEMINI_RED])
    image = Image.alpha_composite(image.convert('RGBA'), gradient).convert('RGB')

    draw = ImageDraw.Draw(image)

    # Draw decorative circles
    for i in range(3):
        x = width * (0.1 + i * 0.3)
        y = height * (0.3 if i % 2 == 0 else 0.7)
        radius = 80 - i * 20
        draw.ellipse([x - radius, y - radius, x + radius, y + radius],
                     fill=(255, 255, 255, 30))

    # Draw logo circle
    logo_size = 180
    logo_x = width // 2 - logo_size // 2
    logo_y = height // 2 - logo_size // 2 - 50

    # White circle background
    draw.ellipse([logo_x - 10, logo_y - 10, logo_x + logo_size + 10, logo_y + logo_size + 10],
                 fill=(255, 255, 255))

    # Try to use a font for the G letter
    try:
        # Try different font paths
        font_paths = [
            "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
            "/System/Library/Fonts/Helvetica.ttc",
            "/Library/Fonts/Arial Bold.ttf",
            "arial.ttf"
        ]
        font = None
        for font_path in font_paths:
            try:
                font = ImageFont.truetype(font_path, 140)
                break
            except:
                continue

        if font:
            # Draw G with gradient effect
            text = "G"
            bbox = draw.textbbox((0, 0), text, font=font)
            text_width = bbox[2] - bbox[0]
            text_height = bbox[3] - bbox[1]
            text_x = logo_x + (logo_size - text_width) // 2
            text_y = logo_y + (logo_size - text_height) // 2 - 10

            # Create gradient text by drawing multiple times
            for i, color in enumerate([GEMINI_BLUE, GEMINI_GREEN, GEMINI_YELLOW, GEMINI_RED]):
                offset = i * 2
                draw.text((text_x + offset, text_y), text, font=font, fill=color)
        else:
            # Fallback: simple G shape
            draw_letter_g(draw, logo_x + 20, logo_y + 20, logo_size - 40,
                         [GEMINI_BLUE, GEMINI_GREEN])
    except Exception as e:
        print(f"   Font error: {e}, using simple shape")
        draw_letter_g(draw, logo_x + 20, logo_y + 20, logo_size - 40,
                     [GEMINI_BLUE, GEMINI_GREEN])

    # Draw text
    try:
        title_font = ImageFont.truetype("/System/Library/Fonts/Supplemental/Arial Bold.ttf", 72)
        subtitle_font = ImageFont.truetype("/System/Library/Fonts/Supplemental/Arial.ttf", 36)
    except:
        title_font = ImageFont.load_default()
        subtitle_font = ImageFont.load_default()

    # Title
    title_text = "GEMINI FREE"
    title_bbox = draw.textbbox((0, 0), title_text, font=title_font)
    title_width = title_bbox[2] - title_bbox[0]
    title_x = (width - title_width) // 2
    title_y = logo_y + logo_size + 30

    # Draw title with shadow
    draw.text((title_x + 2, title_y + 2), title_text, font=title_font, fill=(0, 0, 0, 100))
    draw.text((title_x, title_y), title_text, font=title_font, fill=(255, 255, 255))

    # Subtitle
    subtitle_text = "Your Free AI Assistant"
    subtitle_bbox = draw.textbbox((0, 0), subtitle_text, font=subtitle_font)
    subtitle_width = subtitle_bbox[2] - subtitle_bbox[0]
    subtitle_x = (width - subtitle_width) // 2
    subtitle_y = title_y + 80

    draw.text((subtitle_x, subtitle_y), subtitle_text, font=subtitle_font, fill=(255, 255, 255, 240))

    # Save
    output_path = "feature_graphic.png"
    image.save(output_path, "PNG", optimize=True)
    print(f"   ✅ Saved: {output_path}")
    print(f"   Size: {image.size[0]} x {image.size[1]} px")

    return output_path

def generate_app_icon():
    """Generate App Icon 512 x 512 px"""
    print("\n🎨 Generating App Icon (512 x 512)...")

    size = 512

    # Create white background with rounded corners
    image = Image.new('RGBA', (size, size), (255, 255, 255, 255))

    # Create gradient overlay
    gradient = add_gradient_overlay(image, [GEMINI_BLUE, GEMINI_GREEN, GEMINI_YELLOW, GEMINI_RED])

    # Apply gradient
    image = Image.alpha_composite(image, gradient)

    draw = ImageDraw.Draw(image)

    # Draw letter G
    try:
        font = ImageFont.truetype("/System/Library/Fonts/Supplemental/Arial Bold.ttf", 400)

        text = "G"
        bbox = draw.textbbox((0, 0), text, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]
        text_x = (size - text_width) // 2
        text_y = (size - text_height) // 2 - 20

        # Draw with white
        draw.text((text_x, text_y), text, font=font, fill=(255, 255, 255, 255))

    except Exception as e:
        print(f"   Font error: {e}, using default")
        # Fallback
        font = ImageFont.load_default()
        draw.text((size // 2 - 50, size // 2 - 50), "G", font=font, fill=(255, 255, 255))

    # Convert to RGB (remove alpha for compatibility)
    image = image.convert('RGB')

    # Save
    output_path = "app_icon.png"
    image.save(output_path, "PNG", optimize=True)
    print(f"   ✅ Saved: {output_path}")
    print(f"   Size: {image.size[0]} x {image.size[1]} px")

    return output_path

def main():
    print("=" * 70)
    print("  Gemini Free - Graphics Generator")
    print("=" * 70)
    print()

    # Generate Feature Graphic
    feature_path = generate_feature_graphic()

    # Generate App Icon
    icon_path = generate_app_icon()

    print()
    print("=" * 70)
    print("  ✅ Graphics Generated Successfully!")
    print("=" * 70)
    print()
    print(f"📂 Files created:")
    print(f"   1. {feature_path} (1024 x 500 px)")
    print(f"   2. {icon_path} (512 x 512 px)")
    print()
    print("📤 Ready to upload to Google Play Console!")
    print()

if __name__ == "__main__":
    main()
