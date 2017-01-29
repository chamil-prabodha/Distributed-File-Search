import Message.AbstractMessage;
import Message.MessageDecoder;

/**
 * Created by Chamil Prabodha on 30/01/2017.
 */
public class sample {

    public String sum(String command){
        AbstractMessage response = MessageDecoder.decodeMessage(command);
        DSRPCConnection.getConnection().getListener().messageReceived(response);
        return "JOINOK";
    }

}
