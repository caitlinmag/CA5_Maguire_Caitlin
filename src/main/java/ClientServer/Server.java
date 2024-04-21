package ClientServer;
import org.example.DAOs.JsonConverter;
import com.google.gson.Gson;

import java.net.SocketException;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.Employee;
import org.example.DTOs.Products;
import org.example.Exceptions.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
                //Finding which client is connected
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
    private static DataInputStream dataInputStream = null;
    private static DataOutputStream dataOutputStream = null;
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
        Gson gsonParser = new Gson();

/**
 * Main Author : Rory O'Gorman
 *
 * Feature 9: Display a single entity
 */

        try {
            while ((request = socketReader.readLine()) != null) {
                System.out.println("Server (ClientHandler): Read command from client " + clientNum + ": " + request);
                if (request.startsWith("1")) {
                    System.out.println("\nCall Employees as a JSON string");
                    int employeeID = Integer.parseInt(socketReader.readLine());
                    try {
                        Employee employee = ed.findEmployeeById(employeeID);
                        jsonConverter.singleEmployeeToJson(employee);
                        socketWriter.println(employee);
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
/**
 * Main Author : Caitlin Maguire
 *
 * Feature 10: Display all entities
 */
                } else if (request.startsWith("2")) {
                    List<Employee> employeesList = ed.getAllEmployees(); // get all of the employees first from the ArrayList using the getAllEmployees() method

                    try{
                        if(employeesList.isEmpty()){
                            System.out.println("Sever message: No employees exist in the database. ");
                            socketWriter.println("No employees have been found in the database.");
                        }else{
                            clientResponse = jsonConverter.employeesListToJson(employeesList);      // call the employeesListToJson method using the jsonConverter object

                            socketWriter.println(clientResponse);     // sending the response back to the client - response is displaying all entities in json format

                            System.out.println("Server message: display all entities response sent to client.");
                        }
                    }catch(DaoException ex){
                        socketWriter.println("Failed to Display all Entities. " + ex.getMessage());
                    }

                } else if (request.startsWith("3")) {

                    clientResponse=socketReader.readLine();
                    Employee e=gsonParser.fromJson(clientResponse, Employee.class);
                    try {
                        ed.InsertEmployee(e);
                        socketWriter.println("Sucessfully added employee to database: \n"+gsonParser.toJson(e));
                    }catch (DaoException ex){
                        socketWriter.println(gsonParser.toJson("Failed to add employee to database: "+ex.getMessage()));
                    }

                }else if (request.startsWith("4")) {
                    int employeeId = Integer.parseInt(socketReader.readLine());  // reading in the Employee ID
                    System.out.println("Recieved Employee ID :" + employeeId);

                    try {
                        Employee employee = ed.findEmployeeById(employeeId);

                        if(employee != null){
                            ed.DeleteEmployee(employeeId);     // calling the deleteEmployee method and passing in the Employee ID
                            socketWriter.println("Successfully deleted employee from the database. Employee ID = " + gsonParser.toJson(employeeId));  // sending message to client - employee exists and has been deleted
                        }else{
                            System.out.println("Server Message: An Employee with the ID: " + employeeId + " does not exist in the Database.");
                            socketWriter.println("An Employee with the ID: " + employeeId + " does not exist in the Database.");        // sending message to client - employeeID doesn't exist
                        }

                    } catch (DaoException ex) {
                        socketWriter.println("Failed to delete Employee." + ex.getMessage());
                    }


                }else if (request.startsWith("5")) {
                    ArrayList<String> images = new ArrayList<>();
                    images.add("images/Dkit.jpg");
                    images.add("images/Dog.jpg");
                    images.add("images/Oracle.png");
                    images.add("images/Github.png");
                    //Sending list of images to client in a JSON Format
                    socketWriter.println(gsonParser.toJson(images));
                    String clientUserInput=socketReader.readLine();
                    if (clientUserInput.equalsIgnoreCase("DkIT") || clientUserInput.equalsIgnoreCase("Dog") || clientUserInput.equalsIgnoreCase("Oracle") || clientUserInput.equalsIgnoreCase("Github")) {
                        dataInputStream = new DataInputStream(clientSocket.getInputStream());
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        sendFile(clientUserInput, images);
                        dataInputStream.close();
                        dataInputStream.close();
                    }
                }else if (request.startsWith("6")){
                    int employeeID = Integer.parseInt(socketReader.readLine());
                    List<Products> productsList = ed.getAllProductsBasedOnEmployeeID(employeeID); // get all of the employees first from the ArrayList using the getAllEmployees() method
                    clientResponse=gsonParser.toJson(productsList);
                    socketWriter.println(clientResponse);     // sending the response back to the client - response is displaying all entities in json format
                    System.out.println("Server message: display products response sent to client.");
                }
                else if (request.startsWith("0")){
                    socketWriter.println("Goodbye");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                } else {
                    socketWriter.println("error I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        }
        catch (SocketException ex) {
            // Had to include SocketException to resolve issue with client shutting down after running images list
            System.out.println("SocketException occurred: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();

        }catch(DaoException ex){
            System.out.println("Failed to add employee to database: "+ex.getMessage());
        } catch (Exception e) {
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
    private static void sendFile(String userInput, ArrayList<String> path) throws Exception {
        int bytes = 0;
        File file;
        if (userInput.equalsIgnoreCase("DkIT")) {
            file = new File(path.get(0));
        } else if (userInput.equalsIgnoreCase("Dog")) {
            file = new File(path.get(1));
        } else if (userInput.equalsIgnoreCase("Oracle")) {
            file = new File(path.get(2));
        } else {
            file = new File(path.get(3));
        }
        FileInputStream fileInputStream = new FileInputStream(file);
// send the length (in bytes) of the file to the client

        dataOutputStream.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024]; //4 kilobyte buffer
        // read bytes from file into the buffer until buffer is full or we reached end of file
        while ((bytes = fileInputStream.read(buffer))!= -1) {
            // Send the buffer contents to Client Socket, along with the count of the number of bytes
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }

        // close the file
        fileInputStream.close();
    }
}

