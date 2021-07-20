package client_side.view.render;

import client_side.controller.GameSceneController;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.card.Card;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;


public class Render implements Runnable{
    private Board board;
    private String me;
    private int cardUsedNum;
    private GameSceneController controller;
    private ArrayBlockingQueue<Card> renderQueue;
    private HashMap<String , ImageView> cardsImgViews;

    public Render(String humanPlayer , Board board , GameSceneController controller){
        cardUsedNum = 0;
        this.me = humanPlayer;
        this.board = board;
        this.controller = controller;
        this.renderQueue = new ArrayBlockingQueue<>(50);
        board.setRenderQueue(renderQueue);
        cardsImgViews = new HashMap<>();
    }

    public void addForRender(Card card){
        try {
            renderQueue.put(card);

        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void gameFinishedMsg(Message msg) // on javaFX thread
    {

    }

    public void delete(Card card){
        Platform.runLater(() -> {
            ImageView imageView = cardsImgViews.get(card.getId());
            imageView.setImage(null);
            controller.getPane().getChildren().remove(imageView);
            cardsImgViews.remove(card.getId());
        });
    }


    @Override
    public void run(){
        Card updated = null;
        while (true)
        {
            try {
                updated = renderQueue.take();
                System.out.println("Card got in render!");
                if (cardsImgViews.containsKey(updated.getId()))
                {
                    System.out.println("the card exists . updating x and y in render.");
                    Card finalUpdated = updated;
                    Platform.runLater(() -> {
                        ImageView imageView = cardsImgViews.get(finalUpdated.getId());
                        imageView.setTranslateX(finalUpdated.getCoordinates().getX() * 30);
                        imageView.setTranslateY(finalUpdated.getCoordinates().getY() * 30);
                    });
                }
                else {
                    Card finalUpdated1 = updated;
                    Platform.runLater(() -> {
                        System.out.println("Didn't exist . new image");
                        ImageView imageView = new ImageView(finalUpdated1.getAttackFrm());
                        cardsImgViews.put(finalUpdated1.getId(),imageView);
                        controller.getPane().getChildren().add(imageView);
                        imageView.setTranslateX(finalUpdated1.getCoordinates().getX() * 30);
                        imageView.setTranslateY(finalUpdated1.getCoordinates().getY() * 30);
                        imageView.setFitWidth(30);
                        imageView.setFitHeight(30);


                    });
                }

            }catch (InterruptedException e)
            {

            }
        }
    }
}
