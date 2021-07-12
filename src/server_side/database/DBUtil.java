package server_side.database;

import javafx.geometry.Point2D;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;
import shared.model.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class DBUtil {

    private static Connection connection;
    private static Statement statement;

    {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Clash Royale"
                    ,"root","");
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Player getPlayer(String usr, String password, Socket socket, ObjectOutputStream outObj,
                                   ObjectInputStream inObj)
    {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players");
            while (resultSet.next())
            {
                if (resultSet.getString("username").equals(usr) && resultSet.getString("password").equals(password))
                {
                    ArrayList<Card> cards = new ArrayList<>();
                    Card card;
                    String tableName = usr +" cards";
                    ResultSet cardsSet = connection.createStatement().executeQuery("SELECT * FROM "+tableName);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),5,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/barbarian_walk0.png","client_side/view/pics/barbarian_attack",
                            7,40,40,0, TargetTypes.GROUND,4,false,new Point2D(0,0),usr, SpeedTypes.MEDIUM,1.5
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/barbarian_walk",6,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),3,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/archer_walk0.png","client_side/view/pics/archer_attack",
                            3,40,40,5, TargetTypes.AIR_GROUND,2,false,new Point2D(0,0),usr, SpeedTypes.MEDIUM,1.2
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/archer_walk",20,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),4,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/heli_walk0.png","client_side/view/pics/heli_walk",
                            1,100,100,3, TargetTypes.AIR_GROUND,1,true,new Point2D(0,0),usr, SpeedTypes.FAST,1.8
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/heli_walk",1,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),5,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/wizard_walk0.png","client_side/view/pics/wizard_shot",
                            1,30,30,5, TargetTypes.AIR_GROUND,1,true, new Point2D(0,0),usr,SpeedTypes.MEDIUM,1.7
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/wizard_walk",1,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),4,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/pekka_walk0.png","client_side/view/pics/pekka_attack",
                            7,35,35,0, TargetTypes.GROUND,1,false,new Point2D(0,0),usr, SpeedTypes.FAST,1.8
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/pekka_walk",6,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),5,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/giant_walking0.png","client_side/view/pics/giant_attack",
                            5,60,50,0, TargetTypes.BUILDINGS,1,false,new Point2D(0,0),usr, SpeedTypes.SLOW,1.5
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/giant_walking",4,"",0);
                    cards.add(card);

                    cardsSet.next();
                    card = new SoldierCard(CardTypes.valueOf(cardsSet.getString("type")),4,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/valkyrie_walk3.png","client_side/view/pics/valkyrie_attack",
                            6,40,50,0, TargetTypes.GROUND,1,true,new Point2D(0,0),usr, SpeedTypes.MEDIUM,1.5
                            ,Integer.parseInt(cardsSet.getString("hp")),"client_side/view/pics/valkyrie_walk",8,"client_side/view/pics/valkyrie_die",1);
                    cards.add(card);

                    cardsSet.next();
                    card = new SpellCard(CardTypes.valueOf(cardsSet.getString("type")),3,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/rage.png","",
                            0,30,45,5,null,1,false,new Point2D(0,0),usr,null
                            ,40,40,40,false);
                    cards.add(card);

                    // reduce damage?
                    cardsSet.next();
                    card = new SpellCard(CardTypes.valueOf(cardsSet.getString("type")),4,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/fireball4.png","client_side/view/pics/fireball",
                            6,80,200,2.5,null,1,false,new Point2D(0,0),usr,null
                            ,0,0,0,true);
                    cards.add(card);

                    cardsSet.next();
                    card = new SpellCard(CardTypes.valueOf(cardsSet.getString("type")),3,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/arrow0.png","client_side/view/pics/arrow",
                            1,30,80,4,null,1,false,new Point2D(0,0),usr,null
                            ,0,0,0,true);
                    cards.add(card);

                    cardsSet.next();
                    card = new BuildingCard(CardTypes.valueOf(cardsSet.getString("type")),6,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/cannon_walk0.png","client_side/view/pics/cannon_walk",
                            1,50,50,5.5,TargetTypes.GROUND,1,false,new Point2D(0,0),usr,"",0,
                            0.8,30,Integer.parseInt(cardsSet.getString("hp")));
                    cards.add(card);

                    cardsSet.next();
                    card = new BuildingCard(CardTypes.valueOf(cardsSet.getString("type")),5,Integer.parseInt(cardsSet.getString("damage"))
                            ,Integer.parseInt(cardsSet.getString("level")),"client_side/view/pics/inferno_attack0.png","client_side/view/pics/inferno_attack",
                            1,45,45,6,TargetTypes.AIR_GROUND,1,false,new Point2D(0,0),usr,"",0,
                            0.4,40,Integer.parseInt(cardsSet.getString("hp")));
                    cards.add(card);

                    return new Player(usr,password,Integer.parseInt(resultSet.getString("level")),Integer.parseInt(resultSet.getString("xp")),
                            cards,socket,outObj,inObj);

                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Player register(String usr, String password, Socket socket, ObjectOutputStream outObj,
                                  ObjectInputStream inObj)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("('");sb.append(usr);sb.append("','");sb.append(password);sb.append("',1,0)");
        try {
            statement.executeUpdate("INSERT INTO players (username,password,level,xp) VALUES "+sb.toString());

            String tableName = usr+" cards";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(type VARCHAR(255), level INT, damage INT, hp INT, PRIMARY KEY(type))");

            ArrayList<Card> cards = new ArrayList<>();
            Card card;
            Statement cardSt = connection.createStatement();
            cardSt.executeUpdate(sb.toString());
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.BARBARIAN',1,75,300)");
            card = new SoldierCard(CardTypes.BARBARIAN,5,75,1,"client_side/view/pics/barbarian_walk0.png","client_side/view/pics/barbarian_attack",
                    7,40,40,0, TargetTypes.GROUND,4,
                    false,new Point2D(0,0),usr, SpeedTypes.MEDIUM,1.5,300,"client_side/view/pics/barbarian_walk",6,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.ARCHER',1,33,125)");
            card = new SoldierCard(CardTypes.ARCHER,3,33,1,"client_side/view/pics/archer_walk0.png","client_side/view/pics/archer_attack",
                    3,40,40,5, TargetTypes.AIR_GROUND,2,
                    false, new Point2D(0,0),usr,SpeedTypes.MEDIUM,1.2,125,"client_side/view/pics/archer_walk",20,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.BABY_DRAGON',1,100,800)");
            card = new SoldierCard(CardTypes.BABY_DRAGON,4,100,1,"client_side/view/pics/heli_walk0.png","client_side/view/pics/heli_walk",
                    1,100,100,3, TargetTypes.AIR_GROUND,1,
                    true,new Point2D(0,0),usr, SpeedTypes.FAST,1.8,800,"client_side/view/pics/heli_walk",1,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.WIZARD',1,130,340)");
            card = new SoldierCard(CardTypes.WIZARD,5,130,1,"client_side/view/pics/wizard_walk0.png","client_side/view/pics/wizard_shot",
                    1,30,30,5, TargetTypes.AIR_GROUND,1,
                    true, new Point2D(0,0),usr,SpeedTypes.MEDIUM,1.7,340,"client_side/view/pics/wizard_walk",1,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.MINI_PEKKA',1,325,600)");
            card = new SoldierCard(CardTypes.MINI_PEKKA,4,325,1,"client_side/view/pics/pekka_walk0.png","client_side/view/pics/pekka_attack",
                    7,35,35,0, TargetTypes.GROUND,1,
                    false,new Point2D(0,0),usr, SpeedTypes.FAST,1.8,600,"client_side/view/pics/pekka_walk",6,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.GIANT',1,126,2000)");
            card = new SoldierCard(CardTypes.GIANT,5,126,1,"client_side/view/pics/giant_walking0.png","client_side/view/pics/giant_attack",
                    5,60,50,0, TargetTypes.BUILDINGS,1,
                    false,new Point2D(0,0),usr, SpeedTypes.SLOW,1.5,2000,"client_side/view/pics/giant_walking",4,"",0);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.VALKYRIE',1,120,880)");
            card = new SoldierCard(CardTypes.VALKYRIE,4,120,1,"client_side/view/pics/valkyrie_walk3.png","client_side/view/pics/valkyrie_attack",
                    6,40,50,0, TargetTypes.GROUND,1,
                    true, new Point2D(0,0),usr,SpeedTypes.MEDIUM,1.5,880,"client_side/view/pics/valkyrie_walk",8,"client_side/view/pics/valkyrie_die",1);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.RAGE',1,0,6)");
            card = new SpellCard(CardTypes.RAGE,3,0,1,"client_side/view/pics/rage.png","",
                    0,30,45,5,null,1,false,new Point2D(0,0),usr,null
                    ,40,40,40,false);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.FIREBALL',1,325,0)");
            card = new SpellCard(CardTypes.FIREBALL,4,325,1,"client_side/view/pics/fireball4.png","client_side/view/pics/fireball",
                    6,80,200,2.5,null,1,false,new Point2D(0,0),usr,null
                    ,0,0,0,true);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.ARROWS',1,144,0)");
            card = new SpellCard(CardTypes.ARROWS,3,144,1,"client_side/view/pics/arrow0.png","client_side/view/pics/arrow",
                    1,30,80,4,null,1,false,new Point2D(0,0),usr,null
                    ,0,0,0,true);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.CANNON',1,60,380)");
            card = new BuildingCard(CardTypes.CANNON,6,60,1,"client_side/view/pics/cannon_walk0.png","client_side/view/pics/cannon_walk",
                    1,50,50,5.5,TargetTypes.GROUND,1,false,new Point2D(0,0),usr,"",0,
                    0.8,30,380);
            cards.add(card);


            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,level,damage,hp) VALUES" +
                    " ('CardTypes.INFERNO_TOWER',1,400,800)");
            card = new BuildingCard(CardTypes.INFERNO_TOWER,5,400,1,"client_side/view/pics/inferno_attack0.png","client_side/view/pics/inferno_attack",
                    1,45,45,6,TargetTypes.AIR_GROUND,1,false,new Point2D(0,0),usr,"",0,
                    0.4,40,800);
            cards.add(card);



            tableName = usr+" history";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(opponent VARCHAR(255), winner VARCHAR(255))");
            statement.executeUpdate(sb.toString());

            // new the cards and make the player and return
            return new Player(usr,password,1,0, cards,socket,outObj,inObj);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}