package Message;

/**
 * Created by Chamil Prabodha on 09/01/2017.
 */
public class JOINMessage extends AbstractMessage {

    //    private int length = 0;
//    private MessageType type = null;
    private String ip = null;
    private int port = 0;

    public JOINMessage(int length, MessageType type, String ip, int port){
        setLength(length);
        setType(type);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String dump() {
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[JOIN]"));
        stringBuilder.append(this.ip);
        stringBuilder.append(" ");
        stringBuilder.append(this.port);
        return stringBuilder.toString();
    }

    public String getIp(){
        return this.ip;
    }

    public int getPort(){
        return this.port;
    }

}
