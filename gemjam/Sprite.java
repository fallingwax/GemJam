package gemjam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A class to split the sprite sheet into individual images
 */
public class Sprite {

    private static BufferedImage spriteSheet;

    // the size of the individual images on the sprite sheet
    private static final int TILE_SIZE = 125;

    /**
     * A method to load the sprite sheet
     * @param file the file name
     * @return BufferedImage of the sprite sheet
     */
    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(Sprite.class.getResource("/res/gems2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    /**
     * A method to select an image from the sprite sheet based on an X/Y coordinate
     * @param x the X position of the image
     * @param y the Y position of the image
     * @return BufferedImage the selected image
     */
    public static BufferedImage getSprite(int x, int y) {

        spriteSheet = loadSprite("gems2");
        //return the correct image based on the tile size and x/y coordinate
        return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

}