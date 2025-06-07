package pang;

import java.awt.image.BufferedImage;

public class Ball {

    public enum Size { L, M, S, XS }
    
    public int x, y;
    public int vx, vy;
    public Size size;
    public BufferedImage image;
    public int width, height;

    public Ball(int x, int y, int vx, int vy, Size size, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void move(int maxX, int maxY) {
        x += vx;
        y += vy;

        if (x < 0 || x + width > maxX) {
            vx *= -1;
            x = Math.max(0, Math.min(x, maxX - width));
        }

        if (y < 0 || y + height > maxY) {
            vy *= -1;
            y = Math.max(0, Math.min(y, maxY - height));
        }
    }

}
