//package Message;

import java.util.ArrayList;
import java.util.List;
//import Util.Neighbour;

/**
 * Created by Chamil Prabodha on 08/01/2017.
 */
public class REGOKMessage extends AbstractMessage {

    private int no_nodes = 0;
    private List<Neighbour> neighbours = null;

    public REGOKMessage(int length, MessageType type, int no_nodes){
        setLength(length);
        setType(type);
        this.no_nodes = no_nodes;
        neighbours = new ArrayList<Neighbour>();
    }

    public void addNeighbour(String ip, int port){
        Neighbour neighbour = new Neighbour(ip,port);
        neighbours.add(neighbour);
    }

    public int getNo_nodes(){
        return no_nodes;
    }

    public List<Neighbour> getNeighbours(){
        return neighbours;
    }

    @Override
    public String dump() {
        StringBuilder stringBuilder = new StringBuilder(String.format("%0$-15s","[REGOK]"));
        if(this.no_nodes==0) {
            stringBuilder.append("Neighbours: empty");
            return stringBuilder.toString();
        }
        else {
            stringBuilder.append("Neighbours: ");

            for (Neighbour n : neighbours) {
                stringBuilder.append("(");
                stringBuilder.append(n.getIp());
                stringBuilder.append(",");
                stringBuilder.append(n.getPort());
                stringBuilder.append(")");
                stringBuilder.append(" ");
            }

            return stringBuilder.toString();
        }
    }
}
