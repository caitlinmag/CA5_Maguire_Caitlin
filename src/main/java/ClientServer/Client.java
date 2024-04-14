package ClientServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTOs.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("\n1. Display entity by Employee ID");
            System.out.println("2. Display all Entities (Employees)");
            System.out.println("3. Add an entity (Employee)");
            System.out.println("0. Quit");
            System.out.println("Please enter your choice: ");
            String userRequest = key.nextLine();

            while (true) {
                // send the command to the server on the socket
                out.println(userRequest);

                // process the answer returned by the server
                if (userRequest.startsWith("1")) {
                    System.out.println(" * DISPLAY AN EMPLOYEE OF THE RETAIL STORE * \n");
                    System.out.println("Please enter an Employee ID: ");
                    Scanner kbrd = new Scanner(System.in);

                    int employeeID = kbrd.nextInt();

                    out.println(employeeID);
                    String employeeJson = in.readLine();

                    System.out.println("Selected Entity : \n" + employeeJson);

                } else if (userRequest.startsWith("2")) {
                    // recieving the json string of employees from server
                    String serverResponse = in.readLine();
                    Type employeeListType = new TypeToken<ArrayList<Employee>>(){}.getType();    // Using TypeToken for the gson parser to create arraylist of employees

                    List<Employee> employeesList = new Gson().fromJson(serverResponse, employeeListType);   // parse the ArrayList from json

                    System.out.println(" * ALL EMPLOYEES OF THE RETAIL STORE * \n");

                    // iterate through employees and display each employee using the toString() method
                    for(Employee employee : employeesList)
                        System.out.println(employee.toString());

                } else if (userRequest.startsWith("3")) {

                    String firstName, lastName, department, role;
                    int age;
                    float hourlyRate;

                    System.out.print("First name: ");
                    firstName = key.next();

                    System.out.print("Last name: ");
                    lastName = key.next();

                    System.out.print("Age: ");
                    age = key.nextInt();
                    key.nextLine();

                    System.out.print("Department: ");
                    department = key.nextLine();

                    System.out.print("Role: ");
                    role = key.nextLine();

                    System.out.print("Hourly rate: ");
                    hourlyRate = key.nextFloat();

                    Employee newEmployee = new Employee(0, firstName, lastName, age, department, role, hourlyRate);
                    Gson gsonParser=new Gson();

                    String jsonRequest=gsonParser.toJson(newEmployee);
                    out.println(jsonRequest);

                    String newEmployeeJson = in.readLine();
                    System.out.println(newEmployeeJson);

                }else if (userRequest.startsWith("0")) {// if the user has entered the "quit" command
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                } else {
                    System.out.println("Unknown command... please try again.");
                }
                key = new Scanner(System.in);

                //TODO: create a displayClientMenu() method

                // multithreading the client menu
                System.out.println("\n1. Display Entity by ID)");
                System.out.println("2. Display all Entities (Employees)");
                System.out.println("3. Add an Entity (Employee)");
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

