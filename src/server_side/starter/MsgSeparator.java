package server_side.starter;

import server_side.database.DBUtil;
import shared.model.Message;
import shared.enums.MessageType;
import shared.model.player.Player;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class MsgSeparator implements Runnable{
    private Player player;
    private ArrayBlockingQueue<Message> sharedInbox;
    private ArrayBlockingQueue<Message> inGameInbox;
    private LinkedTransferQueue<Message> gameModeMsg;
    private LinkedTransferQueue<Message> joinGameMsg;

    public MsgSeparator(Player player,ArrayBlockingQueue<Message> sharedInbox , LinkedTransferQueue<Message> gameModeMsg , LinkedTransferQueue<Message> joinGameMsg , ArrayBlockingQueue<Message> inGameInbox ){
        this.player = player;
        this.sharedInbox = sharedInbox;
        this.inGameInbox = inGameInbox;
        this.gameModeMsg = gameModeMsg;
        this.joinGameMsg = joinGameMsg;
    }

    @Override
    public void run() {
        Message msg = null;
        while (true)  // handle later!!
        {
            try {
                msg = sharedInbox.take();
            }catch (InterruptedException e){ // exiting here
                System.out.println("msgSeparator interrupted. Closing msgSeparator...");
                break;
            }

            if (msg.getType() == MessageType.PROFILE){
                // database work
            }
            else if (msg.getType() == MessageType.BATTLE_DECK)
            {
                // database work
            }
            else if(msg.getType() == MessageType.BATTLE_HISTORY)
            {
                // database work
                DBUtil dbUtil = new DBUtil();
                Message history = new Message(MessageType.BATTLE_HISTORY,"Server",dbUtil.getHistory(player));
                player.getSender().sendMsg(history);

            }
            else if (msg.getType() == MessageType.GAME_MODE)
            {
                try {
                    gameModeMsg.transfer(msg);
                }catch (InterruptedException e)
                {
                    System.out.println("Interrupted in transferring gameModeMsg. Exiting...");
                    System.exit(-1);
                }
            }
            else if (msg.getType() == MessageType.JOINING_GAME_REQ)
            {
                try {
                    joinGameMsg.transfer(msg);
                }catch (InterruptedException e)
                {
                    System.out.println("Interrupted in transferring joining game message. Exiting...");
                    System.exit(-1);
                }
            }
            else {
                try {
                    inGameInbox.put(msg);
                }catch (InterruptedException e)
                {
                    System.out.println("Can't put inGameMsg in msgSeparator.");
                    e.printStackTrace();
                }
            }
        }
    }
}