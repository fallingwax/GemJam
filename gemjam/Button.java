package gemjam;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.concurrent.Callable;

public class Button {
    HBox hbox;
    Label title;

    public Button(String title, Callable<Void> buttonAction, int x, int y) {
        this.title = new Label(title);
        this.hbox = new HBox();
        this.title.getStyleClass().add("action-button-text");
        hbox.getStyleClass().add("action-button");
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setLayoutX(x);
        hbox.setLayoutY(y);
        hbox.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                try {
                    buttonAction.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        hbox.getChildren().add(this.title);
    }

}
