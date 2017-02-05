package app;

import Util.Configurations;


/**
 * Created by Chamil Prabodha on 05/01/2017.
 */
public class Daemon {

    public static void main(String[] args){

        try {

            Node node = new Node(Configurations.NODEIP,Configurations.NODEPORT);
            node.registerNode(Configurations.USERNAME);
            node.setFileRepo(Configurations.FILEREPO);
            Thread.sleep(3000);
            node.search();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


