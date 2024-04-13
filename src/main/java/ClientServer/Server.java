package ClientServer;

import org.example.DAOs.EmployeeDaoInterface;
import org.example.DAOs.JsonConverter;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
        String request; // request from the client
        String clientResponse; // message to send back to the client

        // creating objects to be able to call DAO methods
        MySqlEmployeeDao ed = new MySqlEmployeeDao();
        JsonConverter jsonConverter = new JsonConverter();

        try {
            while ((request = socketReader.readLine()) != null) {
                System.out.println("Server (ClientHandler): Read command from client " + clientNum + ": " + request);
                if (request.startsWith("1")) {
                    System.out.println("TEST");

/**
 * Main Author : Caitlin Maguire
 *
 * Feature 10: Display all entities
 */
                } else if (request.startsWith("2")) {
                    List<Employee> employeesList = ed.getAllEmployees(); // get all of the employees first from the ArrayList using the getAllEmployees() method

                    clientResponse = jsonConverter.employeesListToJson(employeesList);      // call the employeesListToJson method using the jsonConverter object

                    socketWriter.println(clientResponse);     // sending the response back to the client - response is displaying all entities in json format

                    System.out.println("Server message: display all entities response sent to client.");

                } else if (request.startsWith("3")) {
                    String message = "TEST";
                    socketWriter.println(message);
                } else if (request.startsWith("0")) {
                    socketWriter.println("Goodbye");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                } else {
                    socketWriter.println("error I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        } finally {
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

