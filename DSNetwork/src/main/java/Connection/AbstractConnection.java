package Connection; /**
 * Created by Chamil Prabodha on 24/01/2017.
 */

import Util.Configurations;

import java.io.IOException;
import java.net.*;

public abstract class AbstractConnection {

    protected static AbstractConnection instance = null;
    protected InetAddress ipaddress = null;
    protected String ip = null;
    protected int port = 8081;
    protected MessageListener listener = null;
    protected boolean listenerEnabled = true;

    protected DatagramSocket sock = null;

    public String connectToBootstrap(String command, int port){


        try {
//            this.ipaddress = InetAddress.getByName("localhost");
            this.sock = new DatagramSocket(this.port);
//            this.port = port;
//            this.ip = "127.0.0.1";

//            String command = "UNREG 127.0.0.1 8084 node4";
            command = String.format("%04d",command.length()+5)+" "+command;

            byte[] sendBytes = command.getBytes();
            byte[] receiveBytes = new byte[65536];

            if(this.ipaddress == null && this.ip == null){
                System.err.println("NodeExecution.DSConnection: Must call init before calling connectToBootstrap");
                return null;
            }

            InetAddress ip = InetAddress.getByName(Configurations.BOOTSTRAPIP);
            DatagramPacket sendPacket = new DatagramPacket(sendBytes,sendBytes.length,ip, Configurations.BOOTSTRAPPORT);
            this.sock.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveBytes,receiveBytes.length);
            this.sock.receive(receivePacket);

            String receiveData = new String(receivePacket.getData(),0,receivePacket.getLength());
//            System.out.println(receiveData);

//            this.sock.close();

            return receiveData;

        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        } catch (UnknownHostException e){
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public String unreg(String command) {

        try {
            command = String.format("%04d",command.length()+5)+" "+command;
            byte[] sendBytes = command.getBytes();
            byte[] receiveBytes = new byte[65536];

            if(this.ipaddress == null && this.ip == null){
                System.err.println("NodeExecution.DSConnection: Must call init before calling connectToBootstrap");
                return null;
            }

            InetAddress ip = InetAddress.getByName(Configurations.BOOTSTRAPIP);
            DatagramPacket sendPacket = new DatagramPacket(sendBytes,sendBytes.length,ip,Configurations.BOOTSTRAPPORT);
            this.sock.send(sendPacket);
            this.sock.setSoTimeout(Configurations.TTL);

            DatagramPacket receivePacket = new DatagramPacket(receiveBytes,receiveBytes.length);

            this.sock.receive(receivePacket);

            String receiveData = new String(receivePacket.getData(),0,receivePacket.getLength());
            return receiveData;

        } catch (SocketTimeoutException e){
            synchronized (this) {
                listenerEnabled = false;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public abstract void Send(String command, String ip, int port);

    public abstract void listen(final String ip, final int port);

    public DatagramSocket getSock(){
        if(this.sock.isClosed())
            try {
                this.sock = new DatagramSocket(this.port,this.ipaddress);
            } catch (SocketException e) {
                e.printStackTrace();
            }

        return this.sock;
    }

    public MessageListener getListener(){
        return this.listener;
    }

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
