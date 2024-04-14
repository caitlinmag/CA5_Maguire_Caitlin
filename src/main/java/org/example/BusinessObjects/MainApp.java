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

        do {
            // calling method to display the menu
            displayMainMenu();

            // menu options
            choice = key.nextInt();
            switch (choice) {
                case 1: {
                    //FEATURE 1: Display and return all employees
                    callFeature1();
                    break;
                }
                case 2: {
                    // FEATURE 2 : Display an employee
                    callFeature2();
                    break;
                }
                case 3: {
                    // TODO : not catching an employee id which does not exist - Also maybe show the details before deleting?
                    // FEATURE 3 : Delete an employee by id
                    callFeature3();
                    break;
                }
                case 4: {
                    // FEATURE 4 : Add an employee
                    //TODO: just a thought when a new employee is created the employee id is displayed as 0 but its not actually 0
                    // eg i made a new employee it showed up as emp 0 when really it was 11 - can that be changed?
                    callFeature4();
                    break;
                }
                case 5: {
                    //FEATURE 5 : Update an employee
                    //TODO: maybe show the employee details of whichever employee the user input - so they can see current details before changing?
                    callFeature5();
                    break;
                }
                case 6: {
                    //TODO: filter for first name doesnt include lowercase - i made an employee called caitlin and it was the last one
                    Filters();
                }
                break;
                case 7: {
                    //FEATURE 7 : Convert list of employees to json
                    callFeature7();
                    break;
                }
                case 8: {
                    //FEATURE 8 : Convert an employee entity to json
                    //TODO: not catching employees that dont exist - comes up as null
                    callFeature8();
                    break;
                }
            }
        } while (choice != 0);
    }

    // Method to display the menu options
    public static void displayMainMenu() {
        System.out.println();
        System.out.println("    *********************** RETAIL STORE EMPLOYEES MENU ***********************");
        System.out.println("    *                                                                         *");
        System.out.println("    *   1. Get all Entities (Employees)                                       *");
        System.out.println("    *   2. Display an Entity by Key (Employee ID)                             *");
        System.out.println("    *   3. Delete an Entity by Key (Employee ID)                              *");
        System.out.println("    *   4. Insert an Entity                                                   *");
        System.out.println("    *   5. Update an existing Entity by ID                                    *");
        System.out.println("    *   6. Filter Entities (Employees)                                        *");
        System.out.println("    *   7. Display a JSON String of all Employees                             *");
        System.out.println("    *   8. Display a JSON String of a Single Employee                         *");
        System.out.println("    *   0. Exit                                                               *");
        System.out.println("    *                                                                         *");
        System.out.println("    ***************************************************************************");
    }

    // Methods for calling features 1 - 8
    public static void callFeature1() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

        try {
            System.out.println("\n* DISPLAYING ALL EMPLOYEES *\n");
            List<Employee> employeesList = IEmployeeDao.getAllEmployees();

            if (employeesList.isEmpty())
                System.out.println("There are no employees");

        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public static void callFeature2() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

        try {
            Scanner kbrd = new Scanner(System.in);
            System.out.println("* DISPLAY AN EMPLOYEE *\n");
            System.out.println("Please enter an Employee ID: ");
            int employeeID = kbrd.nextInt();
            Employee employee = IEmployeeDao.findEmployeeById(employeeID);

            if (employee != null) // null returned if userid and password not valid
                System.out.println("An Employee with the ID: " + employeeID + " has been found. \n" + employee);
            else
                System.out.println("Employee with the ID: " + employeeID + " has not been found. ");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void callFeature3() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
        Scanner key = new Scanner(System.in);

        int id;
        System.out.println("* DELETE AN EMPLOYEE *\n");
        System.out.println("Enter the id of the employee you wish to delete: ");
        id = key.nextInt();
        try {
//            System.out.println("\n Calling DeleteEmployee()\n");
            int deletedId = IEmployeeDao.DeleteEmployee(id);
            System.out.println("Employee with ID " + deletedId + " has been deleted successfully.");
            IEmployeeDao.getAllEmployees();
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public static void callFeature4() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
        Scanner key = new Scanner(System.in);

        String firstName;
        String lastName;
        int age;
        String department;
        String role;
        float hourlyRate;


        System.out.println("* ADD AN EMPLOYEE *\n");
        System.out.println("Please Enter New Employee Details: \n");

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
//            System.out.println("\n Calling InsertEmployee()\n");
            Employee employee = IEmployeeDao.InsertEmployee(e);
            System.out.println("Employee inserted successfully");
            System.out.println("Employee: " + employee);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public static void callFeature5() {
        Scanner key = new Scanner(System.in);
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

        String firstName;
        String lastName;
        int age;
        String department;
        String role;
        float hourlyRate;

        System.out.println("* UPDATE EMPLOYEE DETAILS *\n");
        int empID;
        System.out.println("Enter the Employee ID of the employee you wish to update: ");
        empID = key.nextInt();
        key.nextLine();
        boolean fin = false;
        Employee updateEmployee = new Employee(empID);
        String userInput;
        do {
            System.out.println("Select which field you would like to update (firstName, lastName, age, department, role, hourlyRate): ");
            userInput = key.nextLine();

            if (userInput.equalsIgnoreCase("firstName")) {
                System.out.println("Enter new first name: ");
                firstName = key.nextLine();
                updateEmployee.setFirstName(firstName);

            } else if (userInput.equalsIgnoreCase("lastName")) {
                System.out.println("Enter new last name: ");
                lastName = key.nextLine();
                updateEmployee.setLastName(lastName);

            } else if (userInput.equalsIgnoreCase("age")) {
                System.out.println("Enter new age: ");
                age = key.nextInt();
                updateEmployee.setAge(age);
                key.nextLine();

            } else if (userInput.equalsIgnoreCase("department")) {
                System.out.println("Enter new department: ");
                department = key.nextLine();
                updateEmployee.setDepartment(department);

            } else if (userInput.equalsIgnoreCase("role")) {
                System.out.println("Enter new role: ");
                role = key.nextLine();
                updateEmployee.setRole(role);

            } else if (userInput.equalsIgnoreCase("hourlyRate")) {
                System.out.println("Enter new hourly rate: ");
                hourlyRate = key.nextFloat();
                updateEmployee.setHourlyRate(hourlyRate);
                key.nextLine();

            } else {
                System.out.println("Invalid option");
            }

            System.out.println("Would you like to update another field? (yes or no): ");
            userInput = key.nextLine();

            if (userInput.equalsIgnoreCase("yes")) {
                fin = true;
            } else if (userInput.equalsIgnoreCase("no")) {
                fin = false;
            } else {
                System.out.println("Invalid option");
            }
        } while (fin);

        try {
//            System.out.println("\nCalling updateEmployee()\n");
            Employee employee = IEmployeeDao.updateEmployee(empID, updateEmployee);
            System.out.println("Employee updated successfully");
            System.out.println("Updated Employee: " + IEmployeeDao.findEmployeeById(employee.getEmpID()));
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main Author : Jamie Lawlor
     */
    public static void Filters() {
        EmployeeDaoInterface VIEmployeeDao = new MySqlEmployeeDao();
        Scanner key = new Scanner(System.in);
        String input;
        List<Employee> employeesList;

        System.out.println("* FILTER EMPLOYEES *\n");

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

    public static void callFeature7() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
        JsonConverter jsonConverter = new JsonConverter();

        System.out.println("\n* DISPLAY ALL EMPLOYEES IN JSON FORMAT *\n");

        try {
            List<Employee> employeesList = IEmployeeDao.getAllEmployees();       // using the getAllEmployees() method to access all employees
            String jsonString = jsonConverter.employeesListToJson(employeesList);  // call the employeesListToJson method using the jsonConverter object
            System.out.println(jsonString);    // output json string of employees list

        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public static void callFeature8() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
        JsonConverter jsonConverter = new JsonConverter();

        System.out.println("\n* DISPLAY AN EMPLOYEE IN JSON FORMAT *\n");

        try {
            Scanner kbrd = new Scanner(System.in);
            System.out.println("Please enter an Employee ID");
            int employeeID = kbrd.nextInt();
            Employee employee = IEmployeeDao.findEmployeeById(employeeID);
            String jsonString = jsonConverter.singleEmployeeToJson(employee);
            System.out.println(jsonString);

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

}


