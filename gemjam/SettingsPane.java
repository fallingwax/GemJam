package gemjam;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class for the settings screen
 */
public class SettingsPane {
    GridPane gridPane;

    //Using to map to load elements into the GridPane
    Map<Label, ImageView> map = new LinkedHashMap<>();

    CheckBox disable_sound = new CheckBox();
    CheckBox disable_fx = new CheckBox();
    CheckBox set_christmas = new CheckBox();

    //Keyboard key images
    ImageView arrow_down = new ImageView(new Image("/res/keys/arrow down.png"));
    ImageView arrow_right = new ImageView(new Image("/res/keys/arrow right.png"));
    ImageView arrow_up =  new ImageView(new Image("/res/keys/arrow up.png"));
    ImageView space =  new ImageView(new Image("/res/keys/space.png"));
    ImageView ctrl =  new ImageView(new Image("/res/keys/ctrl.png"));
    ImageView enter =  new ImageView(new Image("/res/keys/enter.png"));
    ImageView esc =  new ImageView(new Image("/res/keys/esc.png"));
    ImageView letter_a =  new ImageView(new Image("/res/keys/letter a.png"));
    ImageView letter_d =  new ImageView(new Image("/res/keys/letter d.png"));
//    ImageView letter_s =  new ImageView(new Image("/res/keys/letter s.png"));
    ImageView letter_w = new ImageView(new Image("/res/keys/letter w.png"));

    Label downLabel = new Label("Enter Initials - Change letter Down");
    Label upLabel = new Label("Enter Initials - Change letter Up");
    Label rightLabel = new Label("Enter Initials - Move forward");
    Label spaceLabel = new Label("Sort gems");
    Label ctrlLabel = new Label("Pause game");
    Label enterLabel = new Label("Enter Initials - Submit");
    Label escLabel = new Label("Reset game");
    Label aLabel = new Label("Move gems left");
    Label dLabel = new Label("Move gems right");
//    Label sLabel = new Label("Move gems down");
    Label wLabel = new Label("Drop gems");
    Label soundLabel = new Label("Disable music");
    Label fxLabel = new Label("Disable sound effects");
    Label christmasLabel = new Label("Christmas Theme");
    Label settingsLabel = new Label("Settings");

    //int to hold the last row of the gridpane
    int lastRow;

    /**
     * Constructor
     */
    public SettingsPane() {

        setMap();
        gridPane = new GridPane();
        gridPane.getStyleClass().add("settings");
        gridPane.add(settingsLabel, 0, 0,2,1 );
        int row = 1;
        gridPane.setVgap(15);
        gridPane.setHgap(10);

        for (Map.Entry<Label, ImageView> entry : map.entrySet()) {
            ImageView iv = entry.getValue();
            Label label = entry.getKey();
            iv.setFitHeight(50);
            iv.setFitWidth(50);
            iv.getStyleClass().add("imageview");
            label.getStyleClass().add("settings-label");
            label.setAlignment(Pos.BASELINE_LEFT);
            gridPane.add(iv,0,row);
            gridPane.add(label, 1, row);
            row++;
            setLastRow(row + 1);
        }

        soundLabel.getStyleClass().add("settings-label");
        fxLabel.getStyleClass().add("settings-label");
        christmasLabel.getStyleClass().add("settings-label");
        disable_sound.getStyleClass().add("check-box");
        disable_fx.getStyleClass().add("check-box");
        gridPane.add(disable_sound, 0, lastRow);
        gridPane.add(soundLabel,1, lastRow);
        gridPane.add(disable_fx, 0,lastRow + 1);
        gridPane.add(fxLabel,1,lastRow + 1);
        gridPane.add(set_christmas, 0,lastRow + 2);
        gridPane.add(christmasLabel,1,lastRow + 2);

    }

    /**
     * A method to map the elements of the settings screen
     */
    public void setMap() {
        map.put(downLabel,arrow_down);
        map.put(upLabel,arrow_up);
        map.put(rightLabel,arrow_right);
        map.put(spaceLabel,space);
        map.put(ctrlLabel,ctrl);
        map.put(enterLabel,enter);
        map.put(escLabel,esc);
        map.put(aLabel,letter_a);
        map.put(dLabel,letter_d);
//        map.put(sLabel,letter_s);
        map.put(wLabel,letter_w);
    }

    /**
     * A method to set the last row
     * @param row set the last row of the GridPane
     */
    private void setLastRow(int row) {
        this.lastRow = row;
    }

    /**
     * A method to return the gridpane
     * @return returning the GripPane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * A method to get the value of the disable sound checkbox
     * @return the value of isSelected
     */
    public boolean getDisableMusic() {
         return disable_sound.isSelected();
    }

    /**
     * A method to get the value of the disable fx checkbox
     * @return the value of isSelected
     */
    public boolean getDisableFX() {
        return disable_fx.isSelected();
    }

    /**
     * A method to get the value of the christmas theme checkbox
     * @return the value of isSelected
     */
    public boolean getChristmasTheme() {
        return set_christmas.isSelected();
    }
}
