import java.util.Timer;
import java.util.TimerTask;

public class ModelTimer {
    private int timeDone;
    private Timer timer;
    private TimerTask task;

    public ModelTimer() {
        this.timeDone = 0;
        this.timer = new Timer();
    }

    public void startTimer() {
        this.task = new TimerTask() {
            @Override
            public void run() { timeDone++; }
        };
        // Schedule the task to run every second (1000 milliseconds)
        this.timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void stopTimer() {
        if (task != null) {
            task.cancel();
            timer.purge(); // Removes cancelled tasks from the timer's task queue
        }
    }

    public void resetTimer() {
        stopTimer(); // Stop the current timer task
        this.timeDone = 0;
    }

    public void incrementTime() { timeDone++; }

    public int getTime() {
        return timeDone;
    }
}
