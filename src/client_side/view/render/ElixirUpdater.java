package client_side.view.render;

import client_side.manager.Manager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Timer;

public class ElixirUpdater implements Runnable {
    private Integer nowY;
    private int nowX;
    private int barLength;
    private ArrayList<ImageView> barImageViews;
    private Timer itsTimer;
    private int currentElixir;
    private Pane barsPane;
    private Manager manager;
    private int repeatNum;
    private TaskForElixir itsTask;

    public ElixirUpdater(Manager manager , Timer itsTimer , int barLength , Pane barsPane) {
        this.manager = manager;
        this.itsTimer = itsTimer;
        this.barsPane = barsPane;
        this.barImageViews = new ArrayList<>();
        this.barLength = barLength;  // length is 83 + 1 + 1 (dark banners)
        nowX = 1; // starting points for animation
        nowY = (int) barsPane.getChildren().get(9).getLayoutY() + 85;
        this.currentElixir = 0;
        repeatNum = 0;

    }
    public void setItsTask(TaskForElixir itsTask)
    {
        this.itsTask = itsTask;
    }

    @Override
    public void run(){
            ImageView next = new ImageView();
            next.setImage(new Image("/client_side/view/pics/barFullSmall.png"));
            next.setFitWidth(41.5);
            if (barImageViews.size() < 10 * 85) {
                barsPane.getChildren().add(next);
                System.out.println("added " + next + "with nowY: " + nowY);
                next.setTranslateX(nowX);
                next.setTranslateY(nowY);
                barImageViews.add(next);
                synchronized (nowY){
                    nowY--;
                    System.out.println("decreased nowY for next round: " + nowY);
                }
                repeatNum++;
                if (repeatNum == barLength)  // may have bug here !!! because of double or int
                {
                    repeatNum = 0;
                    if (currentElixir < 10)
                    {
                        currentElixir++;
                        manager.updateElixir(currentElixir);
                        System.out.println("el: " + currentElixir);
                        nowY--;
                    }
                }
            }
    }

    public void decrease(int elixir){
        currentElixir -= elixir;
        System.out.println("after decrease: " + currentElixir);
        manager.updateElixir(currentElixir);

        for(int i = 0 ; i < 85 * elixir ;i++)
        {
            ImageView imgView = barImageViews.get(barImageViews.size() -1);
            imgView.setImage(null);
            System.out.println("set null for " + (barImageViews.size() - 1));
            barsPane.getChildren().remove(imgView);
            barImageViews.remove(imgView);
        }
        synchronized (nowY){
            nowY = nowY + 85 * elixir + elixir; // fix here
            System.out.println("increased nowY: " + nowY);
        }
    }

    public void fastUpdate(int topMostY){
        for (int i = (int)(barsPane.getChildren().get(9).getLayoutY() + 85) ; i >= topMostY ; i-- )
        {
            this.run();
        }
    }

}
