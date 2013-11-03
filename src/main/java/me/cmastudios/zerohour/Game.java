package me.cmastudios.zerohour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.*;

/**
 * A game using Slick2d
 */
public class Game extends BasicGame {

    /**
     * Screen width
     */
    private static final int WIDTH = 800;
    /**
     * Screen height
     */
    private static final int HEIGHT = 600;
    private int playerPosX;
    private int playerPosY;
    private Image playerImage;
    private Image alienImage;
    private Image background;
    private Image soccerBall;
    private Random rng;
    private List<Alien> aliens;
    private long lastFire;
    private Bullet ball;

    public Game() {
        super("Cosmic Soccer Battle");
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(Color.black);
        g.drawImage(background, 0, 0);
        g.drawImage(playerImage, playerPosX, playerPosY);
        for (Alien alien : aliens) {
            g.drawImage(alienImage, alien.x, alien.y);
        }
        if (ball != null) {
            g.drawImage(soccerBall, ball.x, ball.y);
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        playerPosX = 400;
        playerPosY = 300;
        playerImage = new Image(Game.class.getClassLoader().getResourceAsStream("player.png"), "player", false);
        background = new Image(Game.class.getClassLoader().getResourceAsStream("field.png"), "background", false);
        alienImage = new Image(Game.class.getClassLoader().getResourceAsStream("alien.png"), "alien", false);
        soccerBall = new Image(Game.class.getClassLoader().getResourceAsStream("ball.png"), "ball", false);
        container.setVSync(true);
        rng = new Random();
        aliens = new ArrayList<Alien>();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (container.getInput().isKeyDown(Input.KEY_A)) {
            playerPosX -= 2;
        } else if (container.getInput().isKeyDown(Input.KEY_D)) {
            playerPosX += 2;
        }
        if (container.getInput().isKeyDown(Input.KEY_W)) {
            playerPosY -= 2;
        } else if (container.getInput().isKeyDown(Input.KEY_S)) {
            playerPosY += 2;
        }
        if (container.getInput().isKeyPressed(Input.KEY_SPACE) && System.currentTimeMillis() - lastFire > 250) {
            lastFire = System.currentTimeMillis();
            fireBullet(container);
        }
        if (ball != null) {
            if (ball.direction == Direction.UP) {
                ball.y -= 5;
            } else if (ball.direction == Direction.DOWN) {
                ball.y += 5;
            } else if (ball.direction == Direction.LEFT) {
                ball.x -= 5;
            } else if (ball.direction == Direction.RIGHT) {
                ball.x += 5;
            }
            if (ball.x >= 800 || ball.x <= 0 || ball.y >= 600 || ball.y <= 0) {
                ball = null;
            } else {
                for (Iterator<Alien> it = aliens.iterator(); it.hasNext();) {
                    Alien a = it.next();
                    if (Math.abs(a.x - ball.x) < 25 && Math.abs(a.y - ball.y) < 25) {
                        it.remove();
                        System.out.println("Killing an alien - struck");
                        ball.x = -20;
                    }
                }
                soccerBall.rotate(50.0F);
                if (ball.x >= 669 && ball.x <= 722 && ball.y >= 172 && ball.y <= 340
                        && playerPosX > 600) {
                    aliens.clear();
                    System.out.println("Killing all aliens - scored");
                    ball = null;
                } else if (ball.x >= 90 && ball.x <= 137 && ball.y >= 180 && ball.y <= 337) {
                    for (int i = 0; i < 15; i++) {
                        this.spawnAlien();
                    }
                    ball = null;
                }
            }
        }
        for (Iterator<Alien> it = aliens.iterator(); it.hasNext();) {
            Alien a = it.next();
            if (Math.abs(a.x - playerPosX) < 25 && Math.abs(a.y - playerPosY) < 25) {
                playerPosX = 400;
                playerPosY = 300;
                it.remove();
                System.out.println("Killing an alien and teleporting to center - interference");
            }
        }
        if (rng.nextInt(60) == 30) {
            spawnAlien();
        }
    }

    private void spawnAlien() {
        Alien alien = new Alien();
        alien.x = Math.max(25, rng.nextInt(750));
        alien.y = Math.max(25, rng.nextInt(550));
        aliens.add(alien);
        System.out.println("Spawning alien");
    }

    private void fireBullet(GameContainer gc) {
        Bullet bullet = new Bullet();
        bullet.x = playerPosX;
        bullet.y = playerPosY;
        if (gc.getInput().isKeyDown(Input.KEY_W)) {
            bullet.direction = Direction.UP;
        } else if (gc.getInput().isKeyDown(Input.KEY_S)) {
            bullet.direction = Direction.DOWN;
        } else if (gc.getInput().isKeyDown(Input.KEY_A)) {
            bullet.direction = Direction.LEFT;
        } else if (gc.getInput().isKeyDown(Input.KEY_D)) {
            bullet.direction = Direction.RIGHT;
        } else {
            bullet.direction = Direction.LEFT;
        }
        ball = bullet;
    }

    private class Alien {

        public float x;
        public float y;
    }

    private class Bullet {

        public float x;
        public float y;
        public Direction direction;
    }

    private enum Direction {

        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setForceExit(false);
        app.start();
    }
}
