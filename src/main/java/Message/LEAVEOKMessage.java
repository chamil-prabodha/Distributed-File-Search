package Message;

/**
 * Created by Chamil Prabodha on 04/02/2017.
 */
public class LEAVEOKMessage extends AbstractMessage {

    private int value = 9999;

    public LEAVEOKMessage(int length, MessageType type, int value){
        setLength(length);
        setType(type);
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String dump() {
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[LEAVEOK]"));
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }
}
