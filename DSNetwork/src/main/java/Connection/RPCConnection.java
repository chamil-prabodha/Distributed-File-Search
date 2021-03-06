package Connection;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;


/**
 * Created by Chamil Prabodha on 29/01/2017.
 */
public class RPCConnection extends AbstractConnection {

    int messageCount=0;

    private RPCConnection(){

    }

    public static void init(String ip, int port, MessageListener listener){
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

    public static RPCConnection getConnection(){
        if (instance == null)
            instance = new RPCConnection();

        return (RPCConnection)instance;
    }


    @Override
    public void Send(String command, String ip, int port) {
        messageCount++;
        System.out.println("Meessage Count : "+messageCount);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL("http://"+ip+":"+port));
            XmlRpcClient server = new XmlRpcClient();
            server.setConfig(config);
            command = String.format("%04d",command.length()+5)+" "+command;

            Vector params = new Vector();
            params.add(0, command);

            Object result = server.execute("NodeExecution.RPCSupport.Join",params);

            String receivedata = (String) result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e){
            e.printStackTrace();
        }
    }

    @Override
    public void listen(String ip, int port) {
        WebServer server = new WebServer(port);
        XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
        PropertyHandlerMapping mapping = new PropertyHandlerMapping();
        try {
            mapping.addHandler("NodeExecution.RPCSupport", RPCSupport.class);
            xmlRpcServer.setHandlerMapping(mapping);
            server.start();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


