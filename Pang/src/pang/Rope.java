package pang;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rope {
    public int x, y;
    public boolean active = true;
    private int frameIndex = 0;
    private int animationCounter = 0;
    private int frameDelay = 3; // ipin frame oynama hızı

    public BufferedImage[] frames;

    public Rope(int x, int y, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
    }

    public void update() {
        if (!active) return;

        animationCounter++;
        if (animationCounter >= frameDelay) {
            frameIndex++;
            animationCounter = 0;

            if (frameIndex >= frames.length) {
                active = false; //animasyon bittiğinde ipi sahneden siler
            }
        }
    }

    public void draw(Graphics g, double scaleX, double scaleY) {
        if (!active || frameIndex >= frames.length) return;

        BufferedImage frame = frames[frameIndex];
        double scaleFactor = 1.5;

        int scaledWidth = (int)(frame.getWidth() * scaleX * scaleFactor);
        int scaledHeight = (int)(frame.getHeight() * scaleY * scaleFactor);

        int drawX = (int)(x * scaleX);

        //Alt kısmı zemine denk gelsin ama biraz yukarıdan başlasın 0.70 farkla başlıyor artarsa daha yukarda başlar.
        //0.85 karakter kafasından itibaren gibi
        int drawY = (int)(y * scaleY) - (int)(scaledHeight * 0.70);

        g.drawImage(frame, drawX, drawY, scaledWidth, scaledHeight, null);
    }


}
