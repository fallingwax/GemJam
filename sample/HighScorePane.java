package sample;

import com.google.gson.Gson;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.util.*;

public class HighScorePane {

    char[] letters = new char[]{'_','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
    'S','T','U','V','W','X','Y','Z'};
    GridPane gridPane;
    GridPane setInitialsPane;
    List<HighScores> highScoresList;
    Map<String, Object> map = new HashMap<>();
    Results results;
    String jsonFilePath = "./src/res/highscores.json";
    int i = 0;
    List<Label> initials;
    Label firstInitial;
    Label secondInitial;
    Label thirdInitial;
    int labelCount = 0;


    public HighScorePane() {
        this.gridPane = new GridPane();
        setHighScores();
        setGridPane();
        setLabels();
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane() {
        Label highScoreText = new Label("High Scores");
        highScoreText.getStyleClass().add("high-scores-heading");
        gridPane.getStyleClass().add("game-over");
        gridPane.add(highScoreText,0,0,2,1);
        int row = 1;
        for (HighScores highScore : highScoresList) {
            Label initials = new Label(highScore.initials);
            Label score = new Label("" + highScore.score);
            initials.getStyleClass().add("high-scores-initials");
            score.getStyleClass().add("high-scores");

            gridPane.add(initials,0, row);
            gridPane.add(score,1,row);
            row++;
        }
    }

    public List<HighScores> getHighScoresList() {
        return highScoresList;
    }

    public void setHighScores() {
        File json = new File(jsonFilePath);
        try {
            Reader reader = new FileReader(json);
            Gson gson = new Gson();
            this.results = gson.fromJson(reader,Results.class);
            this.highScoresList = new ArrayList<>(this.results.highscores);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateHighScores(int score ) {
        String initials = (""+firstInitial.getText()+secondInitial.getText()+thirdInitial.getText()+"").replace(" ", "");
        HighScores insert = null;
        int index = -1;
        for ( HighScores highScore : this.highScoresList ) {
            index = this.highScoresList.indexOf(highScore);
            if (score > highScore.score) {
                index = this.highScoresList.indexOf(highScore);
                System.out.println(index);
                insert = new HighScores(score,initials);
                break;
            }
        }

        if (insert != null && index != -1) {
            this.highScoresList.add(index,insert);
        }

        if(highScoresList.size() > 10) {
            int lastElement = this.highScoresList.size() - 1;
            this.highScoresList.remove(lastElement);
        }

        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(jsonFilePath);
            map.put("highscores", highScoresList);
            gson.toJson(map,writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setLabels() {
        initials = new ArrayList<>();
        firstInitial = new Label();
        secondInitial = new Label();
        thirdInitial = new Label();
        initials.add(firstInitial);
        initials.add(secondInitial);
        initials.add(thirdInitial);
    }

    public boolean checkScore(int currentScore) {
        boolean newHighScore = false;
        for ( HighScores highScore : this.highScoresList ) {
            if (currentScore > highScore.score) {
                newHighScore = true;
                break;
            }
        }
        return newHighScore;
    }

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

    public Label getNextLabel() {
        labelCount++;
        if(labelCount > 2) {
            labelCount = 0;
        }
        i = 0;
        return initials.get(labelCount);
    }

    public Label getPreviousLabel() {
        labelCount--;
        if(labelCount < 0) {
            labelCount = 2;
        }
        i = 0;
        return initials.get(labelCount);
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

    public Label currentInitial() {
        return initials.get(labelCount);
    }

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
