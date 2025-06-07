package pang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Balon patlama efektleri:
//L kendi efektini, M balon -> L efektini, S kendi, XS kendi efektini kullanıyor.
public class ExplosionLoader {
	public static BufferedImage[] loadExplosionFrames(String path, int frameCount) {
	    BufferedImage[] frames = new BufferedImage[frameCount];
	    try {
	        BufferedImage sheet = ImageIO.read(new File(path));
	        Color bg = new Color(103, 150, 86); // yeşil arka plan
	        BufferedImage cleaned = makeTransparent(sheet, bg);

	        int frameWidth = sheet.getWidth() / frameCount;
	        int frameHeight = sheet.getHeight();

	        for (int i = 0; i < frameCount; i++) {
	            frames[i] = cleaned.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return frames;
	}

	// Şeffaflaştırma:
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
