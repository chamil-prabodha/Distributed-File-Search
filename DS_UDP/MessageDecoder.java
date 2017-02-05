//package Message;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Created by Chamil Prabodha on 08/01/2017.
 */
public class MessageDecoder {

    public static AbstractMessage decodeMessage(String response){

        StringTokenizer st = new StringTokenizer(response," ");

        AbstractMessage res = null;

        String length = st.nextToken();
        String type = st.nextToken();


        MessageType t = MessageType.valueOf(type);

        switch (t){

            case JOIN:
                String ip = st.nextToken();
                String port = st.nextToken();
                res = new JOINMessage(Integer.parseInt(length),t,ip,Integer.parseInt(port));
                return res;

            case REGOK:
                String no_nodes = st.nextToken();
                res = new REGOKMessage(Integer.parseInt(length),t,Integer.parseInt(no_nodes));
                for(int i = 0;i<Integer.parseInt(no_nodes);i++){
                    String neighbour_ip = st.nextToken();
                    int neighbour_port = Integer.parseInt(st.nextToken());
                    ((REGOKMessage)res).addNeighbour(neighbour_ip,neighbour_port);

                }
                return res;

            case JOINOK:
                String value = st.nextToken();
                res = new JOINOKMessage(Integer.parseInt(length),t,Integer.parseInt(value));
                return res;

            case SER:
                String searchIP  = st.nextToken();
                String searchPort=st.nextToken();
                String searchFileName=st.nextToken();
                String searchHops=st.nextToken();
                String previp = st.nextToken();
                String prevPort = st.nextToken();
//                System.out.println(searchIP+" "+searchPort+" "+searchFileName+" "+searchHops+" "+previp+" "+prevPort);
                res = new SERMessage(Integer.parseInt(length),t,searchIP,Integer.parseInt(searchPort),previp,Integer.parseInt(prevPort),searchFileName,Integer.parseInt(searchHops));
                return res;

            case SEROK:
                System.out.println("File Searched : "+response);
                String searchRespNoOfFiles=st.nextToken();
                String searchRespIP  = st.nextToken();
                String searchRespPort=st.nextToken();
                String searchRespHops=st.nextToken();
                ArrayList<String> fileList=new ArrayList<String>();
                for (int i = 0; i < Integer.parseInt(searchRespNoOfFiles); i++) {

                    fileList.add(st.nextToken());
                }
                res = new SEROKMessage(Integer.parseInt(length),t,Integer.parseInt(searchRespNoOfFiles),searchRespIP,Integer.parseInt(searchRespPort),Integer.parseInt(searchRespHops),fileList);
                return res;

            case UNROK:
                value = st.nextToken();
                res = new UNROKMessage(Integer.parseInt(length),t,Integer.parseInt(value));
                return res;

            case LEAVE:
                ip = st.nextToken();
                port = st.nextToken();
                res = new LEAVEMessage(Integer.parseInt(length),t,ip,Integer.parseInt(port));
                return res;

            case LEAVEOK:
                value = st.nextToken();
                res = new LEAVEOKMessage(Integer.parseInt(length),t,Integer.parseInt(value));
                return res;
            default:
                return null;


        }

    }

}
