package client_side.view.render;

import javafx.scene.control.Label;

public class TimeUpdater implements Runnable{
    private int min = 3;
    private int sec = 0;
    private Label timeLabel;
    public TimeUpdater(Label timeLabel)
    {
        this.timeLabel = timeLabel;
    }

    @Override
    public void run() {
        timeLabel.setText(getTime());
    }
    private String getTime(){
        sec--;
        if (min >= 0 && sec >= 0)
        {
            return min + ":" + (sec >= 10 ? sec : "0" + sec);
        }
        else if (min >= 0 && sec < 0)
        {
            sec = 59;
            min--;
            if (min >= 0)
                return min + ":" + sec;
            else
                return "0:00";
        }
        else {
            return "0:00";
        }
    }
}
