package org.example.BusinessObjects;

import org.example.DAOs.EmployeeDaoInterface;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.*;
import org.example.Exceptions.DaoException;
import org.example.DAOs.JsonConverter;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner key = new Scanner(System.in);
        int choice;

        String firstName;
        String lastName;
        int age;
        String department;
        String role;
        float hourlyRate;

        do {
            System.out.println("************ EMPLOYEES MENU ************");
            System.out.println("\n1. Get all entities");
            System.out.println("2. Get entity by id");
            System.out.println("3. Delete entity by id");
            System.out.println("4. Insert an entity");
            System.out.println("5. Update an entity based on ID");
            System.out.println("6. Filter entities");
            System.out.println("7. JSON String of Employees");
            System.out.println("8. JSON String of Single Employee");
            System.out.println("0. Exit");
            System.out.println("******************************************");

            choice = key.nextInt();
            switch (choice) {
                case 1: {
                    //Feature 1: Display and return all employees
                    try {
                        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

                        System.out.println("\nCall getAllEmployees()");
                        List<Employee> employeesList = IEmployeeDao.getAllEmployees();

                        if (employeesList.isEmpty())
                            System.out.println("There are no employees");
                        else {
                            for (Employee e : employeesList)
                                System.out.println("Employee: " + e.toString());
                        }
                    } catch (DaoException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }

                case 2: {
                    EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
                    try {
                        Scanner kbrd = new Scanner(System.in);
                        System.out.println("\nCall: findEmployeeByID()");
                        System.out.println("Please enter ID");
                        int employeeID = kbrd.nextInt();
                        Employee employee = IEmployeeDao.findEmployeeById(employeeID);

                        if (employee != null) // null returned if userid and password not valid
                            System.out.println("Employee found: " + employee);
                        else
                            System.out.println("Employee not found");
                    } catch (DaoException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case 3: {
                    EmployeeDaoInterface IIIEmployeeDao = new MySqlEmployeeDao();
                    int id;
                    System.out.println("Enter the id of the employee you wish to delete: ");
                    id = key.nextInt();
                    try {
                        System.out.println("\n Calling DeleteEmployee()\n");
                        int deletedId = IIIEmployeeDao.DeleteEmployee(id);
                        System.out.println("Employee with id " + deletedId + " deleted successfully");
                        IIIEmployeeDao.getAllEmployees();
                    } catch (DaoException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    EmployeeDaoInterface IVEmployeeDao = new MySqlEmployeeDao();
                 
                    System.out.print("First name: ");
                    firstName = key.next();
                    System.out.print("Last name: ");
                    lastName = key.next();
                    System.out.print("Age: ");
                    age = key.nextInt();
                    /*This line fixes issue with nextInt() and nextLine() that was skipping over a line of input,
                     so inputs are read properly now.
                     */
                    key.nextLine();
                    System.out.print("Department: ");
                    department = key.nextLine();
                    System.out.print("Role: ");
                    role = key.nextLine();
                    System.out.print("Hourly rate: ");
                    hourlyRate = key.nextFloat();
                    Employee e = new Employee(0, firstName, lastName, age, department, role, hourlyRate);

                    try {

                        System.out.println("\n Calling InsertEmployee()\n");
                        Employee employee = IVEmployeeDao.InsertEmployee(e);
                        System.out.println("Employee inserted successfully");
                        System.out.println("Employee: " + employee);
                    } catch (DaoException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case 5: {
                    EmployeeDaoInterface IUEmployeeDao = new MySqlEmployeeDao();
                    int empID;
                    System.out.println("Enter the ID of the employee you wish to update: ");
                    empID = key.nextInt();
                    key.nextLine();
                    boolean fin = false;
                    Employee updateEmployee = new Employee(empID);
                    String userInput;
                    do {
                        System.out.println("Select which field you would like to update (firstName, lastName, age, department, role, hourlyRate): ");
                        userInput= key.nextLine();
                        if (userInput.equalsIgnoreCase("firstName")){
                            System.out.println("Enter new first name: ");
                            firstName=key.nextLine();
                            updateEmployee.setFirstName(firstName);
                        }else if (userInput.equalsIgnoreCase("lastName")){
                            System.out.println("Enter new last name: ");
                            lastName=key.nextLine();
                            updateEmployee.setLastName(lastName);
                        }else if (userInput.equalsIgnoreCase("age")){
                            System.out.println("Enter new age: ");
                            age=key.nextInt();
                            updateEmployee.setAge(age);
                            key.nextLine();
                        }else if (userInput.equalsIgnoreCase("department")){
                            System.out.println("Enter new department: ");
                            department=key.nextLine();
                            updateEmployee.setDepartment(department);
                        }else if (userInput.equalsIgnoreCase("role")){
                            System.out.println("Enter new role: ");
                            role=key.nextLine();
                            updateEmployee.setRole(role);
                        }else if (userInput.equalsIgnoreCase("hourlyRate")){
                            System.out.println("Enter new hourly rate: ");
                            hourlyRate=key.nextFloat();
                            updateEmployee.setHourlyRate(hourlyRate);
                            key.nextLine();
                        }else{
                            System.out.println("Invalid option");
                        }
                        System.out.println("Would you like to update another field? (yes or no): ");
                        userInput=key.nextLine();
                        if (userInput.equalsIgnoreCase("yes")){
                            fin=true;
                        }else if (userInput.equalsIgnoreCase("no")){
                            fin=false;
                        }else{
                            System.out.println("Invalid option");
                        }
                    } while (fin);
                    try {
                        System.out.println("\nCalling updateEmployee()\n");
                        Employee employee = IUEmployeeDao.updateEmployee(empID, updateEmployee);
                        System.out.println("Employee updated successfully");
                        System.out.println("Updated Employee: " + IUEmployeeDao.findEmployeeById(employee.getEmpID()));
                    } catch (DaoException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case 6: {
                    Filters();
                }
                break;
                case 7: {
                    EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

                    //create a json converter object
                    JsonConverter jsonConverter = new JsonConverter();

                    System.out.println("\nCall All Employees as a JSON string");

                    try {
                        List<Employee> employeesList = IEmployeeDao.getAllEmployees();  //get all of the employees first from the ArrayList using the getAllEmployees() method
                        String jsonString=jsonConverter.employeesListToJson(employeesList);               //call the employeesListToJson method using the jsonConverter object
                        System.out.println("Json String of employees : \n" + jsonString); // output json string of employees list
                    } catch (DaoException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }

                case 8: {
                    EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

                    //create a json converter object
                    JsonConverter jsonConverter = new JsonConverter();

                    System.out.println("\nCall an Employee as a JSON string");

                    try {
                        Scanner kbrd = new Scanner(System.in);
                        System.out.println("Please enter ID");
                        int employeeID = kbrd.nextInt();
                        Employee employee = IEmployeeDao.findEmployeeById(employeeID);
                        jsonConverter.singleEmployeeToJson(employee);

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        } while (choice != 0);

    }

    public static void Filters() {
        Scanner key = new Scanner(System.in);
        EmployeeDaoInterface VIEmployeeDao = new MySqlEmployeeDao();
        String input;
        List<Employee> employeesList;
        System.out.println("What would you like to filter entities by (firstName, lastName, age, department, role, hourlyRate): ");
        input = key.nextLine();
        if (!input.equals("firstName") && !input.equals("lastName") && !input.equals("age") && !input.equals("department") && !input.equals("role") && !input.equals("hourlyRate")) {
            System.out.println("\nInvalid option... make sure to check that your option is inputted as displayed.");
        } else {
            try {
                switch (input) {
                    case "firstName": {
                        FirstNameComparator comp = new FirstNameComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                    case "lastName": {
                        LastNameComparator comp = new LastNameComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                    case "age": {
                        AgeComparator comp = new AgeComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                    case "department": {
                        DepartmentComparator comp = new DepartmentComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                    case "role": {
                        RoleComparator comp = new RoleComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                    case "hourlyRate": {
                        HourlyRateComparator comp = new HourlyRateComparator();
                        employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                        for (Employee e : employeesList)
                            System.out.println("Employee: " + e.toString());
                        break;
                    }
                }
            } catch (DaoException ex) {
                ex.printStackTrace();
            }

        }
    }
}


