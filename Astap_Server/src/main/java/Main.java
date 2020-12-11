import Models.*;
import Services.*;
import Utils.ClientHandler;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class Main {

    private static final int PORT_NUMBER = 5001;
    private static ServerSocket serverSocket;
    private static ClientHandler clientHandler;
    private static Thread thread;
    private static List<Socket> sockets= new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(PORT_NUMBER);
        while (true) {
            for (Socket socket:
                 sockets) {
                String socketInfo = socket.getInetAddress()+":"+socket.getPort()+" подключен.";
                System.out.println(socketInfo);
            }
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            clientHandler = new ClientHandler(socket);
            thread = new Thread(clientHandler);
            thread.start();
            System.out.flush();
        }
    }

    protected void finalize() throws IOException {
        serverSocket.close();
    }
}
