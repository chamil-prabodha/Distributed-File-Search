/**
 * Created by Chamil Prabodha on 05/01/2017.
 */
public class Daemon {

    public static void main(String[] args){
        Node node1 = new Node("127.0.0.1",8083);
        node1.registerNode("node1");
    }
}


