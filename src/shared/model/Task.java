package shared.model;

import javafx.application.Platform;

import java.util.TimerTask;

public class Task extends TimerTask {
    private TimeUpdater updater;
    public Task(TimeUpdater updater)
    {
        this.updater = updater;
    }

    @Override
    public void run() {
        Platform.runLater(updater);
    }
}
