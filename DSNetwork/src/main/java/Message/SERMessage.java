package Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruchi on 1/9/17.
 */
public class SERMessage extends AbstractMessage {

    private String searchip =null;
    private int searchport =0;
    private String previp = null;
    private int prevport = 0;
    private String fileName=null;
    private int hops=0;

    public SERMessage(int length, MessageType type, String searchip, int searchport, String previp, int prevport,String fileName,int hopCount){
        setLength(length);
        setType(type);
        this.searchip = searchip;
        this.searchport = searchport;
        this.previp = previp;
        this.prevport = prevport;
        this.fileName=fileName;
        this.hops=hopCount;

    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[SER]"));
        stringBuilder.append(this.searchip);
        stringBuilder.append(" ");
        stringBuilder.append(this.searchport);
        stringBuilder.append(" ");
        stringBuilder.append(this.previp);
        stringBuilder.append(" ");
        stringBuilder.append(this.prevport);
        stringBuilder.append(" ");
        stringBuilder.append(this.hops);
        stringBuilder.append(" ");
        stringBuilder.append(fileName.replace('@',' '));

        return stringBuilder.toString();
    }

    public String getSearchip() {
        return searchip;
    }

    public int getSearchport() {
        return searchport;
    }

    public String getPrevip(){
        return  previp;
    }

    public int getPrevport(){
        return prevport;
    }

    public String getFileName() {

        return fileName;
    }

    public int getHops() {
        return hops;
    }

    public static String createMessage(String ip, int port,String fileName,int hopCount,String previp,int prevport){
        return "SER "+ip+" "+port+" "+fileName+" "+hopCount+ " "+previp+" "+prevport;
    }
}
