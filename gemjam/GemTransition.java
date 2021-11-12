package gemjam;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class GemTransition {
    TranslateTransition tt;
    FadeTransition fd;
    ParallelTransition pt;

    public GemTransition(Gem gem, int gemSize) {
        tt = new TranslateTransition();
        fd = new FadeTransition();

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

    private void setPt() {
        pt = new ParallelTransition();
        pt.getChildren().addAll(tt, fd);
    }

    public ParallelTransition getPt() {
        return pt;
    }
}
