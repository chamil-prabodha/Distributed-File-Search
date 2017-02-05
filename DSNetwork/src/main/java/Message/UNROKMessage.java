package Message;

/**
 * Created by Chamil Prabodha on 04/02/2017.
 */
public class UNROKMessage extends AbstractMessage {

    private int value = 9999;

    public UNROKMessage(int length, MessageType type, int value){
        setLength(length);
        setType(type);
        this.value = value;
    }

    @Override
    public String dump(){
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[UNROK]"));
        stringBuilder.append(value);
        return stringBuilder.toString();
    }
    public int getValue(){
        return value;
    }
}
