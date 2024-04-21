package org.example.DAOs;

import org.example.DTOs.*;
import org.example.Exceptions.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySqlEmployeeDaoTest {
    MySqlEmployeeDao ed = null;
    JsonConverter jc=null;
    Connection connection = null;
    Socket socket = null;
    //Creating the dao class
    @BeforeEach
    void setUp() {
        ed = new MySqlEmployeeDao();
        jc= new JsonConverter();
    }
    //Ensures that the database increments properly after running tests
    @AfterEach
    void reset() {
        PreparedStatement preparedStatement;
        try {
            connection = ed.getConnection();
            String resetIncrement = "ALTER TABLE employees AUTO_INCREMENT = 1";
            preparedStatement = connection.prepareStatement(resetIncrement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Reset Increment failed: " + ex.getMessage());
        }
    }
    /**
     * Tutorial used for JUnit testing for java and sql:
     * https://softwaredesign.home.blog/tutorials/junit-test-with-dao-data-access-object-class/
     */

    /**
     * Main author: Caitlin Maguire
     */
    @Test
    void getAllEmployeesTest() throws DaoException {
        System.out.println("Test 1 - check whether all 10 employees display");
        List<Employee> employeesList = ed.getAllEmployees();
        assertNotNull(employeesList);
        assertEquals(10, employeesList.size()); //currently there are 11 employees - test will fail when more employees are added
    }

    /**
     * Main author: Caitlin Maguire
     */
    @Test
    void findEmployeeByInvalidIdTest() throws DaoException {
        System.out.println("Test 2 - check whether an employee is found by testing an invalid id");
        //exception scenario
        Employee employee = ed.findEmployeeById(-1);
        assertNull(employee);
    }

    /**
     * Main author: Caitlin Maguire
     */
    @Test
    void findEmployeeByValidIdTest() throws DaoException {
        System.out.println("Test 3 - check whether an employee is found by testing a valid id");

        //normal scenario
        Employee employee = ed.findEmployeeById(3);
        assertNotNull(employee);
        assertEquals("Ruby", employee.getFirstName());
        assertEquals("Mullen", employee.getLastName());
        assertEquals(25, employee.getAge());
        assertEquals("Customer Service", employee.getDepartment());
        assertEquals("Customer Service Assistant", employee.getRole());
        assertEquals(11, employee.getHourlyRate());
    }

    /**
     * Main author: Caitlin Maguire
     * Other contributor: Jamie Lawlor
     */
    @Test
    void deleteEmployeeTest() throws DaoException {
        System.out.println("Test 4 - check if an employee can be deleted from the database");
        connection = ed.getConnection();
        try {
            connection.setAutoCommit(false);
            Employee testEmployee = new Employee(11, "test", "testing", 32, "Sales", "Assistant Supervisor", 12.31f);
            ed.InsertEmployee(testEmployee);
            ed.findEmployeeById(testEmployee.getEmpID());
            ed.DeleteEmployee(testEmployee.getEmpID());
            assertEquals(10, ed.getAllEmployees().size());
            //test the row I want to delete
            Employee newEmployee = ed.findEmployeeById(testEmployee.getEmpID());
            assertNull(newEmployee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main author: Caitlin Maguire
     * Other contributor: Jamie Lawlor
     */
    @Test
    void insertEmployeeTest() throws DaoException {
        System.out.println("Test 5 - check if a new employee is added to the database");
        connection = ed.getConnection();
        try {
            connection.setAutoCommit(false);
            Employee testEmployee = new Employee(11, "Sally", "McQueen", 22, "Sales", "Assistant Supervisor", 13.41f);
            ed.InsertEmployee(testEmployee);
            assertNotNull(testEmployee);                       //checking that the testEmployee object is not null
            assertTrue(10 < testEmployee.getEmpID()); //testing if the empId is greater than 10 - already empId 1 - 10 in the database
            assertEquals(11, ed.getAllEmployees().size()); //test how many rows there are in the employees table

            //testing the new employee
            Employee newEmployee = ed.findEmployeeById(testEmployee.getEmpID());
            assertEquals(testEmployee.getFirstName(), newEmployee.getFirstName());
            assertEquals(testEmployee.getLastName(), newEmployee.getLastName());
            assertEquals(testEmployee.getAge(), newEmployee.getAge());
            assertEquals(testEmployee.getDepartment(), newEmployee.getDepartment());
            assertEquals(testEmployee.getRole(), newEmployee.getRole());
            assertEquals(testEmployee.getHourlyRate(), newEmployee.getHourlyRate());
            ed.DeleteEmployee(testEmployee.getEmpID());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.rollback(); //rollback any changes made to the database from this test
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void updateEmployeeTest() throws DaoException {
        System.out.println("Test 6- check if an employee is updated on the database");
        connection = ed.getConnection();
        try {
            connection.setAutoCommit(false);
            Employee testEmployee = new Employee(11, "test", "testing", 32, "Sales", "Assistant Supervisor", 12.31f);
            ed.InsertEmployee(testEmployee);
            String newFirstName = "Gerry";
            String newLastName = "Carlson";
            int newAge = 45;
            String newDepartment = "Sales";
            String newRole = "Chief Executive Officer";
            float newHourlyRate = 50;
            Employee updatedEmployee = new Employee(testEmployee.getEmpID(), newFirstName, newLastName, newAge, newDepartment, newRole, newHourlyRate);
            ed.updateEmployee(testEmployee.getEmpID(), updatedEmployee);
            Employee checkEmployee = ed.findEmployeeById(testEmployee.getEmpID());
            assertEquals("Gerry", checkEmployee.getFirstName());
            assertEquals("Carlson", checkEmployee.getLastName());
            assertEquals(45, checkEmployee.getAge());
            assertEquals("Sales", checkEmployee.getDepartment());
            assertEquals("Chief Executive Officer", checkEmployee.getRole());
            assertEquals(50, checkEmployee.getHourlyRate());
            ed.DeleteEmployee(checkEmployee.getEmpID());
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingFirstNameFilterTest(){
        System.out.println("Test 7- check if an employee is filtered by first name");
        try {
            String filter = "firstName";
            FirstNameComparator comp = new FirstNameComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals("Annie", filteredEmployeesList.get(0).getFirstName());
            assertEquals("Bonnie", filteredEmployeesList.get(1).getFirstName());
            assertEquals("Carl", filteredEmployeesList.get(2).getFirstName());
            assertEquals("Dylan", filteredEmployeesList.get(3).getFirstName());
            assertEquals("Jess", filteredEmployeesList.get(4).getFirstName());
            assertEquals("Kai", filteredEmployeesList.get(5).getFirstName());
            assertEquals("Molly", filteredEmployeesList.get(6).getFirstName());
            assertEquals("Ruby", filteredEmployeesList.get(7).getFirstName());
            assertEquals("Sophia", filteredEmployeesList.get(8).getFirstName());
            assertEquals("Tom", filteredEmployeesList.get(9).getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingLastNameFilterTest() {
        System.out.println("Test 8- check if an employee is filtered by last name");
        try {
            String filter = "lastName";
            LastNameComparator comp = new LastNameComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals("Connolly", filteredEmployeesList.get(0).getLastName());
            assertEquals("Cooper", filteredEmployeesList.get(1).getLastName());
            assertEquals("Hughes", filteredEmployeesList.get(2).getLastName());
            assertEquals("Mckevitt", filteredEmployeesList.get(3).getLastName());
            assertEquals("Mullen", filteredEmployeesList.get(4).getLastName());
            assertEquals("Murphy", filteredEmployeesList.get(5).getLastName());
            assertEquals("O Brien", filteredEmployeesList.get(6).getLastName());
            assertEquals("Reilly", filteredEmployeesList.get(7).getLastName());
            assertEquals("Ryan", filteredEmployeesList.get(8).getLastName());
            assertEquals("Smith", filteredEmployeesList.get(9).getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingAgeFilterTest() {
        System.out.println("Test 9- check if an employee is filtered by age");
        try {
            String filter = "age";
            AgeComparator comp = new AgeComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals(19, filteredEmployeesList.get(0).getAge());
            assertEquals(20, filteredEmployeesList.get(1).getAge());
            assertEquals(23, filteredEmployeesList.get(2).getAge());
            assertEquals(25, filteredEmployeesList.get(3).getAge());
            assertEquals(30, filteredEmployeesList.get(4).getAge());
            assertEquals(33, filteredEmployeesList.get(5).getAge());
            assertEquals(38, filteredEmployeesList.get(6).getAge());
            assertEquals(45, filteredEmployeesList.get(7).getAge());
            assertEquals(47, filteredEmployeesList.get(8).getAge());
            assertEquals(50, filteredEmployeesList.get(9).getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingDepartmentFilterTest() {
        System.out.println("Test 10- check if an employee is filtered by department");
        try {
            String filter = "department";
            DepartmentComparator comp = new DepartmentComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals("Customer Service", filteredEmployeesList.get(0).getDepartment());
            assertEquals("Customer Service", filteredEmployeesList.get(1).getDepartment());
            assertEquals("Customer Service", filteredEmployeesList.get(2).getDepartment());
            assertEquals("Human Resources", filteredEmployeesList.get(3).getDepartment());
            assertEquals("Human Resources", filteredEmployeesList.get(4).getDepartment());
            assertEquals("Inventory", filteredEmployeesList.get(5).getDepartment());
            assertEquals("Inventory", filteredEmployeesList.get(6).getDepartment());
            assertEquals("Sales", filteredEmployeesList.get(7).getDepartment());
            assertEquals("Sales", filteredEmployeesList.get(8).getDepartment());
            assertEquals("Sales", filteredEmployeesList.get(9).getDepartment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingRoleFilterTest() {
        System.out.println("Test 11- check if an employee is filtered by role");
        try {
            String filter = "role";
            RoleComparator comp = new RoleComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals("Assistant Manager", filteredEmployeesList.get(0).getRole());
            assertEquals("Assistant Manager", filteredEmployeesList.get(1).getRole());
            assertEquals("Customer Service Assistant", filteredEmployeesList.get(2).getRole());
            assertEquals("Human Resources Manager", filteredEmployeesList.get(3).getRole());
            assertEquals("Inventory Manager", filteredEmployeesList.get(4).getRole());
            assertEquals("Manager", filteredEmployeesList.get(5).getRole());
            assertEquals("Sales Assistant", filteredEmployeesList.get(6).getRole());
            assertEquals("Sales Assistant", filteredEmployeesList.get(7).getRole());
            assertEquals("Stock Assistant", filteredEmployeesList.get(8).getRole());
            assertEquals("Supervisor", filteredEmployeesList.get(9).getRole());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void findEmployeesUsingHourlyRateFilterTest() {
        System.out.println("Test 12- check if an employee is filtered by hourly rate");
        try {
            String filter = "role";
            HourlyRateComparator comp = new HourlyRateComparator();
            assertNotNull(comp);
            List<Employee> filteredEmployeesList = ed.findEmployeesUsingFilter(filter, comp);
            assertNotNull(filteredEmployeesList);
            assertEquals((float)10.5, filteredEmployeesList.get(0).getHourlyRate());
            assertEquals((float)10.7, filteredEmployeesList.get(1).getHourlyRate());
            assertEquals((float)10.7, filteredEmployeesList.get(2).getHourlyRate());
            assertEquals((float)11.0, filteredEmployeesList.get(3).getHourlyRate());
            assertEquals((float)12.6, filteredEmployeesList.get(4).getHourlyRate());
            assertEquals((float)13.0, filteredEmployeesList.get(5).getHourlyRate());
            assertEquals((float)13.9, filteredEmployeesList.get(6).getHourlyRate());
            assertEquals((float)14.3, filteredEmployeesList.get(7).getHourlyRate());
            assertEquals((float)15.5, filteredEmployeesList.get(8).getHourlyRate());
            assertEquals((float)16.16, filteredEmployeesList.get(9).getHourlyRate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void employeesListToJsonTest() {
        System.out.println("Test 13- check if all employees are converted to JSON");
        try{
            List<Employee> employeesList = ed.getAllEmployees();
            assertNotNull(employeesList);
            String convertedList=jc.employeesListToJson(employeesList);
            assertEquals("[{\"empID\":1,\"firstName\":\"Tom\",\"lastName\":\"Reilly\",\"age\":45,\"department\":\"Human Resources\",\"role\":\"Assistant Manager\",\"hourlyRate\":12.6},{\"empID\":2,\"firstName\":\"Bonnie\",\"lastName\":\"Ryan\",\"age\":33,\"department\":\"Inventory\",\"role\":\"Inventory Manager\",\"hourlyRate\":13.0},{\"empID\":3,\"firstName\":\"Ruby\",\"lastName\":\"Mullen\",\"age\":25,\"department\":\"Customer Service\",\"role\":\"Customer Service Assistant\",\"hourlyRate\":11.0},{\"empID\":4,\"firstName\":\"Dylan\",\"lastName\":\"Mckevitt\",\"age\":19,\"department\":\"Sales\",\"role\":\"Sales Assistant\",\"hourlyRate\":10.5},{\"empID\":5,\"firstName\":\"Annie\",\"lastName\":\"Murphy\",\"age\":20,\"department\":\"Sales\",\"role\":\"Sales Assistant\",\"hourlyRate\":10.7},{\"empID\":6,\"firstName\":\"Carl\",\"lastName\":\"Connolly\",\"age\":30,\"department\":\"Customer Service\",\"role\":\"Manager\",\"hourlyRate\":15.5},{\"empID\":7,\"firstName\":\"Molly\",\"lastName\":\"Smith\",\"age\":47,\"department\":\"Human Resources\",\"role\":\"Human Resources Manager\",\"hourlyRate\":16.16},{\"empID\":8,\"firstName\":\"Kai\",\"lastName\":\"Hughes\",\"age\":23,\"department\":\"Inventory\",\"role\":\"Stock Assistant\",\"hourlyRate\":10.7},{\"empID\":9,\"firstName\":\"Sophia\",\"lastName\":\"Cooper\",\"age\":38,\"department\":\"Sales\",\"role\":\"Supervisor\",\"hourlyRate\":14.3},{\"empID\":10,\"firstName\":\"Jess\",\"lastName\":\"O Brien\",\"age\":50,\"department\":\"Customer Service\",\"role\":\"Assistant Manager\",\"hourlyRate\":13.9}]",convertedList);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void singleEmployeeToJsonTest() {
        System.out.println("Test 14- check if one employee is converted to JSON");
        try{
            Employee employeeRubyMullen = ed.findEmployeeById(3);
            assertNotNull(employeeRubyMullen);
            String convertedList=jc.singleEmployeeToJson(employeeRubyMullen);
            assertNotNull(convertedList);
            assertEquals("{\"empID\":3,\"firstName\":\"Ruby\",\"lastName\":\"Mullen\",\"age\":25,\"department\":\"Customer Service\",\"role\":\"Customer Service Assistant\",\"hourlyRate\":11.0}",convertedList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void socketConnectionTest(){
        System.out.println("Test 15- check if Socket Connection works");
        try{
            socket= new Socket("localhost", 8888);
        } catch (IOException e) {
            assertNotNull(socket);
        }
    }
    /**
     * Main author: Jamie Lawlor
     */
    @Test
    void displayProductsThatEmployeeOverseesTest(){
        System.out.println("Test 15- check if products are displaying by the id of the employee that oversees them");
        try{
            List<Products> productsList = ed.getAllProductsBasedOnEmployeeID(2);
            assertNotNull(productsList);
            assertEquals(3, productsList.size());
        }catch (DaoException e){
            e.printStackTrace();
        }
    }
}