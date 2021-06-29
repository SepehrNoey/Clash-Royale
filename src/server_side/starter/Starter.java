package server_side.starter;
import client_side.model.Player;
import shared.Message;
import shared.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * starting point of game in server side
 *
 * this class gets valid players from dataBase , initializes a map , makes
 * other things ready and at last gives the rest to other classes to run game
 *
 * @version 1.0
 */
public class Starter {
    public static void main(String[] args){
        ServerSocket server = null ;
        ArrayList<Player> players = new ArrayList<>();
        try {
            server = new ServerSocket(7000);
        }catch (IOException e)
        {
            System.out.println("Can't make server!");
            System.exit(-1);
        }
        while (true){
            Socket socket = null;
            Player player = null;
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            try {
                socket = server.accept();
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
            }catch (IOException e){
                System.out.println("Can't accept request in Starter or can't make input or output stream"); // may have bug here because of timeout!
                System.exit(-1);
            }
            Message msg = null;
            try {
                msg = (Message) ois.readObject();
                if (msg.getType() == MessageType.LOGIN_REQ)
                {
                    // database work
                    player = login(socket , ois , oos);
                }
                else // signup request
                {
                    // database work
                    player = signup(socket , ois , oos);
                }
                if (player != null) // accepted
                {
                    players.add(player);

                }
                else { // refused

                }

            }catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
                System.exit(-1);
            }


        }
    }

    public static Player login(Socket socket , ObjectInputStream inObj
            , ObjectOutputStream outObj){ // because player is created in this method , we need these parameters
        return null; // if player didn't created - null is returned
    }
    public static Player signup(Socket socket , ObjectInputStream inObj
            , ObjectOutputStream outObj){ // because player is created in this method , we need these parameters
        return null; // if player didn't created - null is returned
    }

}
