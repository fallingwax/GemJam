package gemjam;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to create a single game piece.
 */
public class Gem {

    //List of available colors
    private final static ArrayList<Color> colors = new ArrayList<>() {
        {
            add(Color.RED);
            add(Color.BLUE);
            add(Color.ORANGE);
            add(Color.GREEN);
            add(Color.PURPLE);
            add(Color.YELLOW);
        }
    };
    private int size;
    private int x;
    private int y;
    private Color color;
    private int id;
    private int position;
    private int initialPosition;
    private boolean destroy = false;
    private BufferedImage bufferedImage;
    private Image image;
    public ImageView imageView;

    /**
     * Main Constructor
     * @param x the starting X position
     * @param y the starting Y position
     * @param size the game piece size
     * @param position the starting position
     * @param christmas a flag for the christmas theme
     */
    public Gem(int x, int y, int size, int position, boolean christmas){
        this.size = size;
        this.x = x;
        this.y = y;
        this.position = position;
        this.initialPosition = position;

        //get a random valye for the color of the Gem
        Random random = new Random();
        //change this value to set the number of colors 1 - 6
        int rnd = random.nextInt(6);
        color = colors.get(rnd);

        //If were are not themed that get the normal gem images
        if (!christmas) {
            if (color == Color.RED) {
                setImageView(1,2, 0);
            } else if (color == Color.BLUE) {
                setImageView(2,3, 1);
            } else if (color == Color.ORANGE) {
                setImageView(3,0, 1);
            } else if (color == Color.GREEN) {
                setImageView(4,1, 0);
            } else if (color == Color.PURPLE) {
                setImageView(5,0, 0);
            } else {
                setImageView(6,3, 0);
            }
        } else {

            //if we are themed use the christmas balls
            if (color == Color.RED) {
                id = 1;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/01.png"));
            } else if (color == Color.BLUE) {
                id = 2;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/07.png"));
            } else if (color == Color.ORANGE ) {
                id = 3;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/02.png"));
            } else if (color == Color.GREEN) {
                id = 4;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/08.png"));
            } else if (color == Color.PURPLE) {
                id = 5;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/14.png"));
            } else {
                id =6;
                this.imageView = new ImageView(new Image("/res/ChristmasBalls/04.png"));
            }
        }

        //set the parameters for the imageview
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setLayoutX(x + size*2);
        imageView.setY(y);
    }

    /**
     * Constructor for the default grid Gems
     */
    public Gem(int x, int y, int size) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.id = 0;
        this.position = 3;
        this.color = Color.BLACK;
        this.initialPosition = 0;
    }

    /**
     * A method to return the Y value
     * @return current Y value
     */
    public int getY() {
        return this.y;
    }

    /**
     * A method to the get the color ID
     * @return the current color ID
     */
    public int getColorId() {
        return this.id;
    }

    /**
     * A method to get the starting position
     * @return the initial position
     */
    public int getInitialPosition() { return this.initialPosition; }

    /**
     * A method to return the X value
     * @return current X value
     */
    public int getX() {
        return this.x;
    }

    /**
     * A method to the set the current position
     * @param index value 0 -2
     */
    public void setPosition(int index) {
        this.position = index;
    }

    /**
     * A method to get the current position
     * @return position
     */
    public int getPosition() {
        return position;
    }

    /**
     * A method to set the X position
     * @param x the X value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * A method to set the Y position
     * @param y the y value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * A method to set the destroy flag
     */
    public void setDestory() {
        this.destroy = true;
    }

    /**
     * A method to check the destroy flag
     * @return boolean
     */
    public boolean isDestroy() {
        return destroy;
    }

    /**
     * A method to set the ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * A method to set the size
     * @param size the game piece size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * A method to get the destroy flag
     * @return boolean
     */
    public boolean getDestroy() {
        return destroy;
    }

    /**
     * A method to return the imageview of the game piece
     * @return imageview
     */
    public ImageView getImageView() {
        return this.imageView;
    }

    /**
     * A method to set the ImageView with current image from the sprite sheet
     * @param id the id
     * @param imageX the X position of the image from the returned grid of the sprite sheet
     * @param imageY the Y position of the image from the returned grid of the sprite sheet
     */
    private void setImageView(int id, int imageX, int imageY) {
        this.id = id;
        bufferedImage = Sprite.getSprite(imageX, imageY);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.imageView = new ImageView(image);
    }

    /**
     * Overriding the toString for troubleshooting
     * @return String
     */
    @Override
    public String toString() {
        return "Gem{" +
                "size=" + size +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", id=" + id +
                ", position=" + position +
                ", initialPosition=" + initialPosition +
                ", destroy=" + destroy +
                ", bufferedImage=" + bufferedImage +
                ", image=" + image +
                ", imageView=" + imageView +
                '}';
    }
}
