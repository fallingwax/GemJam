package gemjam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprite {

    private static BufferedImage spriteSheet;
    private static final int TILE_SIZE = 125;

    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(Sprite.class.getResource("/res/gems2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    public static BufferedImage getSprite(int x, int y) {

        spriteSheet = loadSprite("gems2");
        return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

}