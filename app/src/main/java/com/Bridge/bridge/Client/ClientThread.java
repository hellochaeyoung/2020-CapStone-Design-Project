package com.Bridge.bridge.Client;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private String mServAddr;
    private Handler mMainHandler;
    public static boolean isRun=false;

    public ClientThread(String servAdder,Handler mainHandler){
        mServAddr=servAdder;
        mMainHandler=mainHandler;

    }

    public void run(){
        Socket socket=null;
        try{
            socket=new Socket(mServAddr,9999);
            doPrintln("Server Success");
            SendThread sendThread=new SendThread(this,socket.getOutputStream());
            RecvThread recvThread=new RecvThread(this,socket.getInputStream());
            sendThread.start();
            recvThread.start();
            isRun=true;
            sendThread.join();
            recvThread.join();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(socket!=null){
                    socket.close();
                    doPrintln("connect finish");
                }
            }catch(IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void doPrintln(String str) {
        if (str.equals("finish")) {
            Message fmsg = Message.obtain();
            fmsg.what = 3;
            fmsg.obj = str + "\n";
            mMainHandler.sendMessage(fmsg);
            System.out.println("finish message oK");

        } else {
            System.out.println("Client Thread here" + str);
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = str + "\n";
            mMainHandler.sendMessage(msg);
        }

    }
}