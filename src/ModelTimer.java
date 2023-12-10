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
        this.timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void stopTimer() {
        if (task != null) {
            task.cancel();
            timer.purge();
        }
    }

    public void resetTimer() {
        stopTimer();
        this.timeDone = 0;
    }

    public void incrementTime() { timeDone++; }

    public int getTime() {
        return timeDone;
    }
}
