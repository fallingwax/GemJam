package gemjam;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.concurrent.Callable;

public class GameOverPane {
    HighScorePane highScorePane;
    GridPane highScoreGridPane;
    Pane gameOverScreen;
    HBox gameOverHBox;
    Label gameOverLabel;
    Label enterInitials;
    int gemSize;
    int width;
    int height;
    int currentScore;
    Button restartBtn;

    /**
     * Constructor
     * @param gemSize size of the game piece
     * @param width width of the game board
     * @param height height of the game board
     * @param hsp high scores pane
     * @param score the current score
     * @param callable a method call for the button function
     */
    public GameOverPane(int gemSize, int width, int height, HighScorePane hsp, int score, Callable<Void> callable) {
        gameOverScreen = new Pane();
        gameOverHBox = new HBox();
        gameOverLabel = new Label("GAME OVER");
        this.highScorePane = hsp;
        this.gemSize = gemSize;
        this.width = width;
        this.height = height;
        this.currentScore = score;
        setGameOverLabel();
        setGameOverScreen();
        restartBtn = new Button("Restart", () -> {
            callable.call();
            return null;
        }, gemSize * 2 + 10, gemSize * 7);
    }

    /**
     * A method to set the size and class of the game over label
     */
    private void setGameOverLabel() {
        gameOverLabel.getStyleClass().add("heading");
        gameOverLabel.setLayoutX(gemSize * 2);
        gameOverLabel.setLayoutY(gemSize * 4);
        gameOverLabel.setWrapText(true);
    }

    /**
     * A method to set the size and class of the game over pane
     */
    private void setGameOverScreen() {
        gameOverScreen.getStyleClass().add("game-over");
        gameOverScreen.setMinSize(width * gemSize, height * gemSize);
    }

    /**
     * A method to set the size and class of the enter initials label
     */
    private void setEnterInitials() {
        enterInitials = new Label("ENTER INITITALS");
        enterInitials.getStyleClass().add("initials-heading");
        enterInitials.setLayoutX(gemSize * 2);
        enterInitials.setLayoutY(gemSize * 9);
        enterInitials.setWrapText(true);
    }

    /**
     * A method to set the size of the high scores GridPane
     */
    private void setHighScoreGridPane() {
        this.highScoreGridPane = highScorePane.newHighScore();
        highScoreGridPane.setMaxWidth(240);
        highScoreGridPane.setLayoutX(gemSize * 2 - 35);
        highScoreGridPane.setLayoutY(gemSize * 10);
    }

    /**
     * A method to get the standard game over screen
     * @return gameOverScreen
     */
    public Pane getGameOverScreen() {
        gameOverScreen.getChildren().addAll(gameOverLabel, restartBtn.hbox);
        return gameOverScreen;
    }

    /**
     * A method to get the game over screen with the high score option
     * @return gameOverScreen
     */
    public Pane getHighScoreGameOverScreen() {
        setEnterInitials();
        setHighScoreGridPane();
        gameOverScreen.getChildren().addAll(gameOverLabel, restartBtn.hbox, enterInitials, highScoreGridPane);
        return gameOverScreen;
    }

}
