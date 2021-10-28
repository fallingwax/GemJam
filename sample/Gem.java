package sample;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Gem {

    final static ArrayList<Color> colors = new ArrayList<Color>(){
        {
            add(Color.RED);
            add(Color.BLUE);
            add(Color.ORANGE);
            add(Color.GREEN);
            add(Color.PURPLE);
            add(Color.YELLOW);
        }
    };
    int size;
    int x;
    int y;
    Rectangle shape;
    Color color;
    int id;
    int position;
    int initialPosition;
    boolean destroy = false;
    BufferedImage bufferedImage;
    Image image;
    ImageView imageView;



    public Gem(int x, int y, int size, int position, boolean christmas){
        this.size = size;
        this.x = x;
        this.y = y;
        this.position = position;
        this.initialPosition = position;
        Random random = new Random();
        int rnd = random.nextInt(6);
        color = colors.get(rnd);
        if (!christmas) {
            if (color == Color.RED) {
                id = 1;
                bufferedImage = Sprite.getSprite(2, 0);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            } else if (color == Color.BLUE) {
                id = 2;
                bufferedImage = Sprite.getSprite(1, 0);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            } else if (color == Color.ORANGE) {
                id = 3;
                bufferedImage = Sprite.getSprite(0, 0);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            } else if (color == Color.GREEN) {
                id = 4;
                bufferedImage = Sprite.getSprite(3, 0);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            } else if (color == Color.PURPLE) {
                id = 5;
                bufferedImage = Sprite.getSprite(0, 1);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            } else {
                id = 6;
                bufferedImage = Sprite.getSprite(3, 1);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView = new ImageView(image);
            }
        } else {

            if (color == Color.RED) {
                id = 1;
                imageView = new ImageView(new Image("/res/ChristmasBalls/01.png"));
            } else if (color == Color.BLUE) {
                id = 2;
                imageView = new ImageView(new Image("/res/ChristmasBalls/07.png"));
            } else if (color == Color.ORANGE ) {
                id = 3;
                imageView = new ImageView(new Image("/res/ChristmasBalls/02.png"));
            } else if (color == Color.GREEN) {
                id = 4;
                imageView = new ImageView(new Image("/res/ChristmasBalls/08.png"));
            } else if (color == Color.PURPLE) {
                id = 5;
                imageView = new ImageView(new Image("/res/ChristmasBalls/14.png"));
            } else {
                id =6;
                imageView = new ImageView(new Image("/res/ChristmasBalls/04.png"));
            }
        }
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setX(x);
        imageView.setLayoutX(x + size*2);
        imageView.setY(y);
    }

    //default grid Gems
    public Gem(int x, int y, int size) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.id = 0;
        this.position = 3;
        this.color = Color.BLACK;
        this.initialPosition = 0;
    }

    public int getY() {
        return this.y;
    }

    public int getColorId() {
        return this.id;
    }
    public int getInitialPosition() { return this.initialPosition; }

    public int getX() {
        return this.x;
    }

    public void setPosition(int index) {
        this.position = index;
    }

    public int getPosition() {
        return position;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDestory() {
        this.destroy = true;
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setSize(int size) {
        this.size = size;
    }


}
