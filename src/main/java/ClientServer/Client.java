package ClientServer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTOs.Employee;
import org.example.DTOs.Products;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
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
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Gson gsonParser = new Gson();
            System.out.println("The client is running and has connected to the server");
            Scanner key = new Scanner(System.in);

            displayClientMenu();

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
                    // receiving the json string of employees from server
                    String serverResponse = in.readLine();
                    Type employeeListType = new TypeToken<ArrayList<Employee>>() {
                    }.getType();    // Using TypeToken for the gson parser to create arraylist of employees
                                    // used to let the gson parser know the type of object to create

                    List<Employee> employeesList = new Gson().fromJson(serverResponse, employeeListType);   // parse the ArrayList from json

                    System.out.println(" * ALL EMPLOYEES OF THE RETAIL STORE * \n");

                    // iterate through employees and display each employee using the toString() method
                    for (Employee employee : employeesList)
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

                    String jsonRequest = gsonParser.toJson(newEmployee);
                    out.println(jsonRequest);

                    String newEmployeeJson = in.readLine();
                    System.out.println(newEmployeeJson);

                } else if (userRequest.startsWith("4")) {
                    System.out.println(" * DELETE AN EMPLOYEE OF THE RETAIL STORE * \n");
                    System.out.println("Please enter the Employee ID of the employee you wish to delete:");

                    int employeeId = key.nextInt();   // taking in the employee ID the user wants to delete

                    String jsonDeleteEmployee = gsonParser.toJson(employeeId);  // converting employeeId to json

                    out.println(jsonDeleteEmployee);

                    String serverResponse = in.readLine();  // reading in the response from server

                    System.out.println(serverResponse);

                }else if (userRequest.startsWith("5")) {
                    boolean validInput = false;

                    while (!validInput) {
                        System.out.println("Which image would you like to select?: DkIT, Dog, Oracle, Github");
                        String userInput = key.nextLine();
                        if (userInput.equalsIgnoreCase("DkIT")) {
                            //Sending user's choice over to the server socket to identify the correct image
                            out.println(userInput);
                            /*Bug in code where the dataOutputStream is reading the length incorrectly,
        Have tried to troubleshoot but unsuccessful, the length is read properly on the server and the buffer
        is returning 4096 and then returns 3347 bytes and breaks out of loop. It doesn't cycle through all the bytes for some reason
        the dataInputStream cannot pick up the correct length.
        * */
                            receiveFile("images/Dkit_received.jpg");
                            validInput = true;
                        } else if (userInput.equalsIgnoreCase("Dog")) {
                            out.println(userInput);
                            receiveFile("images/Dog_received.jpg");
                            validInput = true;

                        } else if (userInput.equalsIgnoreCase("Oracle")) {
                            out.println(userInput);
                            validInput = true;
                            receiveFile("images/Oracle_received.png");

                        }else if (userInput.equalsIgnoreCase("Github")){
                            out.println(userInput);
                            validInput = true;
                            receiveFile("images/Github_received.png");

                        }else {
                            System.out.println("Invalid image....Please try again");
                        }

                    }

                }else if (userRequest.startsWith("6")){

                    Scanner kbrd = new Scanner(System.in);
                    boolean idCheck=false;
                    do {
                        System.out.println("Please enter an Employee ID that oversees products (2,4,5,6): ");
                        int employeeID = kbrd.nextInt();
                        if (employeeID == 2 || employeeID == 4 || employeeID == 5 || employeeID == 6) {
                            out.println(employeeID);
                            String serverResponse = in.readLine();
                            Type productsListType = new TypeToken<ArrayList<Products>>() {
                            }.getType();    // Using TypeToken for the gson parser to create arraylist of employees

                            List<Products> productsList = new Gson().fromJson(serverResponse, productsListType);   // parse the ArrayList from json

                            System.out.println(" * Products that employee "+employeeID+" is in charge of * \n");

                            // iterate through employees and display each employee using the toString() method
                            for (Products p : productsList) {
                                System.out.println(p.toString());
                            }
                            idCheck=true;
                        }else{
                            System.out.println("This employee doesn't oversee any products\n");
                        }
                    } while (!idCheck);

                }
                else if (userRequest.startsWith("0")) {// if the user has entered the "quit" command

                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                } else {
                    System.out.println("Unknown command... please try again.");
                }
                key = new Scanner(System.in);

                // multithreading the client menu

                displayClientMenu();

                userRequest = key.nextLine();
            }
        } catch (Exception ex) {
            System.out.println("Client message: IOException: " + ex);
        }
        System.out.println("Exiting client, but server may still be running.");

    }
    /**
     * Main Author : Rory O'Gorman
     */

    public void displayClientMenu(){
        System.out.println("\n1. Display entity by Employee ID");
        System.out.println("2. Display all entities (Employees)");
        System.out.println("3. Add an entity (Employee)");
        System.out.println("4. Delete an entity (By ID)");
        System.out.println("5. Get Images List");
        System.out.println("6. Display products that an employee oversees");
        System.out.println("0. Quit");

        System.out.println("Please enter your choice: ");
    }

    private static void receiveFile(String fileName)
            throws Exception
    {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        // DataInputStream allows us to read Java primitive types from stream e.g. readLong()
        // read the size of the file in bytes (the file length)
        long size = dataInputStream.readLong();
        System.out.println("Server: file size in bytes = " + size);

        // create a buffer to receive the incoming bytes from the socket
        byte[] buffer = new byte[4 * 1024];         // 4 kilobyte buffer

        System.out.println("Server:  Bytes remaining to be read from socket: ");

        // next, read the raw bytes in chunks (buffer size) that make up the image file
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0,(int)Math.min(buffer.length, size))) != -1) {

            // above, we read a number of bytes from stream to fill the buffer (if there are enough remaining)
            // - the number of bytes we must read is the smallest (min) of: the buffer length and the remaining size of the file
            //- (remember that the last chunk of data read will usually not fill the buffer)

            // Here we write the buffer data into the local file
            fileOutputStream.write(buffer, 0, bytes);

            // reduce the 'size' by the number of bytes read in.
            // 'size' represents the number of bytes remaining to be read from the socket stream.
            // We repeat this until all the bytes are dealt with and the size is reduced to zero
            size = size - bytes;
            System.out.println(size + ", ");
        }

        System.out.println("File is Received");

        System.out.println("Look in the images folder to see the transferred file: parrot_image_received.jpg");
        fileOutputStream.close();
    }
}


