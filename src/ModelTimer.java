import java.util.Timer;
import java.util.TimerTask;

/**
 * ModelTimer is a class that represents a timer for the game.
 * It is used to keep track of the time elapsed in the game.
 */
public class ModelTimer {
    private int timeDone;
    private Timer timer;
    private TimerTask task;

    public ModelTimer() {
        this.timeDone = 0;
        this.timer = new Timer();
    }

    /**
     * Starts the timer.
     */
    public void startTimer() {
        this.task = new TimerTask() {
            @Override
            public void run() { timeDone++; }
        };
        this.timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    /**
     * Stops the timer.
     */
    public void stopTimer() {
        if (task != null) {
            task.cancel();
            timer.purge();
        }
    }

    /**
     * Resets the timer.
     */
    public void resetTimer() {
        stopTimer();
        this.timeDone = 0;
    }

    public int getTime() { return timeDone; }
}
