package pang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RopeLoader {

    public static BufferedImage[] loadRopeFrames(String path) {
        BufferedImage[] frames = new BufferedImage[23];
        try {
            BufferedImage sheet = ImageIO.read(new File(path));
            Color bg = new Color(103, 150, 86); // yeşilimsi arka plan
            BufferedImage cleaned = makeTransparent(sheet, bg);

            // Tüm frame boyutları
            int[] heights = {
                34, 36, 38, 40, 43, 45, 47, 50, 52, 54, 56,
                59, 61, 63, 65, 68, 70, 72, 74, 77, 79, 81, 83
            };

            int x = 0;
            for (int i = 0; i < 23; i++) {
                frames[i] = cleaned.getSubimage(x, 0, 9, heights[i]);
                x += 9 + 8; // frame genişliği + boşluk
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return frames;
    }

    private static BufferedImage makeTransparent(BufferedImage image, Color target) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = new Color(image.getRGB(x, y));
                if (pixel.equals(target)) {
                    result.setRGB(x, y, 0x00000000);
                } else {
                    result.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return result;
    }
}

