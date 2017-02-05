

package Connection;

import Message.AbstractMessage;
import Message.MessageDecoder;

import java.io.IOException;
import java.net.*;

/**
 * Created by Chamil Prabodha on 03/01/2017.
 */
public class UDPConnection extends AbstractConnection {

//    private static DSConnection instance = null;
//    private InetAddress ipaddress = null;
//    private String ip = null;
//    private int port = 8081;
//    private MessageListener listener = null;
//
//    private DatagramSocket sock = null;

    private UDPConnection(){

    }

    public static void init(String ip, int port,MessageListener listener){
        instance = getConnection();
        instance.ip = ip;
        instance.port = port;
        instance.listener = listener;
        try {
            instance.ipaddress = InetAddress.getByName(instance.ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static UDPConnection getConnection(){
        if (instance == null)
            instance = new UDPConnection();

        return (UDPConnection)instance;
    }

//    public String connectToBootstrap(String command, int port){
//
//        Utilities util = new Utilities();
//
//        try {
////            this.ipaddress = InetAddress.getByName("localhost");
//            this.sock = new DatagramSocket(this.port);
////            this.port = port;
////            this.ip = "127.0.0.1";
//
////            String command = "UNREG 127.0.0.1 8084 node4";
//            command = String.format("%04d",command.length()+5)+" "+command;
//
//            byte[] sendBytes = command.getBytes();
//            byte[] receiveBytes = new byte[65536];
//
//            if(this.ipaddress == null && this.ip == null){
//                System.err.println("NodeExecution.DSConnection: Must call init before calling connectToBootstrap");
//                return null;
//            }
//
//            InetAddress ip = InetAddress.getByName(util.getBootstrapIp());
//            DatagramPacket sendPacket = new DatagramPacket(sendBytes,sendBytes.length,ip,util.getBootstrapPort());
//            this.sock.send(sendPacket);
//
//            DatagramPacket receivePacket = new DatagramPacket(receiveBytes,receiveBytes.length);
//            this.sock.receive(receivePacket);
//
//            String receiveData = new String(receivePacket.getData(),0,receivePacket.getLength());
//            System.out.println(receiveData);
//
////            this.sock.close();
//
//            return receiveData;
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//            return null;
//        } catch (UnknownHostException e){
//            e.printStackTrace();
//            return null;
//        } catch (IOException e){
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public String unreg(String command) {
//
//        Utilities util = new Utilities();
//
//        try {
//            command = String.format("%04d", command.length() + 5) + " " + command;
//            byte[] sendBytes = command.getBytes();
//            byte[] receiveBytes = new byte[65536];
//
//            if (this.ipaddress == null && this.ip == null) {
//                System.err.println("NodeExecution.DSConnection: Must call init before calling connectToBootstrap");
//                return null;
//            }
//
//            InetAddress ip = InetAddress.getByName(util.getBootstrapIp());
//            DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, ip, util.getBootstrapPort());
//            this.sock.send(sendPacket);
//
//            DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
//            this.sock.receive(receivePacket);
//
//            String receiveData = new String(receivePacket.getData(), 0, receivePacket.getLength());
//            return receiveData;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

    public void Send(String command, String ip, int port){

        command = String.format("%04d",command.length()+5)+" "+command;
        byte[] sendBytes = command.getBytes();

        if(this.sock == null){
            System.err.println("NodeExecution.DSConnection: Must connect to bootstrap server before sending messages");
            return;
        }

        if(this.ipaddress == null && this.ip == null){
            System.out.println("NodeExecution.DSConnection: Must call init before calling send");
            return;
        }

        try {

            InetAddress ipaddr = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(sendBytes,sendBytes.length,ipaddr,port);
//            System.out.println(ip+" - "+port);
            this.sock.send(sendPacket);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @Deprecated
    private void openConnection(String ip, int port){
        try {
            this.ipaddress = InetAddress.getByName(ip);
            this.sock = new DatagramSocket(port,this.ipaddress);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }

    }

    @Deprecated
    private void openConnection(int port){
        try {
            this.sock = new DatagramSocket(port);

        } catch (SocketException e){
            e.printStackTrace();
        }
    }

    public void listen(final String ip, final int port){

        Thread thread = new Thread(new Runnable() {

            public void run() {

                try {
                    InetAddress ipaddr = InetAddress.getByName(ip);
//                    DatagramSocket socket = new DatagramSocket(port,ipaddr);
                    while(listenerEnabled) {

                        byte[] buffer = new byte[65536];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        try {
                            getSock().receive(packet);
                            String receivedata = new String(packet.getData(),0,packet.getLength());

                            AbstractMessage response = MessageDecoder.decodeMessage(receivedata);
                            getListener().messageReceived(response);

                        } catch (SocketTimeoutException e){

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e){
                    e.printStackTrace();
                }

            }
        });

        System.out.println("Listening");
        thread.start();


    }

    public DatagramSocket getSock(){
        if(this.sock.isClosed())
            try {
                this.sock = new DatagramSocket(this.port,this.ipaddress);
            } catch (SocketException e) {
                e.printStackTrace();
            }

        return this.sock;
    }

//    private MessageListener getListener(){
//        return this.listener;
//    }

    public String getRawIp(){
        return this.ip;
    }

    public int getPort(){
        return this.port;
    }

    public InetAddress getIpaddress(){
        return this.ipaddress;
    }

    public void addListener(MessageListener listener){
        this.listener = listener;
    }



}
