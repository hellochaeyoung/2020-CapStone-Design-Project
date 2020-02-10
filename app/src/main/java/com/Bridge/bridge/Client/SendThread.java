package com.Bridge.bridge.Client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SendThread extends Thread {

    private ClientThread mClientThread;
    private OutputStream mOutStream;
    public static Handler mHandler;

    public SendThread(ClientThread clientThread, OutputStream outputStream){
        mClientThread=clientThread;
        mOutStream=outputStream;
    }

    public void run(){
        Looper.prepare();
        mHandler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        try{
                            //String s=(String) msg.obj;
                            ByteArrayOutputStream bos=new ByteArrayOutputStream();
                            ObjectOutput out=new ObjectOutputStream(bos);
                            out.writeObject(msg.obj);//Object to byteArray
                            byte[] s=bos.toByteArray();


                            mOutStream.write(s);
                            mClientThread.doPrintln(s.toString());
                        }catch(IOException e){
                            mClientThread.doPrintln(e.getMessage());
                        }
                        break;
                    case 2:
                        getLooper().quit();
                        break;
                }
            }
        };
        Looper.loop();
    }
}