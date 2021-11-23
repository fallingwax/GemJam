package gemjam;

import com.google.gson.Gson;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.util.*;

/**
 * A class to set up the High Scores screen
 */
public class HighScorePane {

    //Character array to select initials from
    char[] letters = new char[]{'_','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
    'S','T','U','V','W','X','Y','Z'};
    GridPane gridPane;
    GridPane setInitialsPane;
    List<HighScores> highScoresList;
    Map<String, Object> map = new HashMap<>();
    Results results;
    String jsonFilePath = "./src/res/highscores.json";
    String jsonAbsolutePath = "C:\\GemJam\\highscores.json";
    int i = 0;
    List<Label> initials;
    Label firstInitial;
    Label secondInitial;
    Label thirdInitial;
    int labelCount = 0;

    /**
     * Constructor
     */
    public HighScorePane() {
        this.gridPane = new GridPane();
        setHighScores();
        setGridPane();
        setLabels();
    }

    /**
     * A method to return the GridPane
     * @return GridPane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * A method to set the Grid Pane
     */
    public void setGridPane() {
        Label highScoreText = new Label("High Scores");

        // add class style
        highScoreText.getStyleClass().add("high-scores-heading");
        gridPane.getStyleClass().add("game-over");
        gridPane.add(highScoreText,0,0,2,1);

        // the first row to start adding items to
        int row = 1;

        // loop through the current high scores
        for (HighScores highScore : highScoresList) {
            Label initials = new Label(highScore.initials);
            Label score = new Label("" + highScore.score);
            initials.getStyleClass().add("high-scores-initials");
            score.getStyleClass().add("high-scores");

            gridPane.add(initials,0, row);
            gridPane.add(score,1,row);

            //tick up the row
            row++;
        }
    }

    /**
     * A method to return the high scores list
     * @return List of high scores
     */
    public List<HighScores> getHighScoresList() {
        return highScoresList;
    }

    /**
     * A method to set the High Scores list from JSON file
     */
    public void setHighScores() {

        String protocol = this.getClass().getResource("").getProtocol();
        if(Objects.equals(protocol, "jar")){
            // run in jar
            File file = new File(jsonAbsolutePath);
            try {

                //set up a file read
                Reader reader = new FileReader(file);

                // set the gson object
                Gson gson = new Gson();

                // get the results from the json file
                this.results = gson.fromJson(reader,Results.class);
                this.highScoresList = new ArrayList<>(this.results.highscores);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(Objects.equals(protocol, "file")) {
            // run in ide
            try {

                //set up a file read
                Reader reader = new FileReader(new File(jsonFilePath));

                // set the gson object
                Gson gson = new Gson();

                // get the results from the json file
                this.results = gson.fromJson(reader,Results.class);
                this.highScoresList = new ArrayList<>(this.results.highscores);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * A method to add the new high score to the list
     * @param score the current score
     */
    public void updateHighScores(int score ) {

        // correcting for the blank spaces being added
        String initials = (""+firstInitial.getText()+secondInitial.getText()+thirdInitial.getText()+"").replace(" ", "");
        HighScores insert = null;

        // index of the high score to replace
        int index = -1;

        // loop through the high scores and find the first score that is lower than the current score
        for ( HighScores highScore : this.highScoresList ) {
            index = this.highScoresList.indexOf(highScore);
            if (score > highScore.score) {
                index = this.highScoresList.indexOf(highScore);
                insert = new HighScores(score,initials);
                break;
            }
        }

        // insert the new element
        if (insert != null && index != -1) {
            this.highScoresList.add(index,insert);
        }

        // remove the last element
        if(highScoresList.size() > 10) {
            int lastElement = this.highScoresList.size() - 1;
            this.highScoresList.remove(lastElement);
        }


        String protocol = this.getClass().getResource("").getProtocol();
        if(Objects.equals(protocol, "jar")){
            // run in jar
            try {
                Gson gson = new Gson();
                File file = new File(jsonAbsolutePath);
                Writer writer = new FileWriter(file);
                // map the current high score to
                map.put("highscores", highScoresList);

                // encode the json
                gson.toJson(map,writer);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(Objects.equals(protocol, "file")) {
            // run in ide
            try {
                Gson gson = new Gson();
                Writer writer = new FileWriter("./src/res/highscores.json");
                // map the current high score to
                map.put("highscores", highScoresList);

                // encode the json
                gson.toJson(map,writer);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method to set the labels from the initials
     */
    private void setLabels() {
        initials = new ArrayList<>();
        firstInitial = new Label();
        secondInitial = new Label();
        thirdInitial = new Label();
        initials.add(firstInitial);
        initials.add(secondInitial);
        initials.add(thirdInitial);
    }

    /**
     * A methof to check if the current highscore is in the top 10 scores
     * @param currentScore the current score
     * @return boolean
     */
    public boolean checkScore(int currentScore) {
        boolean newHighScore = false;

        // loop through the highscores and see if we are in the top 10
        for ( HighScores highScore : this.highScoresList ) {
            if (currentScore > highScore.score) {
                newHighScore = true;
                break;
            }
        }
        return newHighScore;
    }

    /**
     * A method to set up and return the new high scores GridPane
     * @return GridPane
     */
    public GridPane newHighScore() {
        setInitialsPane = new GridPane();
        setInitialsPane.setMinSize(240,80);
        setInitialsPane.add(firstInitial,0,0);
        setInitialsPane.add(secondInitial,1,0);
        setInitialsPane.add(thirdInitial,2,0);
        firstInitial.setText("_");
        secondInitial.setText("_");
        thirdInitial.setText("_");
        return setInitialsPane;

    }

    /**
     * A method to return the next label in the initials screen. Move to the right
     * @return Label
     */
    public Label getNextLabel() {
        initials.get(labelCount).getStyleClass().add("yellow");
        labelCount++;
        if(labelCount > 2) {
            return null;
        } else {
            return initials.get(labelCount);
        }
    }

    public Label getFirstInitial() {
        return firstInitial;
    }

    public void setFirstInitial(Label firstInitial) {
        this.firstInitial = firstInitial;
    }

    public Label getSecondInitial() {
        return secondInitial;
    }

    public void setSecondInitial(Label secondInitial) {
        this.secondInitial = secondInitial;
    }

    public Label getThirdInitial() {
        return thirdInitial;
    }

    public void setThirdInitial(Label thirdInitial) {
        this.thirdInitial = thirdInitial;
    }

    /**
     * A method to get the current initials
     * @return Label
     */
    public Label currentInitial() {
        return initials.get(labelCount);
    }

    /**
     * A method to loop through the char array by pressing down
     * @param label the current initial
     */
    public void changeLetterDown(Label label) {
        i++;
        if (i > 26) {
            i = 0;
        }

        if (i < 0) {
            i = 26;
        }
        label.setText(" " + letters[i]);
    }

    /**
     * A method to loop through the char array by pressing up
     * @param label the current initial
     */
    public void changeLetterUp(Label label) {
        i--;
        if (i > 26) {
            i = 0;
        }

        if (i < 0) {
            i = 26;
        }
        label.setText(" " + letters[i]);
    }

}
