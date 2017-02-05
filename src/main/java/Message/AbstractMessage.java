package Message;

/**
 * Created by Chamil Prabodha on 08/01/2017.
 */
public abstract class AbstractMessage {

    private int length = 0;
    private MessageType type = null;

    public abstract String dump();

    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }

    public MessageType getType(){
        return type;
    }

    public void setType(MessageType type){
        this.type = type;
    }

}
