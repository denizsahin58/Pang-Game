package pang;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private BufferedImage bg1, bg2, bg3, foreground;
    private BufferedImage[] playerRightFrames;
    private BufferedImage[] playerLeftFrames;
    private int currentBackground = 0;
    
    private BufferedImage[] ballFrames;
    private List<Ball> activeBalls = new ArrayList<>();
     
    private Set<Ball> ballsThatHit = new HashSet<>();
    private int playerHealth = 10;
    
    private List<Rope> activeRopes = new ArrayList<>();
    private BufferedImage[] ropeFrames;  
    private List<Explosion> activeExplosions = new ArrayList<>();
    private BufferedImage[] explosionFrames;
    private BufferedImage[] smallExplosionFrames;
    private BufferedImage[] xsExplosionFrames;
    // foreground çözünürlüğüne göre konum
    private int playerX = 150;
    private int playerY = 150;
    private int playerFrameIndex = 0;
    private boolean facingRight = true;
    private boolean moving = false;

    private boolean gameRunning = false;
    private long gameStartTime;

    private final Set<Integer> pressedKeys = new HashSet<>();

    private Timer backgroundTimer;
    private Timer animationTimer;
    private Timer movementTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        try {
            bg1 = ImageIO.read(new File("assets/bg_day.png"));
            bg2 = ImageIO.read(new File("assets/bg_evening.png"));
            bg3 = ImageIO.read(new File("assets/bg_night.png"));
            foreground = ImageIO.read(new File("assets/foreground1.png"));
            
            ballFrames = BallLoader.loadBallFrames("assets/balloons_red.png");
            activeBalls.add(new Ball(100, 50, 1, 1, Ball.Size.L, ballFrames[0]));
            explosionFrames = ExplosionLoader.loadExplosionFrames("assets/balloon_bang.png",4);

            ropeFrames = RopeLoader.loadRopeFrames("assets/rope.png");
            
            playerRightFrames = PlayerSpriteLoader.loadPlayerFrames("assets/player_right.png", 5, false);
            playerLeftFrames = PlayerSpriteLoader.loadPlayerFrames("assets/player_right.png", 5, true);
            
            smallExplosionFrames = ExplosionLoader.loadExplosionFrames("assets/small_balloon_bang.png", 4);
            xsExplosionFrames = ExplosionLoader.loadExplosionFrames("assets/xs_balloon_bang.png", 3);

        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundTimer = new Timer(30000, e -> {
            currentBackground++;
            repaint();
        });
        //frame hızını ayarlıyor. 80ms hızla frameler kayıyor
        animationTimer = new Timer(80, e -> {
            if (moving) {
                playerFrameIndex = (playerFrameIndex + 1) % 5;
                repaint();
            }
        });

        movementTimer = new Timer(16, e -> {
        	playerY = getZeminYFromForeground(playerX);

        	
            moving = false;
            if (pressedKeys.contains(KeyEvent.VK_D)) {
                playerX += 8; //tuşlarda oyuncunun hızını ayarlıyoruz. 
                facingRight = true;
                moving = true;
            }
            if (pressedKeys.contains(KeyEvent.VK_A)) {
                playerX -= 8;
                facingRight = false;
                moving = true;
            }
            repaint();
        });

        animationTimer.start();
        movementTimer.start();

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                
                if (e.getKeyCode() == KeyEvent.VK_E) {
                	int ropeX = playerX + playerRightFrames[0].getWidth() / 2 - 4;
                	int ropeY = getZeminYFromForeground(playerX); // karakterin zemin yüksekliği

                    //rope oluşturur
                    Rope rope = new Rope(ropeX, ropeY, ropeFrames);
                    activeRopes.add(rope);
                    // rope için konum bilgisi verir
                    System.out.println("Rope created at X=" + ropeX + ", Y=" + ropeY);
                    System.out.println("Total ropes: " + activeRopes.size());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A) {
                    moving = false;
                    playerFrameIndex = 0;
                    repaint();
                }
            }
        });
    }

    public void startGame() {
        gameRunning = true;
        currentBackground = 0;
        gameStartTime = System.currentTimeMillis();
        playerHealth = 10;
        activeBalls.clear();
        ballsThatHit.clear();

        //rastgele yeni L balon ekle
        if (ballFrames != null) {
            Ball b = new Ball(100, 50, 2, 2, Ball.Size.L, ballFrames[0]);
            activeBalls.add(b);
        }

        backgroundTimer.restart();
        repaint();
    }

    private int getZeminYFromForeground(int gameX) {
        if (foreground == null) return playerY;

        int fx = Math.max(0, Math.min(gameX, foreground.getWidth() - 1));

        for (int fy = foreground.getHeight() - 1; fy >= 0; fy--) {
            int alpha = (foreground.getRGB(fx, fy) >> 24) & 0xff;
            if (alpha > 10) {
                //en alttaki zemini buldup, oyuncunun yüksekliğini çıkarır
                int adjustedY = fy - playerLeftFrames[0].getHeight();
                return Math.max(0, adjustedY);
            }
        }

        return playerY; // hiçbir zemin bulunamadıysa aynı kalsın
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameRunning) {
            //arka plan değişimi
            BufferedImage bg = switch (currentBackground) {
                case 0 -> bg1;
                case 1 -> bg2;
                case 2 -> bg3;
                default -> bg3;
            };
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

            //foreground boyutuna göre ölçek
            double scaleX = (double) getWidth() / foreground.getWidth();
            double scaleY = (double) getHeight() / foreground.getHeight();

            //sayaç
            int elapsedSec = (int) ((System.currentTimeMillis() - gameStartTime) / 1000);
            int remaining = Math.max(90 - elapsedSec, 0);
            

            if (remaining <= 0) {
                backgroundTimer.stop();
                gameRunning = false;
                g.drawString("Game Over!", getWidth() / 2 - 60, getHeight() / 2);
            }

            //foreground orta layer
            g.drawImage(foreground, 0, 0, getWidth(), getHeight(), null);

            //oyuncu çizimi (en üst layer)
            BufferedImage currentFrame = facingRight
                    ? playerRightFrames[playerFrameIndex]
                    : playerLeftFrames[playerFrameIndex];

            int drawX = (int) (playerX * scaleX);
            int drawY = (int) (playerY * scaleY);
            
            // 1 yerine 3/2 gibi sayılar yazınca karakteri büyütüp küçültebiliriz.
            int width = (int) (currentFrame.getWidth() * scaleX * 1);
            int height = (int) (currentFrame.getHeight() * scaleY * 1);

            drawX = Math.max(0, Math.min(getWidth() - width, drawX));

            g.drawImage(currentFrame, drawX, drawY, width, height, null);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Time: " + remaining + "s", getWidth() - 120, 30);//-120, 30 ekran konumu sağ üst
            
            Rectangle playerBox = new Rectangle(drawX, drawY, width, height);
             
            for (Ball b : activeBalls) {
                b.move(foreground.getWidth(), foreground.getHeight());

                // BALON GÖRÜNTÜ BOYUTU OYUNA GÖRE AYARLANIR
                int ballX = (int)(b.x * scaleX);
                int ballY = (int)(b.y * scaleY);
                int ballW = (int)(b.image.getWidth() * scaleX * 1); // balonu 1x büyütüyoruz arttırabiliriz.
                int ballH = (int)(b.image.getHeight() * scaleY * 1);

                g.drawImage(b.image, ballX, ballY, ballW, ballH, null);

                Rectangle ballBox = new Rectangle(ballX, ballY, ballW, ballH);
                boolean isIntersecting = playerBox.intersects(ballBox);

                if (isIntersecting && !ballsThatHit.contains(b)) {
                    // İlk kez balonla çarpışıyor
                	switch (b.size) {
                    case L -> playerHealth -= 4;
                    case M -> playerHealth -= 3;
                    case S -> playerHealth -= 3;
                    case XS -> playerHealth -= 1;
                	}
                	ballsThatHit.add(b); // balon hasarını işaretle
                } 
                else if (!isIntersecting) {
                    ballsThatHit.remove(b); // balon oyuncudan ayrıldıysa tekrar hasar alabilir hale getirir
                }              
                
            }
            for (Rope r : activeRopes) {
                r.update();
                r.draw(g, scaleX, scaleY);
            }
            activeRopes.removeIf(r -> !r.active);

            //can göstergesi
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25)); //25 font büyüklüğü
            g.drawString("Health: " + playerHealth, 20, 30); //20, 30 ekran konumu sol üst
            
            for (Explosion ex : activeExplosions) {
                ex.update();
                ex.draw(g, scaleX, scaleY);
            }
            activeExplosions.removeIf(ex -> ex.finished);
            List<Ball> ballsToAdd = new ArrayList<>();
            List<Ball> ballsToRemove = new ArrayList<>();
            List<Rope> ropesToRemove = new ArrayList<>();

            for (Rope r : activeRopes) {
            	int ropeW = 12;
            	int ropeH = (int)(80 * scaleY); //ekranla birlikte oranla rope'u
            	Rectangle ropeRect = new Rectangle((int)(r.x * scaleX - ropeW / 2), (int)((r.y * scaleY - ropeH)), ropeW, ropeH);

                for (Ball b : activeBalls) {
                    Rectangle ballRect = new Rectangle(
                            (int)(b.x * scaleX),
                            (int)(b.y * scaleY),
                            (int)(b.image.getWidth() * scaleX),
                            (int)(b.image.getHeight() * scaleY)
                    );

                    if (ropeRect.intersects(ballRect)) {
                        //balona göre doğru efekt seçimi
                        BufferedImage[] chosenExplosionFrames;
                        if (b.size == Ball.Size.S) {
                            chosenExplosionFrames = smallExplosionFrames;
                        } else if (b.size == Ball.Size.XS) {
                            chosenExplosionFrames = xsExplosionFrames;
                        } else {
                            chosenExplosionFrames = explosionFrames;
                        }

                        Explosion explosion = new Explosion(b.x, b.y, chosenExplosionFrames);
                        activeExplosions.add(explosion);

                        //balon yok edilecek
                        ballsToRemove.add(b);

                        //yeni balonları ekle
                        if (b.size == Ball.Size.L) {
                            ballsToAdd.add(new Ball(b.x, b.y, 1, -1, Ball.Size.M, ballFrames[1]));
                            ballsToAdd.add(new Ball(b.x, b.y, -1, -1, Ball.Size.M, ballFrames[1]));
                        } else if (b.size == Ball.Size.M) {
                            ballsToAdd.add(new Ball(b.x, b.y, 1, -1, Ball.Size.S, ballFrames[2]));
                            ballsToAdd.add(new Ball(b.x, b.y, -1, -1, Ball.Size.S, ballFrames[2]));
                        } else if (b.size == Ball.Size.S) {
                            ballsToAdd.add(new Ball(b.x, b.y, 1, -1, Ball.Size.XS, ballFrames[3]));
                            ballsToAdd.add(new Ball(b.x, b.y, -1, -1, Ball.Size.XS, ballFrames[3]));
                        }
                        //XS balon bölünmüyor, sadece efekt gösterir

                        //4. Rope -> sil
                        ropesToRemove.add(r);
                        break; // aynı rope, aynı anda sadece 1 balona çarpabilir
                    }

                }
            }

            activeBalls.removeAll(ballsToRemove);
            activeBalls.addAll(ballsToAdd);
            activeRopes.removeAll(ropesToRemove);


            //Listenin dışında silme ve ekleme yap
            activeBalls.removeAll(ballsToRemove);
            activeBalls.addAll(ballsToAdd);
            activeRopes.removeAll(ropesToRemove);


            if (remaining <= 0 || playerHealth <= 0) {
                backgroundTimer.stop();
                gameRunning = false;
                g.drawString("Game Over!", getWidth() / 2 - 60, getHeight() / 2);
            }


        } else {
            g.setColor(Color.WHITE);
            g.drawString("Click 'Game > New' to start the game.", 280, 300);
        }
    }
}
