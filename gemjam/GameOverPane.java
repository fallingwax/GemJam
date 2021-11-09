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
        restartBtn = new Button("Restart", new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                callable.call();
                return null;
            }
        }, gemSize * 2 + 10, gemSize * 7);

    }

    private void setGameOverLabel() {
        gameOverLabel.getStyleClass().add("heading");
        gameOverLabel.setLayoutX(gemSize * 2);
        gameOverLabel.setLayoutY(gemSize * 4);
        gameOverLabel.setWrapText(true);
    }

    private void setGameOverScreen() {
        gameOverScreen.getStyleClass().add("game-over");
        gameOverScreen.setMinSize(width * gemSize, height * gemSize);
    }

    private void setEnterInitials() {
        enterInitials = new Label("ENTER INITITALS");
        enterInitials.getStyleClass().add("initials-heading");
        enterInitials.setLayoutX(gemSize * 2);
        enterInitials.setLayoutY(gemSize * 9);
        enterInitials.setWrapText(true);
    }


    private void setHighScoreGridPane() {
        this.highScoreGridPane = highScorePane.newHighScore();
        highScoreGridPane.setMaxWidth(240);
        highScoreGridPane.setLayoutX(gemSize * 2 - 35);
        highScoreGridPane.setLayoutY(gemSize * 10);
    }

    public Pane getGameOverScreen() {
        gameOverScreen.getChildren().addAll(gameOverLabel, restartBtn.hbox);
        return gameOverScreen;
    }

    public Pane getHighScoreGameOverScreen() {
        setEnterInitials();
        setHighScoreGridPane();
        gameOverScreen.getChildren().addAll(gameOverLabel, restartBtn.hbox, enterInitials, highScoreGridPane);
        return gameOverScreen;
    }

}
