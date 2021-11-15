package gemjam;
/*
    class HighScore
    model of JSON results
 */
public class HighScores {
    int score;
    String initials;

    /**
     * Constructor
     * @param score high score
     * @param initials player initials
     */
    public HighScores(int score,String initials) {
        this.score = score;
        this.initials = initials;
    }
}
