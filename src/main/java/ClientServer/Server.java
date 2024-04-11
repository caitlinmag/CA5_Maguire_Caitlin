package ClientServer;

import org.example.DAOs.MySqlEmployeeDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
            System.out.println("Server has started");
            int clientNum = 0;
            while (true) {
                System.out.println("Server: Listening/waiting for connections on port ..." + SERVER_PORT_NUMBER);
                clientSocket = serverSocket.accept();
                clientNum++;
                System.out.println("Server: Listening for connections on port...." + SERVER_PORT_NUMBER);
                System.out.println("Server: Client " + clientNum + " has connected");
                System.out.println("Server: Port number of remote client: " + clientSocket.getPort());
                System.out.println("Server: Port number of the socket used to talk with client " + clientSocket.getLocalPort());
                Thread t = new Thread(new ClientHandler(clientSocket, clientNum));
                t.start();
                System.out.println("Server: ClientHandler started in thread " + t.getName() + "for client" + clientNum + ".");
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();

                }
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        System.out.println("Server is now exiting.");
    }
}

class ClientHandler implements Runnable {
    MySqlEmployeeDao ed = new MySqlEmployeeDao();
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;
    final int clientNum;

    public ClientHandler(Socket clientSocket, int clientNum) {
        this.clientSocket = clientSocket;
        this.clientNum = clientNum;
        try {
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;
        try {
            while ((request = socketReader.readLine()) != null) {
                System.out.println("Server (ClientHandler): Read command from client " + clientNum + ": " + request);
                if (request.startsWith("1")) {
                    System.out.println("TEST");
                } else if (request.startsWith("2")) {
                    System.out.println("TEST");
                } else if (request.startsWith("3")) {
                    String message="TEST";
                    socketWriter.println(message);
                }else if (request.startsWith("0")){
                    socketWriter.println("Goodbye");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }else {
                    socketWriter.println("error I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            this.socketWriter.close();
            try {
                this.socketReader.close();
                this.clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNum + " is terminating .....");
    }
}