package gemjam;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PanelHBox extends HBox {

    Label title;

    public PanelHBox(String title) {
        this.title = new Label(title);
        getChildren().add(this.title);
        setAlignment(Pos.BOTTOM_CENTER);
    }
}
