package org.example.BusinessObjects;

import org.example.DAOs.EmployeeDaoInterface;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.*;
import org.example.Exceptions.DaoException;
import org.example.DAOs.JsonConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner key = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n1. Get all entities");
            System.out.println("2. Get entity by id");
            System.out.println("3. Delete entity by id");
            System.out.println("4. Insert an entity");
            System.out.println("5. Update an entity based on ID");
            System.out.println("6. Filter entities");
            System.out.println("7. JSON String of Employees");

            System.out.println("0. Exit");

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
                    String firstName;
                    String lastName;
                    int age;
                    String department;
                    String role;
                    float hourlyRate;
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

                    System.out.println("Select fields to update (comma-separated, e.g., firstName, lastName, age, department, role, hourlyRate): ");
                    String fieldsInput = key.nextLine();

                    String[] fields = fieldsInput.split(",");
                    List<String> fieldsToUpdate = Arrays.asList(fields);

                    Employee updatedEmployee = new Employee(0, null, null, 0, null, null, 0.0f);

                    updatedEmployee.setEmpID(empID);
                    for (String field : fieldsToUpdate) {
                        switch (field) {
                            case "firstName":
                                System.out.print("Enter new first name: ");
                                updatedEmployee.setFirstName(key.nextLine());
                                break;
                            case "lastName":
                                System.out.print("Enter new last name: ");
                                updatedEmployee.setLastName(key.nextLine());
                                break;
                            case "age":
                                System.out.print("Enter new age: ");
                                updatedEmployee.setAge(key.nextInt());
                                key.nextLine();
                                break;
                            case "department":
                                System.out.print("Enter new department: ");
                                updatedEmployee.setDepartment(key.nextLine());
                                break;
                            case "role":
                                System.out.print("Enter new role: ");
                                updatedEmployee.setRole(key.nextLine());
                                break;
                            case "hourlyRate":
                                System.out.print("Enter new hourly rate: ");
                                updatedEmployee.setHourlyRate(key.nextFloat());
                                key.nextLine();
                                break;
                        }
                    }
                    try {
                        System.out.println("\nCalling updateEmployee()\n");
                        Employee employee = IUEmployeeDao.updateEmployee(updatedEmployee, fieldsToUpdate);
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
                    System.out.println("\nCall Employees as a JSON string");

                    try {
                        List<Employee> employeesList = IEmployeeDao.getAllEmployees();  //get all of the employees first from the ArrayList using the getAllEmployees() method
                        String employeesJson = IEmployeeDao.employeesListToJson(employeesList);
                        System.out.println(employeesJson);
                    } catch (DaoException ex) {
                        ex.printStackTrace();
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


