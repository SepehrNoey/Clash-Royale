package server_side.starter;
import client_side.model.Player;
import server_side.manager.GameLoop;
import server_side.model.Bot;
import server_side.model.BotLevel1;
import server_side.model.BotLevel2;
import server_side.model.BotLevel3;
import shared.Message;
import shared.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

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
        System.out.println("Server created.");

        ArrayBlockingQueue<Message> inGameInbox = new ArrayBlockingQueue<>(50);
        LinkedTransferQueue<Message> gameModeMsg = new LinkedTransferQueue<>();
        LinkedTransferQueue<Message> joinGameMsg = new LinkedTransferQueue<>();
        ExecutorService executor = Executors.newCachedThreadPool();


        // first player login or signup and also creating a new game
        while (true)
        {
            Socket firstPlayerSocket = null;
            Player firstPlayer = null;
            ObjectOutputStream oosFirst = null;
            ObjectInputStream oisFirst = null;
            try {
                firstPlayerSocket = server.accept();
                oosFirst = new ObjectOutputStream(firstPlayerSocket.getOutputStream());
                oisFirst = new ObjectInputStream(firstPlayerSocket.getInputStream());
            }catch (IOException e)
            {
                System.out.println("Can't make firstPlayer's socket or its streams."); // may have bug here because of timeout!
                System.exit(-1);
            }
            Message firstPlayerLoginMsg = null;
            try {
                firstPlayerLoginMsg = (Message) oisFirst.readObject();
            }catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            if (firstPlayerLoginMsg.getType() == MessageType.LOGIN_REQ) // login for first player
            {
                firstPlayer = login(firstPlayerSocket,oisFirst,oosFirst);
            }
            else
            {
                firstPlayer = signup(firstPlayerSocket,oisFirst,oosFirst); // signup for first player
            }
            if (firstPlayer != null) // accepted
            {
                players.add(firstPlayer);
                MsgSeparator msgSeparator = new MsgSeparator(firstPlayer.getSharedInbox(), gameModeMsg ,joinGameMsg , inGameInbox); // from now all messages are got from Getter!!
                executor.execute(firstPlayer.getGetter());
                executor.execute(msgSeparator);
                break; // now going for other players(if exist)
            }
            else { // refused
                try {
                    oosFirst.writeObject(new Message(MessageType.REFUSED , "Server" , "Login or signup refused. Try again!"));
                }catch (IOException e){
                    System.out.println("Can't send refused message to first player.");
                }
            }
        }

        Message gameMode = null;
        // here , waits until the first player sends the gameModeMessage

        try{
            gameMode = gameModeMsg.take(); // at this time we should render a raw board at client side!!!
        }catch (InterruptedException e)
        {
            System.out.println("Interrupted in getting gameModeMsg. Exiting...");
            System.exit(-1);
        }

        String[] split = gameMode.getContent().split(","); // special content : 1v1 or 2v2 , PvB or PvP , bot1 or bot2 or bot3 (if exist - just for play with bot)
        // three game modes = player vs bot , player vs player , 4 players
        int toWait = split[0].equals("1v1") && split[1].equals("PvB") ? 0 : split[0].equals("1v1") && split[1].equals("PvP") ? 1 : 3 ;

        // players should join one by one

        if (toWait == 0) // with bot
        {
            Bot bot = split[2].equals("bot1") ? new BotLevel1(inGameInbox) : split[2].equals("bot2")
                    ? new BotLevel2(inGameInbox) : new BotLevel3(inGameInbox);
            GameLoop gameLoop = new GameLoop(players , bot);
            gameLoop.play();
        }
        else { // waiting for one or three players
            for (int i = 0; i < toWait; i++) { // can be threaded later
                Socket socket = null;
                Player player = null;
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;
                try {
                    socket = server.accept();
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    System.out.println("Can't accept request in Starter or can't make input or output stream"); // may have bug here because of timeout!
                    System.exit(-1);
                }
                while (true) {
                    Message msg = null;
                    try {
                        msg = (Message) ois.readObject();
                        if (msg.getType() == MessageType.LOGIN_REQ) {
                            // database work
                            player = login(socket, ois, oos);
                        } else // signup request
                        {
                            // database work
                            player = signup(socket, ois, oos);
                        }
                        if (player != null) // accepted
                        {
                            MsgSeparator msgSeparator = new MsgSeparator(player.getSharedInbox(), gameModeMsg, joinGameMsg, inGameInbox); // from now all messages are got from Getter!!
                            executor.execute(player.getGetter());
                            executor.execute(msgSeparator);

                            // here waits until the player sends joinGameReq msg

                            try {
                                Message joinGame = joinGameMsg.take();
                            } catch (InterruptedException e) {
                                System.out.println("Interrupted in getting joinGameMsg. Exiting...");
                                System.exit(-1);
                            }
                            // player joined
                            players.add(player);
                            for (Player ply : players) {
                                ply.getSender().sendMsg(new Message(MessageType.PLAYER_JOINED, "Server", player.getName() + " joined!"));
                            }
                            break;

                        } else { // refused
                            i--; // need one more player
                            try {
                                oos.writeObject(new Message(MessageType.REFUSED, "Server", "Login or signup refused. Try Again!"));
                            } catch (IOException e) {
                                System.out.println("Can't send refused message to first player.");
                            }
                        }

                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
            GameLoop gameLoop = new GameLoop(players , null); // here should change for more game kinds (like bot + bot + human + human)
            gameLoop.play();
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
