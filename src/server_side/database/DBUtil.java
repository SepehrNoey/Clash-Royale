package server_side.database;

import shared.model.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

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
                    // new the cards then player then return
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Player register(String usr, String password)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("('");sb.append(usr);sb.append("','");sb.append(password);sb.append("',1,0)");
        try {
            statement.executeUpdate("INSERT INTO players (username,password,level,xp) VALUES "+sb.toString());

            String tableName = usr+" cards";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(type VARCHAR(255), cost INT, level INT, damage INT, hp INT, PRIMARY KEY(type))");

            Statement cardSt = connection.createStatement();
            cardSt.executeUpdate(sb.toString());
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.BARBARIAN',5,1,75,300)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.ARCHER',3,1,33,125)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.BABY_DRAGON',4,1,100,800)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.WIZARD',5,1,130,340)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.MINI_PEKKA',4,1,325,600)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.GIANT',5,1,126,2000)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.VALKYRIE',4,1,120,880)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.RAGE',3,1,0,6)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.FIREBALL',4,1,325,0)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.ARROWS',3,1,144,0)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.CANNON',6,1,60,380)");
            cardSt.executeUpdate("INSERT INTO "+tableName+" (type,cost,level,damage,hp) VALUES" +
                    " ('CardTypes.INFERNO_TOWER',5,1,400,800)");

            String tableName = usr+" history";
            sb.setLength(0);
            sb.append("CREATE TABLE ");
            sb.append(tableName);
            sb.append("(opponent VARCHAR(255), winner VARCHAR(255))");
            statement.executeUpdate(sb.toString());

            // new the cards and make the player and return

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
