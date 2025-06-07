package pang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BallLoader {

    public static BufferedImage[] loadBallFrames(String path) {
        BufferedImage[] balls = new BufferedImage[4];
        try {
            BufferedImage sheet = ImageIO.read(new File(path));

            //Arka planı transparan yap kahverengi renk kodu #804000
            Color background = new Color(128, 64, 0); // Görselin arka plan rengi
            BufferedImage cleaned = makeTransparent(sheet, background);

            //Kesim noktaları (boşluklar dahil!):
            //(?, ?, ?, ?) == (x başlangıç px, y başlangıç px, x balon px, y balon px)
            balls[0] = cleaned.getSubimage(0, 0, 48, 40);      // L
            balls[1] = cleaned.getSubimage(51, 7, 32, 26);     // M
            balls[2] = cleaned.getSubimage(85, 13, 16, 14);     // S
            balls[3] = cleaned.getSubimage(105, 16, 8, 7);      // XS

        } catch (IOException e) {
            e.printStackTrace();
        }
        return balls;
    }
    //Şeffaflaştırma için: 
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

}



