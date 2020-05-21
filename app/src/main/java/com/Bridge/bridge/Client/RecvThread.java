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
    public byte[] arr=new byte[1000000];
   // public byte[] farr=new byte[1000000];
    public void run(){
        byte[] buf=new byte[1024];
        while(true){
            try{

                int nbytes=mInputStream.read(buf);
                if(nbytes>0){
                    String s=new String(buf,0,nbytes,"UTF-8");
                    //String s= new String()
                   // String se= Base64.encodeToString(buf, Base64.DEFAULT);
                    //System.out.println(s);
                    int len = s.length();

                   // System.out.println(buf.toString());
                    //recvString+=s;
                    System.arraycopy(buf,0,arr,index,nbytes);
                    index+=nbytes;
                    //buf[nbytes-1]==125 && buf[nbytes-2]==125 && buf[nbytes-3]==125
                    //s.substring(len-4,len-1).equals("}}}");
                    System.out.println(s);
                    if(s.substring(len-3,len).equals("}}}")) {
                        System.out.println("OK");
                        //File file = new File(Environment.DIRECTORY_DOCUMENTS+"/test.pptx");
                        byte[] farr=new byte[index-3];
                        File file = new File("/sdcard/Download/test.pptx");
                        for(int i=0;i<index-3;i++){
                            farr[i]=arr[i];
                        }
                        for(int i=0;i<arr.length;i++){
                            System.out.print(arr[i]);
                        }

                        String encoded = Base64.encodeToString(farr,Base64.DEFAULT);
                        byte[] decoded = Base64.decode(farr.toString(), 0);
                        //String decoded = new String(Base64.decode(encoded));

                       // System.out.println(encoded+"######");

                       // Base64 codec = new Base64();
                       // Base64.g
                       // String se=new String(farr,0,index,"UTF-8");
                       // byte[] newarr = new byte[100000];

//                        try{
//                            DataInputStream in = new DataInputStream(mInputStream);
//                            in.readFully(farr);
//
//                        }catch(IOException e){
//                            e.printStackTrace();
//                        }
                      //  BufferedInputStream bis = new BufferedInputStream(mInputStream);
                       // System.out.println(s);



                        FileOutputStream fos=new FileOutputStream(file);
                        fos.write(farr);
//                        int ch ;
//                        while((ch = bis.read()) != -1){
//                            fos.write(ch);
//                        }

                        fos.close();
                        mClientThread.doPrintln("finish");

                        // Message msg= Message.obtain();
                        //msg.what=1;
//                        System.out.println(se);


                        if(SendThread.mHandler!=null) {
                            Message msg = Message.obtain();
                            msg.what = 2;
                            SendThread.mHandler.sendMessage(msg);
                            System.out.println("null SendThread");
                        }
                        MainActivity.isFinish=true;
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