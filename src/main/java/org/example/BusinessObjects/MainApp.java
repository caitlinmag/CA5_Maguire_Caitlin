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
                    // FEATURE 3 : Delete an employee by id
                    callFeature3();
                    break;
                }
                case 4: {
                    // FEATURE 4 : Add an employee
                    callFeature4();
                    break;
                }
                case 5: {
                    //FEATURE 5 : Update an employee

                    callFeature5();
                    break;
                }
                case 6: {
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
                    callFeature8();
                    break;
                }
                case 9: {
                    //FEATURE 9 : Display products that employees oversee
                    callFeature9();
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
        System.out.println("    *   9. Display products that employees oversee                            *");
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
            else {
                // printing employee table headers
                System.out.println("_____________________________________________________________________________________________________________");
                System.out.printf("%-12s %-12s %-12s %-12s %-26s %-18s %-12s%n", "Employee ID", "Firstname", "Surname", "Age", "Department",
                        "Job-Role", "Hourly-Rate");
                System.out.println("_____________________________________________________________________________________________________________");

                // printing each employee entity
                for (Employee e : employeesList)
                    System.out.printf("%-12d %-12s %-12s %-5d %-26s %-26s €%.2f%n",
                            e.getEmpID(), e.getFirstName(), e.getLastName(), e.getAge(), e.getDepartment(), e.getRole(), e.getHourlyRate());
            }
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
            for (Employee e: IEmployeeDao.getAllEmployees()){
                if (id==e.getEmpID()){
                    System.out.println("----------------------------");
                    System.out.println("FIRST NAME: "+e.getFirstName());
                    System.out.println("LAST NAME: "+e.getLastName());
                    System.out.println("AGE: "+e.getAge());
                    System.out.println("DEPARTMENT: "+e.getDepartment());
                    System.out.println("ROLE: "+e.getRole());
                    System.out.println("HOURLY RATE: "+e.getHourlyRate());
                    System.out.println("----------------------------");

                    int deletedId = IEmployeeDao.DeleteEmployee(id);
                    System.out.println("Employee with ID " + deletedId + " has been deleted successfully.");
                    break;
                }
                if (IEmployeeDao.findEmployeeById(id)==null){
                    System.out.println("Employee does not exist in database");
                }
            }
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public static void callFeature4() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();
        Scanner key = new Scanner(System.in);

        String firstName, lastName, department , role;
        int age;
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

        try {
            int empID=IEmployeeDao.getAllEmployees().size()+1;
            Employee e = new Employee(empID, firstName, lastName, age, department, role, hourlyRate);
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

        String firstName, lastName, department, role;
        int age;
        float hourlyRate;

        System.out.println("* UPDATE EMPLOYEE DETAILS *\n");
        int empID;
        System.out.println("Enter the Employee ID of the employee you wish to update: ");
        empID = key.nextInt();
        key.nextLine();
        try {
            Employee existingEmployee = IEmployeeDao.findEmployeeById(empID);
            if (existingEmployee == null) {
                System.out.println("Employee with ID " + empID + " not found.");
                return; // Exit if employee not found
            }

            // Display existing employee details
            System.out.println("Existing Employee Details:");
            System.out.println(existingEmployee);

            boolean fin = false;
            Employee updateEmployee = new Employee(empID);
            System.out.println();
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


//            System.out.println("\nCalling updateEmployee()\n");
            Employee employee = IEmployeeDao.updateEmployee(empID, updateEmployee);
            System.out.println("Employee updated successfully");
            System.out.println("Updated Employee: " + IEmployeeDao.findEmployeeById(employee.getEmpID()));
        }catch (DaoException ex) {
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

        if (!input.equalsIgnoreCase("firstName") && !input.equalsIgnoreCase("lastName") && !input.equalsIgnoreCase("age") && !input.equalsIgnoreCase("department") && !input.equalsIgnoreCase("role") && !input.equalsIgnoreCase("hourlyRate")) {
            System.out.println("\nInvalid option... make sure to check that your option is inputted as displayed.");
        } else {
            try {
                if (input.equalsIgnoreCase("firstName")) {
                    FirstNameComparator comp = new FirstNameComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());

                } else if (input.equalsIgnoreCase("lastName")) {
                    LastNameComparator comp = new LastNameComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());
                } else if (input.equalsIgnoreCase("age")) {
                    AgeComparator comp = new AgeComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());
                } else if (input.equalsIgnoreCase("department")) {
                    DepartmentComparator comp = new DepartmentComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());
                } else if (input.equalsIgnoreCase("role")) {
                    RoleComparator comp = new RoleComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());
                } else {
                    HourlyRateComparator comp = new HourlyRateComparator();
                    employeesList = VIEmployeeDao.findEmployeesUsingFilter(input, comp);
                    for (Employee e : employeesList)
                        System.out.println("Employee: " + e.toString());
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

        //create a json converter object
        JsonConverter jsonConverter = new JsonConverter();

        System.out.println("\n* DISPLAY AN EMPLOYEE IN JSON FORMAT *\n");
        try {
            Scanner kbrd = new Scanner(System.in);
            System.out.println("Please enter ID");
            int employeeID = kbrd.nextInt();
            Employee employee = IEmployeeDao.findEmployeeById(employeeID);
            if (employee == null) {
                System.out.println("Employee not found");
            } else {
                String jsonString = jsonConverter.singleEmployeeToJson(employee);
                System.out.println("Employee JSON String is: \n" + jsonString);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void callFeature9() {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

        try {
            Scanner kbrd = new Scanner(System.in);
            boolean idCheck = false;
            do {
                System.out.println("\n* SELECT AN EMPLOYEE IN CHARGE OF PRODUCTS *\n");
                System.out.println("Please enter an Employee ID that oversees products (2,4,5,6): ");
                int employeeID = kbrd.nextInt();

                if (employeeID == 2 || employeeID == 4 || employeeID == 5 || employeeID == 6) {
                    List<Products> productsList = IEmployeeDao.getAllProductsBasedOnEmployeeID(employeeID);
                    System.out.println("_____________________________________________________________________________________________________________");
                    System.out.printf("%47s'S PRODUCTS%n",IEmployeeDao.findEmployeeById(employeeID).getFirstName().toUpperCase());
                    System.out.println("_____________________________________________________________________________________________________________");
                    System.out.printf("%-16s %-36s %-16s %-16s %-16s%n", "Product ID", "ProductName", "ProductType", "Quantity", "Price");
                    System.out.println("_____________________________________________________________________________________________________________");

                    // printing each employee entity
                    for (Products p : productsList)
                        System.out.printf("%-16d %-36s %-19s %-13d €%.2f%n",
                                p.getProduct_ID(),p.getProductName(),p.getProductType(),p.getQuantity(),p.getPrice());

                    idCheck=true;
                } else {
                    System.out.println("This employee doesn't oversee any products");
                }
            } while (!idCheck);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}


