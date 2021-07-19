package server_side.database;

import shared.enums.CardTypes;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class DBUtil {

    private Connection connection;
    private Statement statement;

    public DBUtil() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Clash Royale"
                    ,"root","");
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Player getPlayer(String usr, String password, Socket socket, ObjectOutputStream outObj,
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
                    String tableName = usr +"_cards";
                    ResultSet cardsSet = connection.createStatement().executeQuery("SELECT * FROM "+tableName);

                    for (int i = 0 ; i < 12 ; i++) // 12 cards
                    {
                        cardsSet.next();
                        card = (Card) Troop.makeTroop(true,cardsSet.getString("type") , Integer.parseInt(resultSet.getString("level")) ,
                                null,usr);
                        cards.add(card);
                    }
                    return new Player(true,usr,password,Integer.parseInt(resultSet.getString("level")),Integer.parseInt(resultSet.getString("xp")),
                            cards,socket,outObj,inObj);

                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Player register(String usr, String password, Socket socket, ObjectOutputStream outObj,
                           ObjectInputStream inObj)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("('");sb.append(usr);sb.append("','");sb.append(password);sb.append("',1,0)");
        try {
            statement.executeUpdate("INSERT INTO players (username,`password`,`level`,xp) VALUES "+sb.toString());

            String tableName = usr+"_cards";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(`index` INT AUTO_INCREMENT,`type` VARCHAR(255), `level` INT , PRIMARY KEY(`index`))");

            ArrayList<Card> cards = new ArrayList<>();
            Card card;
            Statement cardSt = connection.createStatement();


            cardSt.executeUpdate(sb.toString());


            for(CardTypes types : CardTypes.values())
            {
                sb.setLength(0);
                sb.append("'");sb.append(types.toString());sb.append("'");
                cardSt.executeUpdate("INSERT INTO "+tableName+" (`type`,`level`) VALUES" +
                        " ("+sb.toString()+",1)");
                card = (Card) Troop.makeTroop(true,types.toString() , 1 , null,usr);
                cards.add(card);
            }
            tableName = usr+"_history";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(opponent VARCHAR(255), winner VARCHAR(255))");
            statement.executeUpdate(sb.toString());

            // new the cards and make the player and return
            return new Player(true,usr,password,1,0, cards,socket,outObj,inObj);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public String getHistory(Player player)
    {
        String tableName = player.getName()+"_history";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(tableName);
        StringBuilder history = new StringBuilder();
        try {
            ResultSet resultSet = statement.executeQuery(sb.toString());
            while (resultSet.next())
            {
                history.append(resultSet.getString("opponent"));
                history.append(" ");
                history.append(resultSet.getString("winner"));
                history.append(" ");
            }
            return history.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public void UpdateCards(Player player)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");sb.append(player.getName());sb.append("_cards");
        try {
            statement.executeUpdate(sb.toString());

            Statement cardStmnt = connection.createStatement();

            String tableName = player.getName()+"_cards";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(`index` INT AUTO_INCREMENT,`type` VARCHAR(255), `level` INT , PRIMARY KEY(`index`))");
            cardStmnt.executeUpdate(sb.toString());

            for(Card card : player.getDeck())
            {
                sb.setLength(0);
                sb.append("'");sb.append(card.getType().toString());sb.append("'");
                cardStmnt.executeUpdate("INSERT INTO "+tableName+" (`type`,`level`) VALUES" +
                        " ("+sb.toString()+","+card.getLevel()+")");
            }
            for(int i=8;i<12;i++)
            {
                Card card = player.getCards().get(i);
                sb.setLength(0);
                sb.append("'");sb.append(card.getType().toString());sb.append("'");
                cardStmnt.executeUpdate("INSERT INTO "+tableName+" (`type`,`level`) VALUES" +
                        " ("+sb.toString()+","+card.getLevel()+")");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateLevelXp(Player player)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE players SET `level`='");
        sb.append(player.getLevel());
        sb.append("', xp='");
        sb.append(player.getXp());
        sb.append("' WHERE username='");
        sb.append(player.getName());
        sb.append("'");
        try {
            statement.executeUpdate(sb.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}