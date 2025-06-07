package pang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerSpriteLoader {

    public static BufferedImage[] loadPlayerFrames(String path, int frameCount, boolean mirrored) {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File(path));
            int frameWidth = 32;         // Karakterin genişliği
            int frameHeight = 32;        // Karakterin yüksekliği
            int frameSpacing = 2;        // Her kare sonrası 2px pembe çizgi

            BufferedImage[] frames = new BufferedImage[frameCount];

            for (int i = 0; i < frameCount; i++) {
                int x = i * (frameWidth + frameSpacing);
                BufferedImage frame = spriteSheet.getSubimage(x, 0, frameWidth, frameHeight);
                frame = makeTransparent(frame,
                        new Color(255, 0, 255), // pembe çizgiler 2px
                        new Color(0, 255, 0)    // yeşil zemin
                );
                frames[i] = mirrored ? mirrorImage(frame) : frame;
            }

            return frames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // şeffaflaştırma için :
    private static BufferedImage makeTransparent(BufferedImage image, Color... colorsToReplace) {
        BufferedImage transparent = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                Color pixel = new Color(rgb, true);

                boolean match = false;
                for (Color c : colorsToReplace) {
                    if (pixel.equals(c)) {
                        match = true;
                        break;
                    }
                }

                if (match) {
                    transparent.setRGB(x, y, 0x00000000);
                } else {
                    transparent.setRGB(x, y, rgb);
                }
            }
        }

        return transparent;
    }
    // karakterin sol yönü için aynalama
    private static BufferedImage mirrorImage(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage mirrored = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = mirrored.createGraphics();
        g2.drawImage(img, 0, 0, w, h, w, 0, 0, h, null); // X ekseninde aynalama
        g2.dispose();

        return mirrored;
    }
}
