package gemjam;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 * A class to set a custom animation for the Gems when they are destroyed
 */
public class GemTransition {
    TranslateTransition tt;
    FadeTransition fd;
    ParallelTransition pt;

    /**
     * Constructor
     * @param gem the current game piece
     * @param gemSize the size of the game piece
     */
    public GemTransition(Gem gem, int gemSize) {
        tt = new TranslateTransition();
        fd = new FadeTransition();

        //translate moves the gems up three spaces, while slowly fading away
        tt.setToY(-(gemSize * 3));
        tt.setDuration(Duration.millis(500));
        tt.setCycleCount(1);
        tt.setNode(gem.getImageView());
        fd.setFromValue(10);
        fd.setToValue(0);
        fd.setDuration(Duration.millis(500));
        fd.setNode(gem.getImageView());
        fd.setCycleCount(1);
        setPt();
    }

    /**
     * A method to set the elements in the ParallelTransition
     */
    private void setPt() {
        pt = new ParallelTransition();
        pt.getChildren().addAll(tt, fd);
    }

    /**
     * A method to return the parallel transition
     * @return ParallelTransition
     */
    public ParallelTransition getPt() {
        return pt;
    }
}
