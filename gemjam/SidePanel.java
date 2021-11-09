package gemjam;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SidePanel extends GridPane {
    PanelHBox scoreHBox;
    PanelHBox gemsHBox;
    PanelHBox levelHBox;

    static Label currentScoreLabel;
    static Label currentGemsLabel;
    static Label currentLevelLabel;

    public SidePanel(boolean isThemed)  {
        scoreHBox = new PanelHBox("Score");
        gemsHBox = new PanelHBox("Gems");
        levelHBox = new PanelHBox("Level");
        currentScoreLabel = new Label();
        currentGemsLabel = new Label();
        currentLevelLabel = new Label();

        setHgap(10);

        if(isThemed) {
            scoreHBox.title.getStyleClass().add("christmas-label");
            gemsHBox.title.getStyleClass().add("christmas-label");
            levelHBox.title.getStyleClass().add("christmas-label");
        } else {
            scoreHBox.title.getStyleClass().add("label");
            gemsHBox.title.getStyleClass().add("label");
            levelHBox.title.getStyleClass().add("label");
        }

        currentScoreLabel.getStyleClass().add("changingLabel");
        currentLevelLabel.getStyleClass().add("changingLabel");
        currentGemsLabel.getStyleClass().add("changingLabel");
        setGemCount(0);
        setLevel(1);
        setScore(0);

        getStyleClass().add("stackpane");
        add(scoreHBox,0,0);
        add(gemsHBox,0,2);
        add(levelHBox,0,4);

        setHalignment(currentScoreLabel, HPos.RIGHT);
        add(currentScoreLabel,0,1);

        setHalignment(currentGemsLabel, HPos.RIGHT);
        add(currentGemsLabel,0,3);

        setHalignment(currentLevelLabel, HPos.RIGHT);
        add(currentLevelLabel,0,5);
    }

    public SidePanel() {
        getStyleClass().add("stackpane");
    }

    public static void setScore(int score) {
        currentScoreLabel.setText(" " + score);
    }

    public static void setLevel(int level) {
        currentLevelLabel.setText(" " + level);
    }

    public static void setGemCount(int gemCount) {
        currentGemsLabel.setText(" " + gemCount);
    }
}
