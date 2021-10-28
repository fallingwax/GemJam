package sample;

import javafx.animation.AnimationTimer;

public abstract class GameTimer extends AnimationTimer {
    private long sleepNanoSec = 0;

    long prevTime = 0;

    public GameTimer( long sleepMs) {
        this.sleepNanoSec = sleepMs * 1_000_000;
    }
    @Override
    public void handle(long now) {

        // some delay
        if ((now - prevTime) < sleepNanoSec) {
            return;
        }

        prevTime = now;
        game();
    }

    public void game() {

    }

    public void increaseSpeed(long sleepMs) {
        this.sleepNanoSec -= sleepMs * 1_000_000;
    }
}
