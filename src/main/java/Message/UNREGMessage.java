package Message;

/**
 * Created by Chamil Prabodha on 04/02/2017.
 */
public class UNREGMessage extends AbstractMessage {

    private String ip = null;
    private int port = 0;
    private String username = null;

    public UNREGMessage(int length, MessageType type, String ip, int port, String username){
        setLength(length);
        setType(type);
        this.ip = ip;
        this.port = port;
        this.username = username;
    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[UNREG]"));
        stringBuilder.append(ip);
        stringBuilder.append(" ");
        stringBuilder.append(port);
        stringBuilder.append(" ");
        stringBuilder.append(username);
        return stringBuilder.toString();
    }

    public String getIp(){
        return ip;
    }

    public int getPort(){
        return port;
    }

    public String getUsername(){
        return username;
    }
}
