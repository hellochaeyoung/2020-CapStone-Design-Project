package com.Bridge.bridge.Client;

import android.os.Message;
import android.util.Base64;

import com.Bridge.bridge.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecvThread extends Thread {
    private ClientThread mClientThread;
    private InputStream mInputStream;
    //public static boolean isFinish=false;

    public String getRecvString() {
        return recvString;
    }

    public  String recvString="";

    public RecvThread(ClientThread clientThread,InputStream inputStream){
        mClientThread=clientThread;
        mInputStream=inputStream;

    }
    int index=0;
    public byte[] arr=new byte[100000];

    public void run(){
        byte[] buf=new byte[1024];
        while(true){
            try{

                int nbytes=mInputStream.read(buf);
                if(nbytes>0){
                    String s=new String(buf,0,nbytes,"UTF-8");
                    //String s= new String()
                    String se= Base64.encodeToString(buf, Base64.DEFAULT);
                    //System.out.println(s);


                   // System.out.println(buf.toString());
                    recvString+=s;
                    System.arraycopy(buf,0,arr,index,nbytes);
                    index+=nbytes;

                    if(buf[nbytes-1]==125 && buf[nbytes-2]==125 && buf[nbytes-3]==125) {
                        System.out.println("OK");
                        //File file = new File(Environment.DIRECTORY_DOCUMENTS+"/test.pptx");
                        File file = new File("/sdcard/Documents/test.pptx");

                        FileOutputStream fos=new FileOutputStream(file);
                        fos.write(arr);
                        fos.close();
                        MainActivity.isFinish=true;

                        System.out.println(new String(arr,0,index));

                        break;
                    }
                }else{
                    mClientThread.doPrintln("unConnect");
                    if(SendThread.mHandler!=null){
                        Message msg= Message.obtain();
                        msg.what=2;
                        SendThread.mHandler.sendMessage(msg);
                        System.out.println("NO");
                    }
                   // break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        //recvString=

    }
}