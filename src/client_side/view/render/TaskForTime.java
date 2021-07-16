package client_side.view.render;

import javafx.application.Platform;

import java.util.TimerTask;

public class TaskForTime extends TimerTask {
    private TimeUpdater updater;
    public TaskForTime(TimeUpdater updater)
    {
        this.updater = updater;
    }

    @Override
    public void run() {
        Platform.runLater(updater);
    }
}
