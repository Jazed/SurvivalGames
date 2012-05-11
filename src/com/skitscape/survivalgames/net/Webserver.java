package com.skitscape.survivalgames.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.skitscape.survivalgames.SurvivalGames;

public class Webserver extends Thread {

    public void run() {
        try{
        ServerSocket st =  new ServerSocket(880);

            while (SurvivalGames.isActive()) {

                Socket skt = st.accept();

                // Spin off request to a new thread to be handled
                Connection c = new Connection(skt);
                c.start();
            }
        }catch (Exception e) {

            e.printStackTrace();
        }

    }
}

