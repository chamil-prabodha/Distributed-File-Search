package Message;

import java.util.StringTokenizer;


/**
 * Created by Chamil Prabodha on 08/01/2017.
 */
public class MessageDecoder {
    public static AbstractResponse decodeMessage(String response){
        StringTokenizer st = new StringTokenizer(response," ");

        AbstractResponse res = null;

        String length = st.nextToken();
        String type = st.nextToken();
        String no_nodes = st.nextToken();

        MessageType t = MessageType.valueOf(type);

        switch (t){
            case REGOK:

                res = new REGResponse(Integer.parseInt(length),t,Integer.parseInt(no_nodes));
                for(int i = 0;i<Integer.parseInt(no_nodes);i++){
                    String neighbour_ip = st.nextToken();
                    int neighbour_port = Integer.parseInt(st.nextToken());
                    ((REGResponse)res).addNeighbour(neighbour_ip,neighbour_port);

                }
                return res;

            default:
                return null;


        }

    }
}
