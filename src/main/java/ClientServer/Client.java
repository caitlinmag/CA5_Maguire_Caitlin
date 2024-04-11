package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", 8888);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("The client is running and has connected to the server");
            Scanner key = new Scanner(System.in);
            System.out.println("\n1. Display entity by id in JSON format");
            System.out.println("2. Display all entities in JSON format");
            System.out.println("3. Add an entity");
            System.out.println("0. Quit");
            System.out.println("Please enter your choice: ");
            String userRequest = key.nextLine();
            while (true) {
                // send the command to the server on the socket
                out.println(userRequest);
                // process the answer returned by the server
                if (userRequest.startsWith("1")) {
                    String employeeJson = in.readLine();
                    System.out.println("Display entity by id: " + employeeJson);
                } else if (userRequest.startsWith("2")) {
                    String employeeJson = in.readLine();
                    System.out.println("Display all entities: " + employeeJson);
                } else if (userRequest.startsWith("3")) {
                    String newEmployeeJson = in.readLine();
                    System.out.println("Add an entity: " + newEmployeeJson);
                }else if (userRequest.startsWith("0")) // if the user has entered the "quit" command
                {
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                } else {
                    System.out.println("Unknown command... please try again.");
                }
                key = new Scanner(System.in);
                System.out.println("\n1. Display entity by id in JSON format");
                System.out.println("2. Display all entities in JSON format");
                System.out.println("3. Add an entity");
                System.out.println("0. Quit");

                System.out.println("Please enter your choice: ");
                userRequest = key.nextLine();
            }
        } catch (IOException ex) {
            System.out.println("Client message: IOException: " + ex);
        }
        System.out.println("Exiting client, but server may still be running.");

    }
}

