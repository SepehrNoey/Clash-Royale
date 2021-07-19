package server_side.model;

import java.util.TimerTask;

public class ElixirUpdaterForBot extends TimerTask {
    private Bot bot;
    public ElixirUpdaterForBot(Bot bot)
    {
        this.bot = bot;
    }

    @Override
    public void run(){
        if (bot.getElixir() == 10)
            bot.updateElixir(0);
        else
            bot.updateElixir(1);

    }

}
