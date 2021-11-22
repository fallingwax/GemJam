package gemjam;

import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    // Sound effects
    AudioClip swap;
    AudioClip drop;
    AudioClip score;

    // Music
    Media level1;
    Media christmas;

    // Media Player
    Media track;
    MediaPlayer player;

    // The board size and game piece size
    final static int BOARD_WIDTH = 6;
    final static int BOARD_HEIGHT = 13;
    final static int GEM_SIZE = 80;

    // the UI elements
    BorderPane root;
    Pane pane;
    HighScorePane highScorePane = new HighScorePane();
    SettingsPane settingsPane = new SettingsPane();
    GameOverPane gameOverPane;
    SidePanel sidePanel;

    // values for the current score, total gems, current level, current X and Y values
    int currentScore;
    int gemCount;
    int currentLevel;
    int curX;
    int curY;

    // The board and the gems
    Board board;
    Gem gem1;
    Gem gem2;
    Gem gem3;
    Gem bottom;
    Gem middle;
    Gem top;
    Gem nextGem1;
    Gem nextGem2;
    Gem nextGem3;

    // all flags
    boolean isFalling = false;
    boolean isKeyPressed = false;
    boolean game = false;
    boolean paused = false;
    boolean destroyComplete;
    boolean isChristmasTheme = false;
    boolean hasBeenRecorded = false;
    boolean onHighScore = false;

    // the score multiplier
    int matchedThreeMultiplier = 1;

    // timestamps
    long timestamp;
    long prevTimestamp;

    // count of the number of columns
    int columnCreated = 0;

    // Lists to hold the current gems and the matched gems
    List<Gem> gems = new ArrayList<>();
    List<Gem> currentGems = new ArrayList<>();
    List<Gem> matches = new ArrayList<>();

    // The game Loop
    GameTimer gameTimer = new GameTimer(800) {
        @Override
        public void game() {
            if (game) {
                moveDown();

            }
        }
    };

    @Override
    public void start(Stage primaryStage) {

        // set up the scene
        root = new BorderPane();
        Scene scene = new Scene(root, BOARD_WIDTH * GEM_SIZE + 200, BOARD_HEIGHT * GEM_SIZE);
        Font.loadFont(getClass().getResourceAsStream("ARCADE.TTF"),-1);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        primaryStage.setTitle("Gem Jam");
        primaryStage.setResizable(false);
        sidePanel = new SidePanel();

        // add the side panel
        root.setCenter(sidePanel);

        // set the start screen
        startScreen();

        // setting up the game controls. Using flags to restrict button use to specific screens
        scene.setOnKeyPressed((e) -> {
            if (game && e.getCode().equals(KeyCode.S)) {
                moveDown();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (game && e.getCode().equals(KeyCode.SPACE)) {
                if (!settingsPane.getDisableFX()) {
                    swap.play(1);
                }
                sortGems();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (game && e.getCode().equals(KeyCode.A)) {
                moveLeft();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (game && e.getCode().equals(KeyCode.D)) {
                moveRight();
                prevTimestamp = timestamp;
                timestamp = System.currentTimeMillis();
            }

            if (game && e.getCode().equals(KeyCode.W)) {
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

            if (!game && e.getCode().equals(KeyCode.DOWN)) {
                highScorePane.changeLetterDown(highScorePane.currentInitial());
            }

            if (!game && e.getCode().equals(KeyCode.UP)) {
                highScorePane.changeLetterUp(highScorePane.currentInitial());
            }

            if (!game && e.getCode().equals(KeyCode.RIGHT)) {
                highScorePane.getNextLabel();
            }

            if (onHighScore && !hasBeenRecorded && e.getCode().equals(KeyCode.ENTER)) {
                highScorePane.updateHighScores(currentScore);
                highScoresScreen();
                hasBeenRecorded = true;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * A method to create the first two columns of gems and add them to the game
     */
    private void createFirstColumn() {
        //0 is top
        gem3 = new Gem(0, -GEM_SIZE * 2, GEM_SIZE, 0,isChristmasTheme);
        //1 is middle
        gem2 = new Gem(0, -GEM_SIZE, GEM_SIZE, 1,isChristmasTheme);
        //2 is bottom
        gem1 = new Gem(0, 0, GEM_SIZE, 2,isChristmasTheme);

        //0 is top
        nextGem3 = new Gem(0, -GEM_SIZE * 2, GEM_SIZE, 0,isChristmasTheme);
        //1 is middle
        nextGem2 = new Gem(0, -GEM_SIZE, GEM_SIZE, 1,isChristmasTheme);
        //2 is bottom
        nextGem1 = new Gem(0, 0, GEM_SIZE, 2,isChristmasTheme);

        sidePanel.getGemView().getChildren().addAll(nextGem3.getImageView(), nextGem2.getImageView(), nextGem1.getImageView());

        currentGems.add(gem1);
        currentGems.add(gem2);
        currentGems.add(gem3);
    }

    /**
     * A method to create the next column of gems and move the next piece to the game board
     */
    private void createNextColumnm() {

        //copy next set off gems to gem1 - 3
        gem1 = nextGem1;
        gem2 = nextGem2;
        gem3 = nextGem3;

        //adjust gem positions upon entering play field
        gem1.getImageView().setLayoutY(0);
        gem2.getImageView().setLayoutY(0);
        gem3.getImageView().setLayoutY(0);
        gem1.getImageView().setLayoutX(GEM_SIZE * 2);
        gem2.getImageView().setLayoutX(GEM_SIZE * 2);
        gem3.getImageView().setLayoutX(GEM_SIZE * 2);

        //add to current gems
        currentGems.add(gem1);
        currentGems.add(gem2);
        currentGems.add(gem3);

        //create next set off gems
        //0 is top
        nextGem3 = new Gem(0, -GEM_SIZE * 2, GEM_SIZE, 0,isChristmasTheme);
        //1 is middle
        nextGem2 = new Gem(0, -GEM_SIZE, GEM_SIZE, 1,isChristmasTheme);
        //2 is bottom
        nextGem1 = new Gem(0, 0, GEM_SIZE, 2,isChristmasTheme);

        //add them to the side panel
        sidePanel.getGemView().getChildren().addAll(nextGem3.getImageView(), nextGem2.getImageView(), nextGem1.getImageView());

        //increment column count
        columnCreated++;

        //add new gems to game
        pane.getChildren().addAll(gem3.getImageView(), gem2.getImageView(), gem1.getImageView());

    }

    /**
     * A method to move the column of gems left
     */
    private void moveLeft() {
        setGems(currentGems);
        // check that we are on the board and not blocked
        if (board.checkLeft(curX, curY)) {
            bottom.getImageView().setLayoutX(bottom.getImageView().getLayoutX() - GEM_SIZE);
            middle.getImageView().setLayoutX(middle.getImageView().getLayoutX() - GEM_SIZE);
            top.getImageView().setLayoutX(top.getImageView().getLayoutX() - GEM_SIZE);
        }
    }

    /**
     * A method to move the column of gems right
     */
    private void moveRight() {
        setGems(currentGems);
        // check that we are on the board and not blocked
        if (board.checkRight(curX,curY)) {
                bottom.getImageView().setLayoutX(bottom.getImageView().getLayoutX() + GEM_SIZE);
                middle.getImageView().setLayoutX(middle.getImageView().getLayoutX() + GEM_SIZE);
                top.getImageView().setLayoutX(top.getImageView().getLayoutX() + GEM_SIZE);
            }
        }

    /**
     * A method to move the column of gems down the board.
     */
    private void moveDown() {
        setGems(currentGems);
        // check that we are on the board and not blocked
        if(board.checkDown(curX, curY)) {
            bottom.getImageView().setLayoutY(bottom.getImageView().getLayoutY() + GEM_SIZE);
            middle.getImageView().setLayoutY(middle.getImageView().getLayoutY() + GEM_SIZE);
            top.getImageView().setLayoutY(top.getImageView().getLayoutY() + GEM_SIZE);
        } else {
            setPiece(curY);
        }

    }

    /**
     * A method to determine which positions the gems are in
     * @param gemList the current gems
     */
    private void setGems(List<Gem> gemList) {
        for (Gem gem : gemList) {
            if (gem.getPosition() == 2) {
                curX = (int) gem.getImageView().getLayoutX() / GEM_SIZE;
                curY = ((int) gem.getImageView().getLayoutY() / GEM_SIZE) + 1;
                bottom = gem;
            }
            if (gem.getPosition() == 1) {
                middle = gem;
            }
            if (gem.getPosition() == 0) {
                top = gem;
            }
        }
    }

    /**
     * A method to check all of the matches on the board, destroy any matched gems and update the UI elements
     */
    private void checkMatches() {

        // redraw the board here to make sure we have removed any blank spaces before checking for matches
        board.redraw();
        // get the current number of matches
        matches = board.getMatches();

        // if matches has more than 3 elements
        if (matches.size() >= 3) {
            for (int x = 0; x < board.grid.length; x++) {
                for (int y = 0; y < board.grid[x].length; y++) {

                    // check if the gem should be destroyed
                    if (board.grid[x][y].getDestroy()) {

                        // destroy the gem
                        destroyGem(board.grid[x][y]);

                        // create a blank gem where the destroyed gem was located
                        board.grid[x][y] = new Gem(0, 0, 0);

                        // update the score
                        currentScore += 50 * matchedThreeMultiplier * currentLevel;
                        SidePanel.setScore(currentScore);

                        // update the gem count
                        gemCount++;
                        SidePanel.setGemCount(gemCount);

                        // update the game level and increse the game speed
                        if (currentScore % 2000 == 0) {
                            currentLevel++;
                            SidePanel.setLevel(currentLevel);
                            gameTimer.increaseSpeed(50);

                            // reset the game speed if we are at level 10
                            if (currentLevel % 10 == 0) {
                                gameTimer.resetSpeed();
                            }
                        }
                    }
                }
            }

            // increase the multiplier for the next set off matches
            matchedThreeMultiplier += 2;

        }

        // create a new column if no matches and reset the multiplier
        if (matches.size() == 0) {
            makeNewColumn();
            matchedThreeMultiplier = 1;
        }
    }

    /**
     * A method to destroy the current gem
     * @param gem the current
     */
    public void destroyGem(Gem gem) {
        // set the animation
        GemTransition gt = new GemTransition(gem, GEM_SIZE);
        ParallelTransition parallelTransition = gt.getPt();
        parallelTransition.play();

        // remove the fx if disabled
        if (!settingsPane.getDisableFX()) {
            score.play(1);
        }

        // set up what to do after we destroy a gem.
        parallelTransition.setOnFinished(actionEvent -> {

            // if we are one more match to remove, remove the match and set the flag that we are done destroying pieces and check for more matches.
            if (matches.size() == 1) {
                // remove the last gem from matches
                matches.remove(gem);
                destroyComplete = true;
                // after all are destroyed, we need to check for more matches
                checkMatches();
            } else {
                // remove the current gem and keep destroying gems
                matches.remove(gem);
            }
        });
    }

    /**
     * A method to set the current column of gems in place
     * @param y the current Y position
     */
    private void setPiece(int y) {
        // pause the timer while we set the piece and check from matches
        pause();
        isFalling = false;
        // check for game over, if a piece is set above the top row of the game board
        if(board.checkTop((int) top.getImageView().getLayoutY())) {
            game = false;
            gameOver();
        } else {

            // play fx
            if (!settingsPane.getDisableFX()) {
                drop.play(1);
            }

            // set the columns position on the game board and place the gems in the array
            board.setGridPositions(currentGems, y);

            // check from matches on the game board
            checkMatches();
        }
    }

    /**
     * A method to start a new game.
     */
    private void newGame() {

        // Sound effects
        swap = new AudioClip(getClass().getResource("/sounds/swoosh.wav").toExternalForm());
        drop = new AudioClip(getClass().getResource("/sounds/drop.wav").toExternalForm());
        score = new AudioClip(getClass().getResource("/sounds/score.wav").toExternalForm());

        // Music
        level1 = new Media(getClass().getResource("/sounds/track1.mp3").toExternalForm());
        //https://rushcoil.bandcamp.com/album/xmas-compilation-2010
        christmas = new Media(getClass().getResource("/sounds/RushJet1 - XMAS Compilation 2010 - 13 O Holy Night.mp3").toExternalForm());

        onHighScore = false;

        // set game flag to true
        game = true;

        // setup the board
        pane = new Pane();
        sidePanel = new SidePanel(isChristmasTheme);
        board = new Board();
        pane.setPrefSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);

        // check if we are running the christmas theme
        if(isChristmasTheme) {
            track = christmas;
            player = new MediaPlayer(track);
            pane.getStyleClass().add("christmas-grid");
        } else {
            track = level1;
            player = new MediaPlayer(track);
            pane.getStyleClass().add("grid");
        }

        // setup the player volume
        if(settingsPane.getDisableMusic()) {
            player.setVolume(0);
        } else {
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(40);
        }

        // add elements to the scene
        root.setLeft(pane);
        root.setCenter(sidePanel);

        // set the defaults
        currentScore = 0;
        gemCount = 0;
        currentLevel = 1;

        // start the game loop
        gameTimer.start();

        // play the music
        player.play();

        // create the first columns
        createFirstColumn();
        pane.getChildren().addAll(gem1.getImageView(), gem2.getImageView(), gem3.getImageView());
    }

    /**
     * A method to immediately drop the current column into place
     */
    private void drop() {

        // the value of the next availabe Y coordinate
        int emptyY = 0;

        //set the gem order
        setGems(currentGems);

        //check the next empty Y coordinate
        for (int y = 0; y < board.grid[(int)bottom.getImageView().getLayoutX() / GEM_SIZE].length; y++) {
            if (board.grid[(int)bottom.getImageView().getLayoutX() / GEM_SIZE][y].getColorId() == 0) {
//                System.out.println("Empty Spot is " + y);
                emptyY = y;
            }
        }

        //bottom is at the bottom
        if (bottom.getPosition() == 2) {
            top.getImageView().relocate(top.getImageView().getLayoutX(),(emptyY - 2) * GEM_SIZE);
            middle.getImageView().relocate(middle.getImageView().getLayoutX(),(emptyY - 1) * GEM_SIZE);
            bottom.getImageView().relocate(bottom.getImageView().getLayoutX(),emptyY * GEM_SIZE);
            setPiece(emptyY);
        }
        else if (bottom.getPosition() == 1)
        {
            middle.getImageView().relocate(middle.getImageView().getLayoutX(),(emptyY - 2) * GEM_SIZE);
            bottom.getImageView().relocate(bottom.getImageView().getLayoutX(),(emptyY - 1) * GEM_SIZE);
            top.getImageView().relocate(top.getImageView().getLayoutX(),emptyY * GEM_SIZE );
            setPiece(emptyY);
        }
        else
        {
            bottom.getImageView().relocate(middle.getImageView().getLayoutX(),(emptyY - 2) * GEM_SIZE);
            top.getImageView().relocate(bottom.getImageView().getLayoutX(),(emptyY - 1) * GEM_SIZE);
            middle.getImageView().relocate(top.getImageView().getLayoutX(),emptyY * GEM_SIZE);
            setPiece(emptyY);
        }
    }

    /**
     * A method to make a new column of gems
     */
    public void makeNewColumn() {

        //run later so that we execute this method on the current thread
        Platform.runLater(() -> {

            // restart the timer if we are paused
            if (paused) resume();
            // add all the current gems to the gems array
            gems.addAll(currentGems);
            currentGems.clear();

            // create the new columns
            createNextColumnm();

            // set the flag is falling to true
            isFalling = true;
        });
    }

    /**
     * An unused method to check we have pressed a key. #FIXME to give a second to keep swaping pieces before we set the piece
     * @return boolean
     */
    private boolean checkKeyPress() {
        return !isKeyPressed;
    }

    /**
     * A method to pause the game timer
     */
    private void pause() {
        gameTimer.stop();
        paused = true;
    }

    /**
     * A method to restart the game timer
     */
    private void resume() {
        gameTimer.start();
        paused = false;
    }

    /**
     * A method to sort the order of the game pieces within the column.  TMB -> BTM -> MBT
     */
    public void sortGems(){

        //2 is the bottom
        //1 is the middle
        //0 is the top
        //bottom is at the bottom
        if (gem1.getPosition() == 2) {
            //top down 1
            gem3.getImageView().setLayoutY((int)gem3.getImageView().getLayoutY() + GEM_SIZE);//B
            gem3.setPosition(1);
            //middle down 1
            gem2.getImageView().setLayoutY((int)gem2.getImageView().getLayoutY() + GEM_SIZE);     //M
            gem2.setPosition(2);
            //bottom to top
            gem1.getImageView().setLayoutY((int)gem1.getImageView().getLayoutY() - (GEM_SIZE * 2));//T
            gem1.setPosition(0);
        }

        //bottom is in the middle
        else if (gem1.getPosition() == 1)
        {
            //bottom down 1
            gem1.getImageView().setLayoutY(gem1.getImageView().getLayoutY() + GEM_SIZE ); //B
            gem1.setPosition(2);
            //middle down 1
            gem2.getImageView().setLayoutY(gem2.getImageView().getLayoutY() + GEM_SIZE);        //M
            gem2.setPosition(1);
            //top to top
            gem3.getImageView().setLayoutY(gem3.getImageView().getLayoutY() - (GEM_SIZE*2));                  //T
            gem3.setPosition(0);
        }

        //bottom is at the top
        else
        {
            //bottom down 1`
            gem1.getImageView().setLayoutY(gem1.getImageView().getLayoutY() + GEM_SIZE); //B
            gem1.setPosition(1);
            //top down 1
            gem3.getImageView().setLayoutY(gem3.getImageView().getLayoutY() + GEM_SIZE);  //M
            gem3.setPosition(0);
            //middle to top
            gem2.getImageView().setLayoutY(gem2.getImageView().getLayoutY() - (GEM_SIZE*2));                //T
            gem2.setPosition(2);
        }
    }

    /**
     * A method to call the game over screen
     */
    private void gameOver() {
        sidePanel.getChildren().remove(sidePanel.getGemView());
        gameOverPane = new GameOverPane(GEM_SIZE, BOARD_WIDTH, BOARD_HEIGHT, highScorePane, currentScore, () -> {
            newGame();
            return null;
        });

        // if our score belongs in the top ten high scores, get the initials screen
        if (highScorePane.checkScore(currentScore)) {
            root.setLeft(gameOverPane.getHighScoreGameOverScreen());
            onHighScore = true;
        } else {
            root.setLeft(gameOverPane.getGameOverScreen());
        }

        // stop the music and the timer
        player.stop();
        gameTimer.stop();
    }

    /**
     * A method to setup the high scores screen
     */
    private void highScoresScreen() {
        HighScorePane highScorePane = new HighScorePane();
        GridPane scoresPane = highScorePane.getGridPane();
        sidePanel = new SidePanel();
        scoresPane.setMinSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);
        root.setLeft(scoresPane);
        root.setCenter(sidePanel);
    }

    /**
     * A method to set up the start up screen
     */
    private void startScreen() {
        // set christmas theme
        isChristmasTheme = settingsPane.getChristmasTheme();

        // stop the player if it was running
        if (player != null) {
            player.stop();
        }
        gameTimer.stop();

        // setup the UI
        board = new Board();
        pane = new Pane();
        sidePanel = new SidePanel();
        root.setCenter(sidePanel);
        Label title;

        // set the styles
        if(isChristmasTheme) {
            title = new Label("GEM XMAS");
            title.getStyleClass().add("christmas-heading");
        } else {
            title = new Label("GEM JAM");
            title.getStyleClass().add("heading");
        }

        pane.setPrefSize(BOARD_WIDTH * GEM_SIZE, BOARD_HEIGHT * GEM_SIZE);
        pane.getStyleClass().add("game-over");

        title.setLayoutX(GEM_SIZE * 2);
        title.setLayoutY(GEM_SIZE * 4);
        title.setWrapText(true);

        // set up the buttons
        Button playButton = new Button("Play", () -> {
            newGame();
            return null;
        }, GEM_SIZE * 2 - 15, GEM_SIZE * 7);

        Button settingsButton = new Button("Settings", () -> {
            settingsScreen();
            return null;
        }, GEM_SIZE * 2 - 15, GEM_SIZE * 8);

        Button highScoresButton = new Button("Scores", () -> {
            highScoresScreen();
            return null;
        }, GEM_SIZE * 2 - 15, GEM_SIZE * 9);

        // set the elements to the scene
        pane.getChildren().addAll(title, playButton.hbox, settingsButton.hbox, highScoresButton.hbox);
        root.setLeft(pane);
    }

    /**
     * A method to set the settings screen
     */
    private void settingsScreen() {
        root.setLeft(settingsPane.getGridPane());
    }
}