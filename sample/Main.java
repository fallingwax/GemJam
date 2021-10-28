package sample;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;
import java.util.List;

public class Main extends Application {

    AudioClip swap = new AudioClip(getClass().getResource("/sounds/swoosh.wav").toExternalForm());
    AudioClip drop = new AudioClip(getClass().getResource("/sounds/drop.wav").toExternalForm());
    AudioClip score = new AudioClip(getClass().getResource("/sounds/score.wav").toExternalForm());
    Media level1 = new Media(getClass().getResource("/sounds/track1.mp3").toExternalForm());
    //https://rushcoil.bandcamp.com/album/xmas-compilation-2010
    Media christmas = new Media(getClass().getResource("/sounds/RushJet1 - XMAS Compilation 2010 - 13 O Holy Night.mp3").toExternalForm());
    Media track;
    MediaPlayer player;


    final static int BOARD_WIDTH = 6;
    final static int BOARD_HEIGHT = 13;
    final static int GEM_SIZE = 80;

    BorderPane root;
    Pane pane;

    GridPane sidePanel;
    HighScorePane highScorePane = new HighScorePane();
    SettingsPane settingsPane = new SettingsPane();

    HBox scoreHBox;
    VBox gemsVBox;
    VBox levelVBox;
    Pane gameOverScreen;

    Label currentScoreLabel;
    Label scoreLabel;
    Label gemsLabel;
    Label currentGemsLabel;
    Label levelLabel;
    Label currentLevelLabel;
    Label gameOverLabel;

    HBox gameOverHBox;

    int currentScore;
    int gemCount;
    int currentLevel;

    Gem[][] grid = new Gem[BOARD_WIDTH][BOARD_HEIGHT];
    Gem gem1;
    Gem gem2;
    Gem gem3;
    boolean isFalling = false;
    boolean isKeyPressed = false;
    boolean game = true;
    boolean paused = false;
    boolean destroyComplete;
    boolean isChristmasTheme = false;
    int matchedThreeMultiplier = 1;
    long timestamp;
    long prevTimestamp;
    int columnCreated = 0;
    List<Gem> gems = new ArrayList<>();
    List<Gem> currentGems = new ArrayList<Gem>();
    List<Gem> matches = new ArrayList<>();
    TranslateTransition tt;
    FadeTransition fd;

    //Game Loop
    GameTimer gameTimer = new GameTimer(800) {
        @Override
        public void game() {
            if (game) {
                player.play();
                moveDown();

            } else {

                gameOver();

            }
        }
    };



    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        Scene scene = new Scene(root, BOARD_WIDTH * GEM_SIZE + 200, BOARD_HEIGHT * GEM_SIZE);
        scene.getStylesheets().add("css/stylesheet.css");
        primaryStage.setTitle("Gem Jam");
        primaryStage.setResizable(false);
        sidePanel = new GridPane();
        sidePanel.getStyleClass().add("stackpane");
        sidePanel.setHgap(10);
        sidePanel.setGridLinesVisible(false);
        root.setCenter(sidePanel);
//        gameOver();
        startScreen();
        scene.setOnKeyPressed((e) -> {
            if (e.getCode().equals(KeyCode.S)) {
                moveDown();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (e.getCode().equals(KeyCode.SPACE)) {
                if (!settingsPane.getDisableFX()) {
                    swap.play(1);
                }
                sortGems();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (e.getCode().equals(KeyCode.A)) {
                moveLeft();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (e.getCode().equals(KeyCode.D)) {
                moveRight();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (e.getCode().equals(KeyCode.W)) {
                drop();
            }

            if (e.getCode().equals(KeyCode.CONTROL)) {
                if (!paused) {
                    pause();
                    player.pause();
                } else {
                    resume();
                    player.play();
                }
            }

            if (e.getCode().equals(KeyCode.ESCAPE)) {
                startScreen();
            }

            if (e.getCode().equals(KeyCode.DOWN)) {
                highScorePane.changeLetterDown(highScorePane.currentInitial());
            }

            if (e.getCode().equals(KeyCode.UP)) {
                highScorePane.changeLetterUp(highScorePane.currentInitial());
            }

            if (e.getCode().equals(KeyCode.RIGHT)) {
                highScorePane.getNextLabel();
            }

            if (e.getCode().equals(KeyCode.LEFT)) {
                highScorePane.getPreviousLabel();
            }

            if (e.getCode().equals(KeyCode.ENTER)) {
                highScorePane.updateHighScores(currentScore);
                highScoresScreen();
            }

        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createColumn() {

        //0 is top
        gem3 = new Gem(0, -GEM_SIZE * 2, GEM_SIZE, 0,isChristmasTheme);
        //1 is middle
        gem2 = new Gem(0, -GEM_SIZE, GEM_SIZE, 1,isChristmasTheme);
        //2 is bottom
        gem1 = new Gem(0, 0, GEM_SIZE, 2,isChristmasTheme);
        currentGems.add(gem1);
        currentGems.add(gem2);
        currentGems.add(gem3);
    }

    private void moveLeft() {
        int curX = 0;
        int curY = 0;
        Gem bottom = null;
        Gem middle = null;
        Gem top = null;
        for (Gem gem : currentGems) {
            if (gem.getPosition() == 2) {
                curX = (int) gem.imageView.getLayoutX() / GEM_SIZE;
                curY = (int) gem.imageView.getLayoutX() / GEM_SIZE;
                bottom = gem;
            }
            if (gem.getPosition() == 1) {
                middle = gem;
            }
            if (gem.getPosition() == 0) {
                top = gem;
            }
        }
        if (curX > 0) {
            if (grid[curX - 1][curY].getColorId() == 0) {
                bottom.imageView.setLayoutX(bottom.imageView.getLayoutX() - GEM_SIZE);
                middle.imageView.setLayoutX(middle.imageView.getLayoutX() - GEM_SIZE);
                top.imageView.setLayoutX(top.imageView.getLayoutX() - GEM_SIZE);
            }
        }
    }


    private void moveRight() {
        int curX = 0;
        int curY = 0;
        Gem bottom = null;
        Gem middle = null;
        Gem top = null;
        for (Gem gem : currentGems) {
            if (gem.getPosition() == 2) {
                curX = (int) gem.imageView.getLayoutX() / GEM_SIZE;
                curY = (int) gem.imageView.getLayoutY() / GEM_SIZE;
                bottom = gem;
            }
            if (gem.getPosition() == 1) {
                middle = gem;
            }
            if (gem.getPosition() == 0) {
                top = gem;
            }
        }
        if (curX < BOARD_WIDTH - 1) {
            if (grid[curX + 1][curY].getColorId() == 0) {
                bottom.imageView.setLayoutX(bottom.imageView.getLayoutX() + GEM_SIZE);
                middle.imageView.setLayoutX(middle.imageView.getLayoutX() + GEM_SIZE);
                top.imageView.setLayoutX(top.imageView.getLayoutX() + GEM_SIZE);
            }
        }
    }


    private void checkMatches() {
        redrawBoard();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (matches.contains(grid[x][y]))
                    //check if we already cell to the list
                    continue;

                //check if colorId != 0
                if (grid[x][y].getColorId() != 0) {
                    //check down for 3 matches
                    if (y + 1 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 1].getColorId()) {
                        if (y + 2 < BOARD_HEIGHT && grid[x][y].getColorId() == grid[x][y + 2].getColorId()) {
                            matches.add(grid[x][y]);
                            matches.add(grid[x][y + 1]);
                            matches.add(grid[x][y + 2]);
                            grid[x][y].setDestory();
                            grid[x][y + 1].setDestory();
                            grid[x][y + 2].setDestory();
                            System.out.println("three down");
                            if (y + 3 < BOARD_HEIGHT) {
                                if (grid[x][y].getColorId() == grid[x][y + 3].getColorId()) {
                                    matches.add(grid[x][y + 3]);
                                    grid[x][y + 3].setDestory();
                                    if (y + 4 < BOARD_HEIGHT) {
                                        if (grid[x][y].getColorId() == grid[x][y + 4].getColorId()) {
                                            matches.add(grid[x][y + 4]);
                                            grid[x][y + 4].setDestory();
                                        }
                                    }
                                }
                            }
                        }
                    }

                        //check right for 3 matches
                        if (x + 1 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 1][y].getColorId()) {
                            if (x + 2 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 2][y].getColorId()) {
                                matches.add(grid[x][y]);
                                matches.add(grid[x + 1][y]);
                                matches.add(grid[x + 2][y]);
                                grid[x][y].setDestory();
                                grid[x + 1][y].setDestory();
                                grid[x + 2][y].setDestory();
                                System.out.println("three across");
                                if (x + 3 < BOARD_WIDTH) {
                                    if (grid[x][y].getColorId() == grid[x + 3][y].getColorId()) {
                                        matches.add(grid[x + 3][y]);
                                        grid[x + 3][y].setDestory();
                                        if (x + 4 < BOARD_WIDTH) {
                                            if (grid[x][y].getColorId() == grid[x + 4][y].getColorId()) {
                                                matches.add(grid[x + 4][y]);
                                                grid[x + 4][y].setDestory();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //check down right diagonal for 3 matches
                        if (y + 1 < BOARD_HEIGHT && x + 1 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 1][y + 1].getColorId()) {
                            if (y + 2 < BOARD_HEIGHT && x + 2 < BOARD_WIDTH && grid[x][y].getColorId() == grid[x + 2][y + 2].getColorId()) {
                                matches.add(grid[x][y]);
                                matches.add(grid[x + 1][y + 1]);
                                matches.add(grid[x + 2][y + 2]);
                                grid[x][y].setDestory();
                                grid[x + 1][y + 1].setDestory();
                                grid[x + 2][y + 2].setDestory();
                                System.out.println("three down right");
                                if (x + 3 < BOARD_WIDTH && y + 3 < BOARD_HEIGHT) {
                                    if (grid[x][y].getColorId() == grid[x + 3][y + 3].getColorId()) {
                                        matches.add(grid[x + 3][y + 3]);
                                        grid[x + 3][y + 3].setDestory();
                                        if (x + 4 < BOARD_WIDTH && y + 4 < BOARD_HEIGHT) {
                                            if (grid[x][y].getColorId() == grid[x + 4][y + 4].getColorId()) {
                                                matches.add(grid[x + 4][y + 4]);
                                                grid[x + 4][y + 4].setDestory();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //check down left diagonal for 3 matches
                        if (y + 1 < BOARD_HEIGHT && x - 1 < BOARD_WIDTH && x - 1 >= 0 && grid[x][y].getColorId() == grid[x - 1][y + 1].getColorId()) {
                            if (y + 2 < BOARD_HEIGHT && x - 2 < BOARD_WIDTH && x - 2 >= 0 && grid[x][y].getColorId() == grid[x - 2][y + 2].getColorId()) {
                                matches.add(grid[x][y]);
                                matches.add(grid[x - 1][y + 1]);
                                matches.add(grid[x - 2][y + 2]);
                                grid[x][y].setDestory();
                                grid[x - 1][y + 1].setDestory();
                                grid[x - 2][y + 2].setDestory();
                                System.out.println("three down left");
                                if (x - 3 >= 0 && y + 3 < BOARD_HEIGHT) {
                                    if (grid[x][y].getColorId() == grid[x - 3][y + 3].getColorId()) {
                                        matches.add(grid[x - 3][y + 3]);
                                        grid[x - 3][y + 3].setDestory();
                                        if (x - 4 >= 0 && y + 4 < BOARD_HEIGHT) {
                                            if (grid[x][y].getColorId() == grid[x - 4][y + 4].getColorId()) {
                                                matches.add(grid[x - 4][y + 4]);
                                                grid[x - 4][y + 4].setDestory();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }



        if (matches.size() >= 3) {
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    if (grid[x][y].destroy) {
                        destroyGem(grid[x][y]);
                        grid[x][y] = new Gem(0, 0, 0);
                        currentScore += 50 * matchedThreeMultiplier * currentLevel;
                        currentScoreLabel.setText(currentScore + "");
                        gemCount++;
                        currentGemsLabel.setText(gemCount + "");
                        if (currentScore % 2000 == 0) {
                            currentLevel++;
                            System.out.println("current level " + currentLevel);
                            currentLevelLabel.setText(currentLevel + "");
                            gameTimer.increaseSpeed(100);
                        }
                    }
                }
            }
            matchedThreeMultiplier += 2;

        } else {
            redrawBoard();
            makeNewColumn(pane);
            matchedThreeMultiplier = 1;
        }

    }

    public void destroyGem(Gem gem) {
        tt = new TranslateTransition();
        tt.setToY(-(GEM_SIZE * 3));
        tt.setDuration(Duration.millis(500));
        tt.setCycleCount(1);
        tt.setNode(gem.imageView);
        this.fd = new FadeTransition();
        fd.setFromValue(10);
        fd.setToValue(0);
        fd.setDuration(Duration.millis(500));
        fd.setNode(gem.imageView);
        fd.setCycleCount(1);
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                tt, fd
        );
        parallelTransition.play();
        if (!settingsPane.getDisableFX()) {
            score.play(1);
        }
        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (matches.size() == 1) {
                    matches.remove(gem);
                    destroyComplete = true;
                    redrawBoard();
                    checkMatches();
                } else {
                    matches.remove(gem);
                }
            }

        });

    }

    private void moveDown() {

        Gem bottom = null;

        for (Gem gem : currentGems) {
            if (gem.getInitialPosition() == 2) {
                bottom = gem;
            }
            if (bottom.imageView.getLayoutY() / GEM_SIZE < BOARD_HEIGHT) {
                gem.imageView.setLayoutY((int) gem.imageView.getLayoutY() + GEM_SIZE);
            }
        }
        //if bottom piece is still at the bottom
        if (bottom.getPosition() == 2) {

            //check if there is a gem below or if we are at the bottom
            if (bottom.imageView.getLayoutY() / GEM_SIZE < BOARD_HEIGHT - 1 && grid[(int) bottom.imageView.getLayoutX() / GEM_SIZE][(int) bottom.imageView.getLayoutY() / GEM_SIZE + 1].getColorId() > 0) {
                //check for game over
                if (bottom.imageView.getLayoutY() / GEM_SIZE - 2 <= 0) {
                    System.out.println("game over");
                    game = false;
                } else {
                    setPiece();
                }
            } else if (bottom.imageView.getLayoutY() / GEM_SIZE == BOARD_HEIGHT - 1) {
                setPiece();
            }


        }
        //bottom is in the middle
        else if (bottom.getPosition() == 0) {
            if (bottom.imageView.getLayoutY() / GEM_SIZE < BOARD_HEIGHT - 3 && grid[(int) bottom.imageView.getLayoutX() / GEM_SIZE][(int) bottom.imageView.getLayoutY() / GEM_SIZE + 3].getColorId() > 0) {
                //check for game over
                if (bottom.imageView.getLayoutY() / GEM_SIZE - 1 <= 0) {
                    game = false;
                } else {
                    setPiece();
                }
            } else if (bottom.imageView.getLayoutY() / GEM_SIZE == BOARD_HEIGHT - 3) {
                setPiece();
            }


        }
        //bottom is at the top
        else {
            if (bottom.imageView.getLayoutY() / GEM_SIZE < BOARD_HEIGHT - 2 && grid[(int) bottom.imageView.getLayoutX() / GEM_SIZE][(int) bottom.imageView.getLayoutY() / GEM_SIZE + 2].getColorId() > 0) {
                //check for game over
                if (bottom.imageView.getLayoutY() / GEM_SIZE <= 0) {
                    game = false;
                } else {
                    setPiece();
                }
            } else if (bottom.imageView.getLayoutY() / GEM_SIZE == BOARD_HEIGHT - 2) {
                setPiece();
            }


        }

    }

    private void setPiece() {
        if(!settingsPane.getDisableFX()) {
            drop.play(1);
        }
        isFalling = false;
        pause();
        setGridPositions();
        checkMatches();
    }

    private void newGame() {
        pane = new Pane();
        scoreHBox = new HBox();
        gemsVBox = new VBox();
        levelVBox = new VBox();

        currentScoreLabel = new Label();
        scoreLabel = new Label();
        gemsLabel = new Label();
        currentGemsLabel = new Label();
        levelLabel = new Label();
        currentLevelLabel = new Label();

        pane.setPrefSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);

        if(isChristmasTheme) {
            track = christmas;
            player = new MediaPlayer(track);
            scoreLabel.getStyleClass().add("christmas-label");
            gemsLabel.getStyleClass().add("christmas-label");
            levelLabel.getStyleClass().add("christmas-label");
            pane.getStyleClass().add("christmas-grid");
        } else {
            track = level1;
            player = new MediaPlayer(track);
            scoreLabel.getStyleClass().add("label");
            gemsLabel.getStyleClass().add("label");
            levelLabel.getStyleClass().add("label");
            pane.getStyleClass().add("grid");
        }

        if(settingsPane.getDisableMusic()) {
            player.setVolume(0);
        } else {
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(40);
        }


        root.setLeft(pane);
        root.setCenter(sidePanel);

        currentScore = 0;
        currentScoreLabel.getStyleClass().add("changingLabel");
        currentScoreLabel.setText("" + currentScore);
        scoreLabel.setText("Score");
        gemsLabel.setText("Gems");
        levelLabel.setText("Level");

        gemCount = 0;
        currentGemsLabel.getStyleClass().add("changingLabel");
        currentGemsLabel.setText("" + gemCount);

        currentLevel = 1;
        currentLevelLabel.getStyleClass().add("changingLabel");
        currentLevelLabel.setText("" + currentLevel);

        scoreHBox.getStyleClass().add("hbox");
        scoreHBox.getChildren().add(scoreLabel);
        scoreHBox.setAlignment(Pos.BOTTOM_CENTER);

        gemsVBox.getStyleClass().add("hbox");
        gemsVBox.getChildren().add(gemsLabel);
        gemsVBox.setAlignment(Pos.BOTTOM_CENTER);

        levelVBox.getStyleClass().add("hbox");
        levelVBox.getChildren().add(levelLabel);
        levelVBox.setAlignment(Pos.BOTTOM_CENTER);

        sidePanel.add(scoreHBox,0,0);
        sidePanel.add(gemsVBox,0,2);
        sidePanel.add(levelVBox,0,4);

        sidePanel.setHalignment(currentScoreLabel, HPos.RIGHT);
        sidePanel.add(currentScoreLabel,0,1);

        sidePanel.setHalignment(currentGemsLabel, HPos.RIGHT);
        sidePanel.add(currentGemsLabel,0,3);

        sidePanel.setHalignment(currentLevelLabel, HPos.RIGHT);
        sidePanel.add(currentLevelLabel,0,5);


        fillArray();
        redrawBoard();
        game = true;
        gameTimer.start();
        createColumn();
        pane.getChildren().addAll(gem1.imageView, gem2.imageView, gem3.imageView);
    }

    private void redrawBoard() {
        pane.getChildren().clear();
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT - 1; y++) {
                if (grid[x][y].getColorId() > 0) {
                    if (grid[x][y + 1].getColorId() == 0) {
                        grid[x][y + 1] = grid[x][y];
                        grid[x][y] = new Gem(0, 0, 0);
                    }
                }
            }
        }

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y].getColorId() > 0) {
//                    System.out.println("gem " + grid[x][y].getColorId() + " color, should move to x:" + x + " y:" + y);
                    grid[x][y].imageView.relocate(x * GEM_SIZE, y * GEM_SIZE);
                    pane.getChildren().add(grid[x][y].imageView);
                }
            }
        }
    }

    private void drop() {
        Gem bottom = null;
        Gem middle = null;
        Gem top = null;
        int emptyY = 0;

        //set the gem order
        for (Gem gem : currentGems) {
            if (gem.getInitialPosition() == 2) {
                bottom = gem;
            }
            else if(gem.getInitialPosition() == 1) {
                middle = gem;
            } else {
                top = gem;
            }
        }
        //check the next empty Y coordinate
        for (int y = 0; y < grid[(int)bottom.imageView.getLayoutX() / GEM_SIZE].length; y++) {
            if (grid[(int)bottom.imageView.getLayoutX() / GEM_SIZE][y].getColorId() == 0) {
                emptyY = y;
            }
        }

        //bottom is at the bottom
        if (bottom.getPosition() == 2) {
            top.imageView.relocate(top.imageView.getLayoutX(),(emptyY - 2) * GEM_SIZE);
            middle.imageView.relocate(middle.imageView.getLayoutX(),(emptyY - 1) * GEM_SIZE);
            bottom.imageView.relocate(bottom.imageView.getLayoutX(),emptyY * GEM_SIZE);
            setPiece();
        }
        else if (bottom.getPosition() == 1)
        {
            middle.imageView.relocate(middle.imageView.getLayoutX(),(emptyY - 2) * GEM_SIZE);
            bottom.imageView.relocate(bottom.imageView.getLayoutX(),(emptyY - 1) * GEM_SIZE);
            top.imageView.relocate(top.imageView.getLayoutX(),emptyY * GEM_SIZE );
            setPiece();
        }
        else
        {
            bottom.imageView.relocate(middle.imageView.getLayoutX(),(emptyY - 2) * GEM_SIZE);
            top.imageView.relocate(bottom.imageView.getLayoutX(),(emptyY - 1) * GEM_SIZE);
            middle.imageView.relocate(top.imageView.getLayoutX(),emptyY * GEM_SIZE);
            setPiece();
        }
    }

    public void makeNewColumn(Pane pane) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (paused) resume();
//                printArray();
                gems.addAll(currentGems);
                currentGems.clear();
                createColumn();
                columnCreated++;
//                System.out.println(columnCreated);
                pane.getChildren().addAll(gem1.imageView, gem2.imageView, gem3.imageView);
                isFalling = true;
            }
        });
    }


    private boolean checkKeyPress() {
        if (!isKeyPressed) {
            return true;
        } else {
            return false;
        }

    }

    private void pause() {
        gameTimer.stop();
        paused = true;
    }

    private void resume() {
        gameTimer.start();
        paused = false;
    }

    private void setGridPositions() {
        //gem1 is at the
//        System.out.println(gem1.getPosition());
        if (gem1.getPosition() == 0) {
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 1] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 2] = gem3;
        }
        //gem1 is in the middle
        else if (gem1.getPosition() == 1) {
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 1] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 2] = gem3;
        } else
        //gem1 is at the bottom
        {
            grid[(int) gem1.imageView.getLayoutX() / GEM_SIZE][(int) gem1.imageView.getLayoutY() / GEM_SIZE] = gem1;
            grid[(int) gem2.imageView.getLayoutX() / GEM_SIZE][(int) gem2.imageView.getLayoutY() / GEM_SIZE - 1] = gem2;
            grid[(int) gem3.imageView.getLayoutX() / GEM_SIZE][(int) gem3.imageView.getLayoutY() / GEM_SIZE - 2] = gem3;
        }
    }

    private void fillArray() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                grid[x][y] = new Gem(0,0,0);
    }

    private void printArray() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                System.out.println("x= " + x + " y=" + y + " Position: " + grid[x][y].position + " Color: " + grid[x][y].getColorId() + " Destroy:" + grid[x][y].destroy);
    }

    public void sortGems(){
//        System.out.println(gem1.getPosition());


        //2 is the bottom
        //1 is the middle
        //0 is the top
        //bottom is at the bottom
        if (gem1.getPosition() == 2) {
            //top down 1
            gem3.imageView.setLayoutY((int)gem3.imageView.getLayoutY() + GEM_SIZE);//B
            gem3.setPosition(1);
            //middle down 1
            gem2.imageView.setLayoutY((int)gem2.imageView.getLayoutY() + GEM_SIZE);     //M
            gem2.setPosition(2);
            //bottom to top
            gem1.imageView.setLayoutY((int)gem1.imageView.getLayoutY() - (GEM_SIZE * 2));//T
            gem1.setPosition(0);
        }

        //bottom is in the middle
        else if (gem1.getPosition() == 1)
        {
            //bottom down 1
            gem1.imageView.setLayoutY(gem1.imageView.getLayoutY() + GEM_SIZE ); //B
            gem1.setPosition(2);
            //middle down 1
            gem2.imageView.setLayoutY(gem2.imageView.getLayoutY() + GEM_SIZE);        //M
            gem2.setPosition(1);
            //top to top
            gem3.imageView.setLayoutY(gem3.imageView.getLayoutY() - (GEM_SIZE*2));                  //T
            gem3.setPosition(0);
        }

        //bottom is at the top
        else
        {
            //bottom down 1`
            gem1.imageView.setLayoutY(gem1.imageView.getLayoutY() + GEM_SIZE); //B
            gem1.setPosition(1);
            //top down 1
            gem3.imageView.setLayoutY(gem3.imageView.getLayoutY() + GEM_SIZE);  //M
            gem3.setPosition(0);
            //middle to top
            gem2.imageView.setLayoutY(gem2.imageView.getLayoutY() - (GEM_SIZE*2));                //T
            gem2.setPosition(2);
        }

    }

    private void gameOver() {
        gameOverScreen = new Pane();
        gameOverHBox = new HBox();
        gameOverLabel = new Label("GAME OVER");


        gameOverLabel.getStyleClass().add("heading");
        gameOverScreen.getStyleClass().add("game-over");
        gameOverScreen.setMinSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);
        gameOverLabel.setLayoutX(GEM_SIZE * 2);
        gameOverLabel.setLayoutY(GEM_SIZE * 4);
        gameOverLabel.setWrapText(true);


        gameOverHBox.getStyleClass().add("action-button");
        Text restartText = new Text("Restart");
        restartText.getStyleClass().add("action-button-text");
        gameOverHBox.setAlignment(Pos.BOTTOM_CENTER);
        gameOverHBox.setLayoutX(GEM_SIZE * 2 + 10);
        gameOverHBox.setLayoutY(GEM_SIZE * 7);
        gameOverHBox.getChildren().add(restartText);


        if (highScorePane.checkScore(currentScore)) {

            Label enterInitials = new Label("ENTER INITITALS");
            GridPane highScore = highScorePane.newHighScore();
            highScore.setMaxWidth(240);
            highScore.setLayoutX(GEM_SIZE * 2 - 35);
            highScore.setLayoutY(GEM_SIZE * 10);


            enterInitials.getStyleClass().add("initials-heading");
            enterInitials.setLayoutX(GEM_SIZE * 2);
            enterInitials.setLayoutY(GEM_SIZE * 9);
            enterInitials.setWrapText(true);
            gameOverScreen.getChildren().addAll(gameOverLabel, gameOverHBox, enterInitials, highScore);
        } else {
            gameOverScreen.getChildren().addAll(gameOverLabel, gameOverHBox);
        }

        root.setLeft(gameOverScreen);
        player.stop();
        gameTimer.stop();
        gameOverHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Clicked");
                newGame();
            }
        });

    }

    private void highScoresScreen() {
        HighScorePane highScorePane = new HighScorePane();
        GridPane scoresPane = highScorePane.getGridPane();
        sidePanel = new GridPane();
        sidePanel.getStyleClass().add("game-over");
        scoresPane.setMinSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);
        root.setLeft(scoresPane);
        root.setCenter(sidePanel);
    }

    private void startScreen() {
        isChristmasTheme = settingsPane.getChristmasTheme();
        if (player != null) {
            player.stop();
        }
        gameTimer.stop();
        pane = new Pane();
        sidePanel = new GridPane();
        sidePanel.getStyleClass().add("stackpane");
        sidePanel.setHgap(10);
        sidePanel.setGridLinesVisible(false);
        root.setCenter(sidePanel);
        HBox playButton = new HBox();
        HBox highScoresButton = new HBox();
        HBox settingsButton = new HBox();
        Label title;
        if(isChristmasTheme) {
            title = new Label("GEM XMAS");
            title.getStyleClass().add("christmas-heading");
        } else {
            title = new Label("GEM JAM");
            title.getStyleClass().add("heading");
        }
        Label play = new Label("Play");
        Label highScores = new Label("Scores");
        Label settings = new Label("Settings");


        pane.setPrefSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);
        pane.getStyleClass().add("game-over");


        title.setLayoutX(GEM_SIZE * 2);
        title.setLayoutY(GEM_SIZE * 4);
        title.setWrapText(true);

        playButton.getStyleClass().add("action-button");
        playButton.setAlignment(Pos.BOTTOM_CENTER);
        playButton.setLayoutX(GEM_SIZE * 2 - 15);
        playButton.setLayoutY(GEM_SIZE * 7);
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newGame();
            }
        });
        playButton.getChildren().addAll(play);

        settingsButton.getStyleClass().add("action-button");
        settingsButton.setAlignment(Pos.BOTTOM_CENTER);
        settingsButton.setLayoutX(GEM_SIZE * 2 - 15);
        settingsButton.setLayoutY(GEM_SIZE * 8);
        settingsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                settingsScreen();
            }
        });
        settingsButton.getChildren().addAll(settings);

        highScoresButton.getStyleClass().add("action-button");
        highScoresButton.setAlignment(Pos.BOTTOM_CENTER);
        highScoresButton.setLayoutX(GEM_SIZE * 2 - 15);
        highScoresButton.setLayoutY(GEM_SIZE * 9);
        highScoresButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                highScoresScreen();
            }
        });
        highScoresButton.getChildren().addAll(highScores);

        play.getStyleClass().add("action-button-text");
        highScores.getStyleClass().add("action-button-text");
        settings.getStyleClass().add("action-button-text");


        pane.getChildren().addAll(title, playButton, settingsButton, highScoresButton);

        root.setLeft(pane);
    }

    private void settingsScreen() {
        root.setLeft(settingsPane.getGridPane());
    }

}



