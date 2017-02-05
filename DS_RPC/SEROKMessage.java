//package Message;

import java.util.ArrayList;

/**
 * Created by ruchi on 1/9/17.
 */
public class SEROKMessage extends AbstractMessage {

    private int no_Files=0;
    private String ip=null;
    private int port=0;
    private int hops=0;
    private ArrayList<String> fileNames=new ArrayList<String>();

    public SEROKMessage(int length, MessageType type, int no_Files, String ip, int port, int hopCount, ArrayList<String> fileNames){
        setLength(length);
        setType(type);
        this.no_Files=no_Files;
        this.ip=ip;
        this.port=port;
        this.hops=hopCount;
        this.fileNames=fileNames;
    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[SEROK]"));
        stringBuilder.append(no_Files);
        stringBuilder.append(" ");
        stringBuilder.append(this.ip);
        stringBuilder.append(" ");
        stringBuilder.append(this.port);
        stringBuilder.append(" ");
        stringBuilder.append(this.hops);
        stringBuilder.append(" ");
        for(String name:fileNames){
            stringBuilder.append(name.replace('@',' '));
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();

    }

    public int getNo_Files() {
        return no_Files;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getHops() {
        return hops;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public static String createMessage(int no_Files,String ip, int port,int hopCount,ArrayList<String> fileNames){
        String message="";
        message="SEROK "+no_Files+" "+ip+" "+port+" "+hopCount;
        for(String fileName:fileNames){
            message=message+" "+fileName;
        }
        return message;
    }
}
