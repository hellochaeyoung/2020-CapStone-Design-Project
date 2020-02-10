package com.Bridge.bridge.Client;

import android.os.Message;

import java.io.IOException;
import java.io.InputStream;

public class RecvThread extends Thread {
    private ClientThread mClientThread;
    private InputStream mInputStream;

    public RecvThread(ClientThread clientThread,InputStream inputStream){
        mClientThread=clientThread;
        mInputStream=inputStream;

    }

    public void run(){
        byte[] buf=new byte[1024];
        while(true){
            try{
                int nbytes=mInputStream.read(buf);
                if(nbytes>0){
                    String s=new String(buf,0,nbytes);
                    mClientThread.doPrintln(s);
                }else{
                    mClientThread.doPrintln("unConnect");
                    if(SendThread.mHandler!=null){
                        Message msg= Message.obtain();
                        msg.what=2;
                        SendThread.mHandler.sendMessage(msg);
                    }
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}