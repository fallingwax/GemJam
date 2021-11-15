package gemjam;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A class to create the SidePanel from the game. Extends GridPane to make setting easier
 */
public class SidePanel extends GridPane {

    // the labels
    private PanelHBox scoreHBox;
    private PanelHBox gemsHBox;
    private PanelHBox levelHBox;
    private VBox gemView;

    // labels to hold the values
    private static Label currentScoreLabel;
    private static Label currentGemsLabel;
    private static Label currentLevelLabel;

    /**
     * Constructor
     * @param isThemed checking for the theme setting
     */
    public SidePanel(boolean isThemed)  {
        scoreHBox = new PanelHBox("Score");
        gemsHBox = new PanelHBox("Gems");
        levelHBox = new PanelHBox("Level");

        // a vbox to view the next game piece
        gemView = new VBox();
        currentScoreLabel = new Label();
        currentGemsLabel = new Label();
        currentLevelLabel = new Label();
        gemView.setLayoutX(0);

        setHalignment(gemView, HPos.RIGHT);
        setHgap(10);

        // set the correct styles depending on the theme check box
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

        // set the default values
        setGemCount(0);
        setLevel(1);
        setScore(0);

        // add the elements to the Grid
        getStyleClass().add("stackpane");
        add(gemView, 0, 0);
        add(scoreHBox,0,2);
        add(gemsHBox,0,4);
        add(levelHBox,0,6);

        // position the elements
        setHalignment(currentScoreLabel, HPos.RIGHT);
        add(currentScoreLabel,0,3);

        setHalignment(currentGemsLabel, HPos.RIGHT);
        add(currentGemsLabel,0,5);

        setHalignment(currentLevelLabel, HPos.RIGHT);
        add(currentLevelLabel,0,7);
    }

    /**
     * A default constructor for a simple black SidePanel
     */
    public SidePanel() {
        getStyleClass().add("stackpane");
    }

    /**
     * A method the set the current score value
     * @param score the current score
     */
    public static void setScore(int score) {
        currentScoreLabel.setText(" " + score);
    }

    /**
     * A method the set the current level value
     * @param level the current level
     */
    public static void setLevel(int level) {
        currentLevelLabel.setText(" " + level);
    }

    /**
     * A method the set the current gem count
     * @param gemCount the current gemCount
     */
    public static void setGemCount(int gemCount) {
        currentGemsLabel.setText(" " + gemCount);
    }

    /**
     * A method to return the gemView
     * @return Pane
     */
    public Pane getGemView() {
        return gemView;
    }

    /**
     * A method to set the gemView
     * @param gemView the current gem view
     */
    public void setGemView(VBox gemView) {
        this.gemView = gemView;
    }
}
