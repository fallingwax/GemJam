package gemjam;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A class to set the SidePanel labels into an HBox
 */
public class PanelHBox extends HBox {

    Label title;

    /**
     * Constructor
     * @param title the name of the label
     */
    public PanelHBox(String title) {
        this.title = new Label(title);
        getChildren().add(this.title);
        setAlignment(Pos.BOTTOM_CENTER);
    }
}
