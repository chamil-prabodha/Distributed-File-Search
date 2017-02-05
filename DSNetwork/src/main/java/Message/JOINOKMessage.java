package Message;

/**
 * Created by Chamil Prabodha on 08/01/2017.
 */
public class JOINOKMessage extends AbstractMessage {

    private int value = 9999;

    public JOINOKMessage(int length, MessageType type, int value){
        setLength(length);
        setType(type);
        this.value = value;
    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[JOINOK]"));
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }
    public int getValue(){
        return value;
    }

}
