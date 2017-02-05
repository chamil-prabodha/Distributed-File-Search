package app;

import Connection.MessageListener;
import Connection.RPCConnection;
import Connection.UDPConnection;
import Message.*;
import Util.Configurations;
import Util.Neighbour;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chamil Prabodha on 03/01/2017.
 */
public class Node implements MessageListener {

    Boolean isSearching=false;
    Random rn = new Random();
    private String fileRepo="src/main/FileRepo";
    int hopCount=5;
    SEROKListener serokListener = null;
    private List<Neighbour> neighbours = null;
    BufferedReader bufferedReader = null;
    String nextquery = null;
    int QueryCount = 1;
    String username = null;

//    public static void main(String args[]){
//        boolean status = registerNode("127.0.0.1", 8084, "node4");
//        if(status)
//            System.out.println("NodeExecution.app.Node Successfully Registered");
//    }



    public Node(String ip,int port){
//        NodeExecution.DSConnection.init(ip,port,this);
        RPCConnection.init(ip, port, this);
        neighbours = new ArrayList<Neighbour>();

    }

    public boolean registerNode(String username){

        this.username = username;

        String ip = RPCConnection.getConnection().getRawIp();
        int port = RPCConnection.getConnection().getPort();

        String command = "REG "+ip+" "+port+" "+username;

        String response = RPCConnection.getConnection().connectToBootstrap(command,port);

        if(response !=null) {
//            StringTokenizer st = new StringTokenizer(response, " ");
//
//            String length = st.nextToken();
//            String rescode = st.nextToken();
//
//            String count = st.nextToken();

            AbstractMessage res = MessageDecoder.decodeMessage(response);

            int length = res.getLength();
            MessageType rescode = res.getType();

            int count = ((REGOKMessage)res).getNo_nodes();

            if (count == 9999) {
                System.err.println("BOOTSTRAP: failed, there is some error in the command");
                return false;
            } else if (count == 9998) {
                System.err.println("BOOTSTRAP: failed, already registered to you, unregister first");
                return false;
            } else if (count == 9997) {
                System.err.println("BOOTSTRAP: failed, registered to another user, try a different IP and port");
                return false;
            } else if (count == 9996) {
                System.err.println("BOOTSTRAP: failed, can’t register. BS full");
                return false;
            } else {
//                for(int i=0;i<Integer.parseInt(count);i++){
//                    String neighbour_ip = st.nextToken();
//                    int neighbour_port = Integer.parseInt(st.nextToken());
//                    neighbours.add(new Neighbour(neighbour_ip,neighbour_port));
//                    join(neighbour_ip,neighbour_port);
//                }

                neighbours = ((REGOKMessage)res).getNeighbours();
                String list = "";
                for(int i=0;i<neighbours.size();i++){
                    Neighbour neighbour = neighbours.get(i);
                    join(neighbour.getIp(),neighbour.getPort());
                    list += "<"+neighbour.getIp()+","+neighbour.getPort()+">";
                    list += " ";
                }
//                for(Neighbour n:neighbours){
//                    System.out.println(n.getIp()+":"+n.getPort());
//                }

                System.out.println(res.dump());
                listen();
                return true;
            }
        }

        return false;

    }

    private boolean unregisterNode(String username){

        String ip = RPCConnection.getConnection().getRawIp();
        int port = RPCConnection.getConnection().getPort();

        String command = "UNREG "+ip+" "+port+" "+username;
        String response = RPCConnection.getConnection().unreg(command);

        if(response!=null)
            RPCConnection.getConnection().getListener().messageReceived(MessageDecoder.decodeMessage(response));
        return false;
    }

    private void join(String ip, int port){

        String command = "JOIN "+ RPCConnection.getConnection().getRawIp()+" "+ RPCConnection.getConnection().getPort();
        RPCConnection.getConnection().Send(command,ip,port);
    }

    private void listen(){
        String ip = RPCConnection.getConnection().getRawIp();
        int port = RPCConnection.getConnection().getPort();

        RPCConnection.getConnection().listen(ip,port);
    }

    private String getUsername(){
        return this.username;
    }
//    private boolean unregisterNode(String username){
//
//        String ip = NodeExecution.DSConnection.getConnection().getRawIp();
//        int port = NodeExecution.DSConnection.getConnection().getSearchport();
//
//        String command = "UNREG "+ip+" "+port+" "+username;
//        NodeExecution.DSConnection.getConnection().connectToBootstrap(command,port);
//        return false;
//    }
//
//    private void join(String ip, int port){
//
//        String command = "JOIN "+NodeExecution.DSConnection.getConnection().getRawIp()+" "+NodeExecution.DSConnection.getConnection().getSearchport();
//        NodeExecution.DSConnection.getConnection().Send(command,ip,port);
//    }
//
//    private void listen(){
//        String ip = NodeExecution.DSConnection.getConnection().getRawIp();
//        int port = NodeExecution.DSConnection.getConnection().getSearchport();
//
//        NodeExecution.DSConnection.getConnection().listen(ip,port);
//    }


    public void messageReceived(AbstractMessage res) {
        int length = res.getLength();
        MessageType type = res.getType();

        switch (type){
            case JOIN:
                String ip = ((JOINMessage)res).getIp();
                int port = ((JOINMessage)res).getPort();

                System.out.println(res.dump());
                Neighbour neighbour = new Neighbour(ip,port);
                neighbours.add(neighbour);
//                for(Neighbour n:neighbours){
//                    System.out.println(n.getIp()+":"+n.getPort());
//                }
                break;
            case SEROK:
                SEROKMessage message=(SEROKMessage)res;
                System.out.println(res.dump());
                System.out.println("====================================================");
                System.out.println("No of files found "+message.getNo_Files());
                for (String fileName:message.getFileNames()) {
                    System.out.println("File : "+fileName);
                }
                System.out.println("Search Finished  : "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                System.out.println("====================================================");
                try {
                    if(bufferedReader != null){
                        nextquery = bufferedReader.readLine();
                    }

                    if(serokListener!=null && nextquery!=null){
                        serokListener.serokReceived();
                    }
                    else if(serokListener!=null && nextquery==null){
                        serokListener.searchFinished();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                isSearching=false;
                break;
            case SER:
                SERMessage messageSearch=(SERMessage)res;

                System.out.println(res.dump());
//                System.out.println("Search File Name "+messageSearch.getFileName());

                ArrayList<String> files=this.localSearch(messageSearch.getFileName());

                if(files.size()>0||messageSearch.getHops()<=0){
                    sendSearchOkRequest(messageSearch.getHops(),files,messageSearch.getSearchip(),messageSearch.getSearchport());
                }else{

                    List<Neighbour> subneighbours = new ArrayList<Neighbour>();
                    for(Neighbour n: neighbours){
                        if(n.getPort()!= messageSearch.getPrevport()) {
                            subneighbours.add(n);
//                            System.out.println(n.getPort());
                        }
                    }

                    if(subneighbours.size()==0){
                        sendSearchOkRequest(messageSearch.getHops(),files,messageSearch.getSearchip(),messageSearch.getSearchport());
                    }
                    else {
                        Neighbour neighbourNode = subneighbours.get(rn.nextInt(subneighbours.size()));

//                        if(DSConnection.getConnection().getPort() != messageSearch.getSearchport())
                        sendSearchRequest(messageSearch.getFileName(), neighbourNode.getIp(), neighbourNode.getPort(), messageSearch.getHops(), messageSearch.getSearchip(), messageSearch.getSearchport());
                    }
                }
                break;

            case UNROK:
                System.out.println(res.dump());
                String command = "LEAVE "+ RPCConnection.getConnection().getRawIp()+" "+ RPCConnection.getConnection().getPort();
                for(Neighbour n:neighbours){
                    RPCConnection.getConnection().Send(command,n.getIp(),n.getPort());
                }
                break;

            case LEAVE:
                System.out.println(res.dump());
                for(Neighbour n:neighbours){
                    if(n.getIp().equalsIgnoreCase(((LEAVEMessage)res).getIp()) && n.getPort()==((LEAVEMessage)res).getPort()){
                        neighbours.remove(n);
                        command = "LEAVEOK "+0;
                        RPCConnection.getConnection().Send(command,n.getIp(),n.getPort());
                    }
                }

//                String list = "";
//                for(Neighbour n:neighbours){
//                    list += "<"+n.getIp()+","+n.getPort()+">";
//                    list += " ";
//                }
//                if(list.equalsIgnoreCase(""))
//                    System.out.println("[NEIGHBOURS] empty");
//                else
//                    System.out.println("[NEIGHBOURS] "+list);

                break;
            case LEAVEOK:
                System.out.println(res.dump());
                break;
            default:
                break;
        }
    }

//    public void search(String searchQuerry){
//
//        System.out.println("Search Started "+searchQuerry+" : "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//        ArrayList<String> files=this.localSearch(searchQuerry);
//
//        if(files.size()>0){
//            sendSearchOkRequest(hopCount,files,
//                    NodeExecution.DSConnection.getConnection().getRawIp(),
//                    NodeExecution.DSConnection.getConnection().getSearchport());
//        }else{
//            System.out.println(neighbours.size());
//            Neighbour neighbourNode=neighbours.get(rn.nextInt(neighbours.size()));
//            sendSearchRequest(searchQuerry,
//                    neighbourNode.getSearchip(),
//                    neighbourNode.getSearchport(),
//                    hopCount,
//                    NodeExecution.DSConnection.getConnection().getRawIp(),
//                    NodeExecution.DSConnection.getConnection().getSearchport());
//        }
//    }


    public void search(String searchQuerry){

        System.out.println("Search Started "+searchQuerry+" : "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        ArrayList<String> files=this.localSearch(searchQuerry);

        if(files.size()>0){
            sendSearchOkRequest(hopCount,files,
                    RPCConnection.getConnection().getRawIp(),
                    RPCConnection.getConnection().getPort());
        }else{
            if(neighbours.size()>0) {
                Neighbour neighbourNode = neighbours.get(rn.nextInt(neighbours.size()));
                sendSearchRequest(searchQuerry,
                        neighbourNode.getIp(),
                        neighbourNode.getPort(),
                        hopCount,
                        RPCConnection.getConnection().getRawIp(),
                        RPCConnection.getConnection().getPort());
            }
        }
    }

    public void searchAll(){

        try{
            bufferedReader = new BufferedReader(new FileReader("src/main/FileRepo/querryList.txt"));
            String line = bufferedReader.readLine();

            search(line);

            serokListener = new SEROKListener() {
                @Override
                public void serokReceived() {
                    if(nextquery!=null) {
                        QueryCount++;
                        System.out.println("[Query count] "+QueryCount);
                        search(nextquery);
                    }
                }

                @Override
                public void searchFinished(){

                }


            };

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(){
        isSearching=true;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true){
//                    System.out.println("Enter the search querry");
                    String searchQuery = scanner.next();
                    searchQuery = searchQuery.trim();
                    if(searchQuery.equalsIgnoreCase("exit")){
                        unregisterNode(getUsername());
                        System.exit(0);
                    }
                    else if(searchQuery.equalsIgnoreCase("#all")){
                        searchAll();
                    }
                    else if(searchQuery.equalsIgnoreCase("#unreg")){
                        unregisterNode(getUsername());
                    }
                    else{
                        search(searchQuery);
                    }

                }
            }
        });

        t.start();


    }

    public void search(File file){
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();

            while (line != null) {
                System.out.println(line);
                this.search(line);

                while (isSearching){

                }
                line = br.readLine();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> localSearch(String name) {
//        name=name.trim();
        File folder = new File(this.getFileRepo());
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> fileList=new ArrayList<String>();

//        for (File file : listOfFiles) {
//            if (file.isFile()) {
//
//                QGram dig = new QGram(2);
//                Levenshtein levD = new Levenshtein();
//
//                if (name.equalsIgnoreCase(file.getName().trim())) {
//                    fileList.add(file.getName());
//                }
//                else{
//                    for (String partialName:file.getName().split(" ")){
//                        if(partialName.equalsIgnoreCase(name)){
//                            fileList.add(file.getName());
//                            break;
//                        }
//                    }
//                }
//
////                System.out.print(file.getName() + " : ");
////                System.out.println(dig.distance(name.toLowerCase(), file.getName().toLowerCase()));
////                System.out.print(file.getName() + " : ");
////                System.out.println(levD.distance(name.toLowerCase(), file.getName().toLowerCase()));
//            }
//        }

        try {
            FileReader fr = new FileReader(Configurations.FILEREPO);

            BufferedReader br = new BufferedReader(fr);
            String filename;

            while((filename = br.readLine())!=null){
                if(name.equals(filename)){
                    fileList.add(filename);
                }
                else{
                    StringTokenizer st1 = new StringTokenizer(name, " ");
                    StringTokenizer st2 = new StringTokenizer(filename, " ");
                    String qword = st1.nextToken();
                    String dword;
                    while(st2.hasMoreElements()){
                        if (qword.equals(dword=st2.nextToken())){
                            boolean consecutive = true;
                            while(st1.hasMoreElements() && st2.hasMoreElements()){
                                dword = st2.nextToken();
                                qword = st1.nextToken();
                                if(!qword.equals(dword))
                                    consecutive = false;
                            }
                            if(consecutive)
                                fileList.add(filename);
                        }

                    }
                }

            }

            br.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

//    public void sendSearchRequest(String fileName,String ip,int port,int hopCount,String originIP,int originPort){
//        hopCount--;
//        NodeExecution.DSConnection.getConnection().Send(SERMessage.createMessage(originIP,originPort,fileName,hopCount),ip,port);
//    }
//
//    public void sendSearchOkRequest(int hopCount,ArrayList<String> fileList,String originIP,int originPort){
//        System.out.println("----------------------------------------");
//        System.out.println("Search Finished  : "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//        System.out.println("Search found in - Hop Count : "+hopCount);
//        for(String fileName:fileList){
//            System.out.println("File Name : "+fileName);
//        }
//        System.out.println("----------------------------------------");
//        String ip = NodeExecution.DSConnection.getConnection().getRawIp();
//        int port = NodeExecution.DSConnection.getConnection().getSearchport();
//        NodeExecution.DSConnection.getConnection().Send(SEROKMessage.createMessage(fileList.size(),ip,port,hopCount,fileList),originIP,originPort);
//    }

    public void sendSearchRequest(String fileName,String ip,int port,int hopCount,String originIP,int originPort){
        hopCount--;
        fileName = fileName.replace(' ','@');
        RPCConnection.getConnection().Send(SERMessage.createMessage(originIP,originPort,fileName,hopCount, RPCConnection.getConnection().getRawIp(), RPCConnection.getConnection().getPort()),ip,port);
    }

    public void sendSearchOkRequest(int hopCount,ArrayList<String> fileList,String originIP,int originPort){
//        System.out.println("----------------------------------------");
//        System.out.println("Search Finished  : "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//        System.out.println("Search found in - Hop Count : "+hopCount);
//        for(String fileName:fileList){
//            System.out.println("File Name : "+fileName);
//        }
//        System.out.println("----------------------------------------");
        String ip = RPCConnection.getConnection().getRawIp();
        int port = RPCConnection.getConnection().getPort();
        RPCConnection.getConnection().Send(SEROKMessage.createMessage(fileList.size(),ip,port,hopCount,fileList),originIP,originPort);
    }



    public String getFileRepo() {
        return fileRepo;
    }

    public void setFileRepo(String fileRepo) {
        this.fileRepo = fileRepo;
    }

    public interface SEROKListener{
        public void serokReceived();
        public void searchFinished();

    }
}
