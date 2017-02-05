//package Message;

/**
 * Created by Chamil Prabodha on 04/02/2017.
 */
public class LEAVEMessage extends AbstractMessage {

    private String ip = null;
    private int port = 0;

    public LEAVEMessage(int length, MessageType type, String ip,int port){
        setLength(length);
        setType(type);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[LEAVE]"));
        stringBuilder.append(this.ip);
        stringBuilder.append(" ");
        stringBuilder.append(this.port);
        return stringBuilder.toString();
    }
    public String getIp(){
        return ip;
    }

    public int getPort(){
        return port;
    }
}
