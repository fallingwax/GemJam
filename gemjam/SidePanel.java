package gemjam;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SidePanel extends GridPane {
    private PanelHBox scoreHBox;
    private PanelHBox gemsHBox;
    private PanelHBox levelHBox;
    private VBox gemView;

    private static Label currentScoreLabel;
    private static Label currentGemsLabel;
    private static Label currentLevelLabel;

    public SidePanel(boolean isThemed)  {
        scoreHBox = new PanelHBox("Score");
        gemsHBox = new PanelHBox("Gems");
        levelHBox = new PanelHBox("Level");
        gemView = new VBox();
        currentScoreLabel = new Label();
        currentGemsLabel = new Label();
        currentLevelLabel = new Label();
        gemView.setLayoutX(0);
        setHalignment(gemView, HPos.RIGHT);


        setHgap(10);

        if(isThemed) {
            scoreHBox.title.getStyleClass().add("christmas-label");
            gemsHBox.title.getStyleClass().add("christmas-label");
            levelHBox.title.getStyleClass().add("christmas-label");
            gemView.getStyleClass().add("christmas-next-piece");
        } else {
            scoreHBox.title.getStyleClass().add("label");
            gemsHBox.title.getStyleClass().add("label");
            levelHBox.title.getStyleClass().add("label");
            gemView.getStyleClass().add("next-piece");
        }

        currentScoreLabel.getStyleClass().add("changingLabel");
        currentLevelLabel.getStyleClass().add("changingLabel");
        currentGemsLabel.getStyleClass().add("changingLabel");
        setGemCount(0);
        setLevel(1);
        setScore(0);

        getStyleClass().add("stackpane");
        add(gemView, 0, 0);
        add(scoreHBox,0,2);
        add(gemsHBox,0,4);
        add(levelHBox,0,6);

        setHalignment(currentScoreLabel, HPos.RIGHT);
        add(currentScoreLabel,0,3);

        setHalignment(currentGemsLabel, HPos.RIGHT);
        add(currentGemsLabel,0,5);

        setHalignment(currentLevelLabel, HPos.RIGHT);
        add(currentLevelLabel,0,7);
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

    public Pane getGemView() {
        return gemView;
    }

    public void setGemView(VBox gemView) {
        this.gemView = gemView;
    }
}
