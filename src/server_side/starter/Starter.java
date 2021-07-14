package server_side.starter;
import server_side.database.DBUtil;
import shared.model.player.Player;
import server_side.manager.GameLoop;
import server_side.model.Bot;
import server_side.model.BotLevel1;
import server_side.model.BotLevel2;
import server_side.model.BotLevel3;
import shared.model.Message;
import shared.enums.MessageType;

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
        ArrayBlockingQueue<Message> incomingEventsForBots = new ArrayBlockingQueue<>(50);
        LinkedTransferQueue<Message> gameModeMsg = new LinkedTransferQueue<>();
        LinkedTransferQueue<Message> joinGameMsg = new LinkedTransferQueue<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        DBUtil dbUtil = new DBUtil();


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
                firstPlayer = signupOrLogin("login",firstPlayerLoginMsg,firstPlayerSocket,oisFirst,oosFirst , dbUtil);
            }
            else
            {
                firstPlayer = signupOrLogin("signup",firstPlayerLoginMsg,firstPlayerSocket,oisFirst,oosFirst ,dbUtil); // signup for first player
            }
            if (firstPlayer != null) // accepted
            {
                String cards = "";
                for (int i = 0 ; i < 12 ; i++)
                    cards += firstPlayer.getCards().get(i).getType().toString() + ",";
                firstPlayer.getSender().sendMsg(new Message(MessageType.DATA , "Server" , firstPlayer.getName() + ","
                        + firstPlayer.getLevel() + "," + firstPlayer.getXp() + "," + firstPlayer.getPassword() + "," + cards));
                players.add(firstPlayer);
                MsgSeparator msgSeparator = new MsgSeparator(firstPlayer,firstPlayer.getSharedInbox(), gameModeMsg ,joinGameMsg , inGameInbox); // from now all messages are got from Getter!!
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
            gameMode = gameModeMsg.take(); // special content : 1v1 or 2v2 or bot1 or bot2 or bot3
        }catch (InterruptedException e)
        {
            System.out.println("Interrupted in getting gameModeMsg. Exiting...");
            System.exit(-1);
        }

        int toWait = (gameMode.getContent().equals("bot1")  || gameMode.getContent().equals("bot2") || gameMode.getContent().equals("bot3")) ? 0
                : gameMode.getContent().equals("1v1") ? 1 : 3 ;

        // players should join one by one

        if (toWait == 0) // with bot
        {
            Bot bot = gameMode.getContent().equals("bot1") ? new BotLevel1(inGameInbox , incomingEventsForBots , players.get(0).getLevel()) : gameMode.getContent().equals("bot2")
                    ? new BotLevel2(inGameInbox , incomingEventsForBots , players.get(0).getLevel()) : new BotLevel3(inGameInbox , incomingEventsForBots , players.get(0).getLevel());
            GameLoop gameLoop = new GameLoop(gameMode.getContent(), players , bot , inGameInbox,incomingEventsForBots , executor);
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
                            player = signupOrLogin("login",msg,socket, ois, oos , dbUtil);
                        } else // signup request
                        {
                            player = signupOrLogin("signup",msg,socket, ois, oos , dbUtil);
                        }
                        if (player != null) // accepted
                        {
                            String cards = "";
                            for (int j = 0 ; j < 12 ; j++)
                                cards += player.getCards().get(j).getType().toString() + ",";
                            player.getSender().sendMsg(new Message(MessageType.DATA , "Server" , player.getName() + ","
                                    + player.getLevel() + "," + player.getXp() + "," + player.getPassword() + "," + cards));
                            MsgSeparator msgSeparator = new MsgSeparator(player,player.getSharedInbox(), gameModeMsg, joinGameMsg, inGameInbox); // from now all messages are got from Getter!!
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
            GameLoop gameLoop = new GameLoop(gameMode.getContent(), players , null , inGameInbox, incomingEventsForBots , executor); // here should change for more game kinds (like bot + bot + human + human)
            gameLoop.play();
        }
    }
    public static Player signupOrLogin(String singOrLog , Message req,Socket socket , ObjectInputStream inObj
            , ObjectOutputStream outObj , DBUtil dbUtil){ // because player is created in this method , we need these parameters

        Player player = null;
        if (singOrLog.equals("signup"))
            player = dbUtil.register(req.getSender(), req.getContent(),socket,outObj,inObj);
        else
            player = dbUtil.getPlayer(req.getSender(), req.getContent(),socket,outObj,inObj);

        return player; // if player didn't created - null is returned
    }

}