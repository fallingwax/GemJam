package gemjam;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Gem {

    private final static ArrayList<Color> colors = new ArrayList<Color>(){
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

    public boolean isDestroy() {
        return destroy;
    }

    public Gem(int x, int y, int size, int position, boolean christmas){
        this.size = size;
        this.x = x;
        this.y = y;
        this.position = position;
        this.initialPosition = position;
        Random random = new Random();
        //change this value to set the number of colors 1 - 6
        int rnd = random.nextInt(6);
        color = colors.get(rnd);
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
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
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

    public boolean getDestroy() {
        return destroy;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    private void setImageView(int id, int imageX, int imageY) {
        this.id = id;
        bufferedImage = Sprite.getSprite(imageX, imageY);
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.imageView = new ImageView(image);
    }

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
