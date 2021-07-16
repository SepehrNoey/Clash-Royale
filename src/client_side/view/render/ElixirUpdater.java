package client_side.view.render;

import client_side.manager.Manager;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class ElixirUpdater implements Runnable {
    private Integer nowY;
    private int nowX;
    private int barLength;
    private ArrayList<ArrayList<ImageView>> barImageViews;
    private Timer itsTimer;
    private ArrayList<ImageView> currentBarImgViews;
    private int currentElixir;
    private Pane barsPane;
    private Manager manager;
    private int repeatNum;
    private TaskForElixir itsTask;

    public ElixirUpdater(Manager manager , Timer itsTimer , int barLength,int currentElixir , Pane barsPane) {
        this.manager = manager;
        this.itsTimer = itsTimer;
        this.barsPane = barsPane;
        this.barImageViews = new ArrayList<ArrayList<ImageView>>();
        this.barLength = barLength;  // length is 83 + 1 + 1 (dark banners)
        nowX = 1; // starting points for animation
        nowY = (int) barsPane.getChildren().get(10 - currentElixir).getLayoutY();
        this.currentElixir = currentElixir;
        repeatNum = 0;

    }
    public void setItsTask(TaskForElixir itsTask)
    {
        this.itsTask = itsTask;
    }



    @Override
    public void run(){
//        synchronized (this) {
            ImageView next = new ImageView();
            next.setImage(new Image("/client_side/view/pics/barFullSmall.png"));
            next.setFitWidth(41.5);
            if (currentBarImgViews == null) // first bar creating
            {
                currentBarImgViews = new ArrayList<>();
                barImageViews.add(currentBarImgViews);
            }
            if ((barImageViews.size() == 10 && currentBarImgViews.size() < barLength) || barImageViews.size() < 10) {
                barsPane.getChildren().add(next);
                next.setTranslateX(nowX);
                next.setTranslateY(nowY);
                nowY--;
                repeatNum++;
                if (repeatNum == barLength)  // may have bug here !!! because of double or int
                {
                    currentElixir++;
                    repeatNum = 0;
                    manager.updateElixir(currentElixir);
                    System.out.println("el: " + currentElixir);
                    if (currentElixir == 10)
                    {
                        itsTimer.cancel();
                        itsTimer.purge(); // may have bug here
                    }
                    else{
                        currentBarImgViews = new ArrayList<>();
                        barImageViews.add(currentBarImgViews);
                        nowY--;
                    }
                }
            }
//            notifyAll();
//        }
    }

    public void decrease(int elixir){
        if (currentElixir != 10){ // when elixir is 10 , timer is stopped
            itsTimer.cancel();
            itsTimer.purge();
        }
        currentElixir -= elixir;
        nowY = nowY + elixir * 85;
        for (ArrayList<ImageView> thisArrayList : barImageViews) {
            Iterator<ImageView> imgItr = thisArrayList.iterator(); // may have bug here
            while (imgItr.hasNext()) {
                ImageView imageView = imgItr.next();
                imageView.setImage(null);
                barsPane.getChildren().remove(imageView);
                imgItr.remove();
            }
        }
        barImageViews = new ArrayList<ArrayList<ImageView>>();
        currentBarImgViews = null; // starting again
        fastUpdate(nowY);  // may have bug here
        itsTimer.schedule(itsTask,0,27);
    }

    public void fastUpdate(int topMostY){
        for (int i = (int)(barsPane.getChildren().get(9).getLayoutY() + 85) ; i >= topMostY ; i-- )
        {
            Platform.runLater(this);
        }
    }


}
