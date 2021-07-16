package client_side.view.render;

import javafx.application.Platform;

import java.util.TimerTask;

public class TaskForElixir extends TimerTask {

    private ElixirUpdater elixirUpdater;
    public TaskForElixir(ElixirUpdater elixirUpdater){
        this.elixirUpdater = elixirUpdater;
    }

    @Override
    public void run(){
        Platform.runLater(elixirUpdater);
    }
}
