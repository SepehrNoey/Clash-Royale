package server_side.starter;

import java.net.ServerSocket;

public class JoinServer implements Runnable{
    private ServerSocket serverSocket;

    public JoinServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

    }
}
