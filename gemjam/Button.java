package gemjam;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.concurrent.Callable;

public class Button {
    //hbox is a container to make a button
    HBox hbox;
    Label title;

    /**
     * Constructor
     * @param title the name of the button
     * @param buttonAction the on click method call
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Button(String title, Callable<Void> buttonAction, int x, int y) {
        this.title = new Label(title);
        this.hbox = new HBox();
        this.title.getStyleClass().add("action-button-text");
        hbox.getStyleClass().add("action-button");
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setLayoutX(x);
        hbox.setLayoutY(y);
        hbox.setOnMouseClicked(mouseEvent -> {
            try {
                buttonAction.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        hbox.getChildren().add(this.title);
    }

}
