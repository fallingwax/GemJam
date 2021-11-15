package gemjam;

import javafx.animation.AnimationTimer;

/**
 * The Game timer. By extending the AnimationTimer we can tweek the handle method to change the pacing of the game. AnimationTimer normally runs at 60 fps
 */
public abstract class GameTimer extends AnimationTimer {

    //delay in nano seconds
    private long sleepNanoSec = 0;

    //the initial delay rate
    private long initialSleep;

    long prevTime = 0;

    /**
     * Constructor
     * @param sleepMs the time delay in milliseconds
     */
    public GameTimer( long sleepMs) {

        this.sleepNanoSec = sleepMs * 1_000_000;
        initialSleep = sleepNanoSec;
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

    /**
     * Method is overriden in the Main class
     */
    public void game() {

    }

    /**
     * A method to increase the game play speed
     * @param sleepMs the time delay in milliseconds
     */
    public void increaseSpeed(long sleepMs) {

        this.sleepNanoSec -= sleepMs * 1_000_000;
    }

    /**
     * A method to reset the game speed to the initial rate
     */
    public void resetSpeed() {

        this.sleepNanoSec = initialSleep;
    }

}
