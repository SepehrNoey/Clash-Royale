package client_side.view.scenes;

import client_side.manager.Manager;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server_side.database.DBUtil;
import shared.enums.MessageType;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.card.Card;

import java.util.concurrent.ExecutorService;

public class MenuScene {

    private ImageView profile;
    private ImageView deck;
    private ImageView history;
    private ImageView bot1;
    private ImageView bot2;
    private ImageView v1;
    private ImageView v2;
    private ImageView logOut;

    private Image profileBlack;
    private Image profileWhite;
    private Image deckBlack;
    private Image deckWhite;
    private Image historyBlack;
    private Image historyWhite;
    private Image bot1Black;
    private Image bot1White;
    private Image bot2Black;
    private Image bot2White;
    private Image v1Black;
    private Image v1White;
    private Image v2Black;
    private Image v2White;
    private Image logOutBlack;
    private Image logOutWhite;
    private ExecutorService executor;

    public MenuScene(ExecutorService executor)
    {
        this.executor = executor;
    }

    public Group buildScene()
    {
        Group menu = new Group();
        ImageView back = new ImageView(new Image("client_side/view/pics/menuBack2.png"));
        back.setFitWidth(630);
        back.setFitHeight(900);
        menu.getChildren().add(back);


        profileBlack = new Image("client_side/view/pics/profileBlack.png");
        profileWhite = new Image("client_side/view/pics/profileWhite.png");
        profile = new ImageView(profileBlack);
        profile.setFitWidth(130);
        profile.setFitHeight(85);
        profile.setTranslateX(80);
        profile.setTranslateY(100);
        menu.getChildren().add(profile);




        deckBlack = new Image("client_side/view/pics/deckBlack.png");
        deckWhite = new Image("client_side/view/pics/deckWhite.png");
        deck = new ImageView(deckBlack);
        deck.setFitWidth(130);
        deck.setFitHeight(95);
        deck.setTranslateX(260);
        deck.setTranslateY(100);
        menu.getChildren().add(deck);


        historyBlack = new Image("client_side/view/pics/historyBlack.png");
        historyWhite = new Image("client_side/view/pics/historyWhite.png");
        history = new ImageView(historyBlack);
        history.setFitWidth(130);
        history.setFitHeight(95);
        history.setTranslateX(440);
        history.setTranslateY(100);
        menu.getChildren().add(history);



        bot1Black = new Image("client_side/view/pics/bot1Black.png");
        bot1White = new Image("client_side/view/pics/bot1White.png");
        bot1 = new ImageView(bot1Black);
        bot1.setFitWidth(130);
        bot1.setFitHeight(95);
        bot1.setTranslateX(150);
        bot1.setTranslateY(300);
        menu.getChildren().add(bot1);



        bot2Black = new Image("client_side/view/pics/bot2Black.png");
        bot2White = new Image("client_side/view/pics/bot2White.png");
        bot2 = new ImageView(bot2Black);
        bot2.setFitWidth(130);
        bot2.setFitHeight(95);
        bot2.setTranslateX(350);
        bot2.setTranslateY(300);
        menu.getChildren().add(bot2);



        v1Black = new Image("client_side/view/pics/1v1Black.png");
        v1White = new Image("client_side/view/pics/1v1White.png");
        v1 = new ImageView(v1Black);
        v1.setFitWidth(130);
        v1.setFitHeight(95);
        v1.setTranslateX(150);
        v1.setTranslateY(450);
        menu.getChildren().add(v1);




        v2Black = new Image("client_side/view/pics/2v2Black.png");
        v2White = new Image("client_side/view/pics/2v2White.png");
        v2 = new ImageView(v2Black);
        v2.setFitWidth(130);
        v2.setFitHeight(95);
        v2.setTranslateX(350);
        v2.setTranslateY(450);
        menu.getChildren().add(v2);



        logOutBlack = new Image("client_side/view/pics/logOutBlack.png");
        logOutWhite = new Image("client_side/view/pics/logOutWhite.png");
        logOut = new ImageView(logOutBlack);
        logOut.setFitWidth(80);
        logOut.setFitHeight(45);
        logOut.setTranslateX(50);
        logOut.setTranslateY(600);
        menu.getChildren().add(logOut);


        return menu;
    }

    public void enableController(Player player, Scene scene)
    {
        profile.setOnMouseEntered(e->{
            profile.setImage(profileWhite);
            profile.setCursor(Cursor.HAND);
        });
        profile.setOnMouseExited(e->{
            profile.setImage(profileBlack);
        });
        profile.setOnMouseClicked(e->{

            Stage stage = new Stage();
            Group group = new Group();
            ImageView view = new ImageView(new Image("client_side/view/pics/profileFrame.png"));
            view.setFitWidth(200);
            view.setFitHeight(120);
            view.setTranslateX(250);
            view.setTranslateY(30);
            group.getChildren().add(view);
            Label usr = new Label(player.getName());
            Label level = new Label(String.valueOf(player.getLevel()));
            Label xp = new Label(player.getXp()+" XP");
            usr.setTranslateX(275);
            usr.setTranslateY(43);
            usr.setFont(new Font(17));
            level.setTranslateX(415);
            level.setTranslateY(45);
            xp.setTranslateX(300);
            xp.setTranslateY(110);
            xp.setFont(new Font(20));
            group.getChildren().add(usr);
            group.getChildren().add(level);
            group.getChildren().add(xp);


            ImageView[] frames = new ImageView[12];
            for (int i=0; i<12;i++)
                frames[i] = new ImageView(player.getCards().get(i).getCardImage());
            double gap = 30;
            for(int i=0;i<6;i++)
            {
                frames[i].setFitWidth(103);
                frames[i].setFitHeight(200);
                frames[i].setTranslateX(gap+(103*i));
                frames[i].setTranslateY(200);
                gap+=3.6;
                group.getChildren().add(frames[i]);
            }
            gap = 30;
            for (int i=0;i<6;i++)
            {
                frames[i+6].setFitWidth(103);
                frames[i+6].setFitHeight(200);
                frames[i+6].setTranslateX(gap+(103*i));
                frames[i+6].setTranslateY(450);
                gap+=3.6;
                group.getChildren().add(frames[i+6]);
            }
            // put cards info on above frames by iterating over player's card
            Label[] cardsLevel = new Label[12];
            double lblGap = 53;
            for(int i=0; i<6;i++)
            {
                cardsLevel[i] = new Label(player.getCards().get(i).getLevel()+"");
                cardsLevel[i].setTranslateX(lblGap+(i*83.6));
                cardsLevel[i].setTranslateY(360);
                lblGap+=23;
                group.getChildren().add(cardsLevel[i]);
            }


            lblGap = 53;
            for(int i=0; i<6;i++)
            {
                cardsLevel[i+6] = new Label(player.getCards().get(i+6).getLevel()+"");
                cardsLevel[i+6].setTranslateX(lblGap+(i*83.6));
                cardsLevel[i+6].setTranslateY(610);
                lblGap+=23;
                group.getChildren().add(cardsLevel[i+6]);
            }

            Scene newScene = new Scene(group,700,680, Color.ORANGE);

            stage.setScene(newScene);
            stage.setTitle("Profile");
            stage.show();

        });

        deck.setOnMouseEntered(e->{
            deck.setImage(deckWhite);
            deck.setCursor(Cursor.HAND);
        });
        deck.setOnMouseExited(e->{
            deck.setImage(deckBlack);
        });
        deck.setOnMouseClicked(e->{

            Stage stage = new Stage();
            Group group = new Group();
            ImageView[] deckView = new ImageView[8];
            ImageView[] otherView = new ImageView[8];
            double gap = 50;
            for(int i=0; i<8;i++)
            {
                deckView[i] = new ImageView(player.getCards().get(i).getCardImage());
                deckView[i].setFitWidth(70);
                deckView[i].setFitHeight(140);
                deckView[i].setTranslateX(gap+(i*70));
                deckView[i].setTranslateY(50);
                gap+=5;
                group.getChildren().add(deckView[i]);
            }
            gap = 50;
            for (int i=8; i<16;i++)
            {
                if(i<=11)
                    otherView[i-8] = new ImageView(player.getCards().get(i).getCardImage());
                else
                    otherView[i-8] = new ImageView();
                otherView[i-8].setFitWidth(70);
                otherView[i-8].setFitHeight(140);
                otherView[i-8].setTranslateX(gap+((i-8)*70));
                otherView[i-8].setTranslateY(240);
                gap+=5;
                group.getChildren().add(otherView[i-8]);
            }


            deckView[0].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[0].getImage());
                        deckView[0].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[1].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[1].getImage());
                        deckView[1].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[2].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[2].getImage());
                        deckView[2].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[3].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[3].getImage());
                        deckView[3].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[4].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[4].getImage());
                        deckView[4].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[5].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[5].getImage());
                        deckView[5].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[6].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[6].getImage());
                        deckView[6].imageProperty().set(null);
                        break;
                    }
                }
            });
            deckView[7].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if (otherView[j].getImage()==null)
                    {
                        otherView[j].setImage(deckView[7].getImage());
                        deckView[7].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[0].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[0].getImage());
                        otherView[0].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[1].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[1].getImage());
                        otherView[1].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[2].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[2].getImage());
                        otherView[2].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[3].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[3].getImage());
                        otherView[3].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[4].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[4].getImage());
                        otherView[4].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[5].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[5].getImage());
                        otherView[5].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[6].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[6].getImage());
                        otherView[6].imageProperty().set(null);
                        break;
                    }
                }
            });

            otherView[7].setOnMouseClicked(ev->{
                for(int j=0;j<8;j++)
                {
                    if(deckView[j].getImage()==null)
                    {
                        deckView[j].setImage(otherView[7].getImage());
                        otherView[7].imageProperty().set(null);
                        break;
                    }
                }
            });

            Image finishBlack = new Image("client_side/view/pics/finishBlack.png");
            Image finishWhite = new Image("client_side/view/pics/finishWhite.png");
            ImageView finish = new ImageView(finishBlack);
            finish.setFitWidth(80);
            finish.setFitHeight(45);
            finish.setTranslateX(690);
            finish.setTranslateY(450);
            group.getChildren().add(finish);

            finish.setOnMouseEntered(ev->{
                finish.setImage(finishWhite);
                finish.setCursor(Cursor.HAND);
            });
            finish.setOnMouseExited(ev->{
                finish.setImage(finishBlack);
            });

            finish.setOnMouseClicked(ev->{
                boolean full = true;
                for (int i=0; i<8; i++)
                {
                    if (deckView[i].getImage()==null)
                    {
                        full = false;
                        break;
                    }
                }
                if(full)
                {
                    player.setBattleDeck(deckView);
                    DBUtil dbUtil = new DBUtil();
                    dbUtil.UpdateCards(player);
                    stage.close();
                }

            });

            Scene newScene = new Scene(group,800,530);
            stage.setScene(newScene);
            stage.setTitle("Battle Deck");
            stage.show();

        });

        history.setOnMouseEntered(e->{
            history.setImage(historyWhite);
            history.setCursor(Cursor.HAND);
        });
        history.setOnMouseExited(e->{
            history.setImage(historyBlack);
        });
        history.setOnMouseClicked(e->{
            Message historyReq = new Message(MessageType.BATTLE_HISTORY, player.getName(),"");
            player.getSender().sendMsg(historyReq);
            /*Message history = player.getGetter().getMsg();*/
            Message history = null;
            try {
                history = player.getSharedInbox().take();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            String[] splitHistory = history.getContent().split(" ");

            Stage stage = new Stage();
            Group group = new Group();
            Label[] labels = new Label[splitHistory.length/2];
            StringBuilder sb = new StringBuilder();
            int j = -1;
            for(int i=0;i< splitHistory.length;i++)
            {
                j++;
                sb.append("opponent  :  ");
                sb.append(splitHistory[i]);
                sb.append("    winner  :  ");
                sb.append(splitHistory[i+1]);
                labels[j] = new Label(sb.toString());
                group.getChildren().add(labels[j]);
                labels[j].setFont(new Font(14));
                labels[j].setTranslateX(50);
                labels[j].setTranslateY((i++)*20);
                sb.setLength(0);
            }
            stage.setScene(new Scene(group,400,200,Color.YELLOW));
            stage.setTitle("Battle Histories");
            stage.setHeight(900);
            stage.setWidth(630);
            stage.show();
        });

        bot1.setOnMouseEntered(e->{
            bot1.setImage(bot1White);
            bot1.setCursor(Cursor.HAND);
        });
        bot1.setOnMouseExited(e->{
            bot1.setImage(bot1Black);
        });
        bot1.setOnMouseClicked(e->{
            player.getSender().sendMsg(new Message(MessageType.GAME_MODE , player.getName(),"bot1"));
            Manager manager = new Manager(player , "bot1","bot1",scene , executor);
            manager.init();
        });


        bot2.setOnMouseEntered(e->{
            bot2.setImage(bot2White);
            bot2.setCursor(Cursor.HAND);
        });
        bot2.setOnMouseExited(e->{
            bot2.setImage(bot2Black);
        });
        bot2.setOnMouseClicked(e->{
            player.getSender().sendMsg(new Message(MessageType.GAME_MODE , player.getName(),"bot2"));
            Manager manager = new Manager(player , "bot2","bot2",scene,executor);
            manager.init();
        });

        v1.setOnMouseEntered(e->{
            v1.setImage(v1White);
            v1.setCursor(Cursor.HAND);
        });
        v1.setOnMouseExited(e->{
            v1.setImage(v1Black);
        });
        v1.setOnMouseClicked(e->{
            player.getSender().sendMsg(new Message(MessageType.GAME_MODE , player.getName(),"1v1"));
            Manager manager = new Manager(player , "1v1","1v1",scene,executor);
            manager.init();
        });

        v2.setOnMouseEntered(e->{
            v2.setImage(v2White);
            v2.setCursor(Cursor.HAND);
        });
        v2.setOnMouseExited(e->{
            v2.setImage(v2Black);
        });
        v2.setOnMouseClicked(e->{
            player.getSender().sendMsg(new Message(MessageType.GAME_MODE , player.getName(),"2v2"));
            Manager manager = new Manager(player , "2v2","2v2",scene,executor);
            manager.init();
        });

        logOut.setOnMouseEntered(e->{
            logOut.setImage(logOutWhite);
            logOut.setCursor(Cursor.HAND);
        });
        logOut.setOnMouseExited(e->{
            logOut.setImage(logOutBlack);
        });
        logOut.setOnMouseClicked(e->{
            player.getSender().sendMsg(new Message(MessageType.LOG_OUT,"player",""));
            System.exit(1);
        });
    }

}