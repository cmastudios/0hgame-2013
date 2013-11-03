package me.cmastudios.zerohour;

import org.newdawn.slick.*;

/**
 * A game using Slick2d
 */
public class Game extends BasicGame {

    /** Screen width */
    private static final int WIDTH = 800;
    /** Screen height */
    private static final int HEIGHT = 600;
    
    private int playerPosX;
    private int playerPosY;
    private Image playerImage;
    private Image background;

    public Game() {
        super("Super Soccer Lineup");
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(Color.black);
        g.drawImage(background, 0, 0);
        g.drawImage(playerImage, playerPosX, playerPosY);

    }

    @Override
    public void init(GameContainer container) throws SlickException {
        playerPosX = 400;
        playerPosY = 300;
        playerImage = new Image(Game.class.getClassLoader().getResourceAsStream("player.png"), "player", false);
        background = new Image(Game.class.getClassLoader().getResourceAsStream("field.png"), "background", false);
        container.setVSync(true);
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
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setForceExit(false);
        app.start();
    }

}
