package gemjam;

import java.util.List;

/**
 * A class to model the results of the high scores JSON data
 */
public class Results {
    List<HighScores> highscores;

    @Override
    public String toString() {
        return "Results{" +
                "highScoresList=" + highscores +
                '}';
    }
}

