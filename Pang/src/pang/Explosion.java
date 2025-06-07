package pang;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {
    public int x, y;
    public BufferedImage[] frames;
    public int frameIndex = 0;
    private int counter = 0;
    private int frameDelay = 6;
    public boolean finished = false;

    public Explosion(int x, int y, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
    }

    public void update() {
        if (finished) return;

        counter++;
        if (counter >= frameDelay) {
            frameIndex++;
            counter = 0;
            if (frameIndex >= frames.length) {
                finished = true;
            }
        }
    }

    public void draw(Graphics g, double scaleX, double scaleY) {
        if (finished || frameIndex >= frames.length) return;

        BufferedImage frame = frames[frameIndex];
        int drawX = (int)(x * scaleX);
        int drawY = (int)(y * scaleY);
        int w = (int)(frame.getWidth() * scaleX);
        int h = (int)(frame.getHeight() * scaleY);

        g.drawImage(frame, drawX, drawY, w, h, null);
    }
}
