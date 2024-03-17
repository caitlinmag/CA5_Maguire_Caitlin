package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySqlEmployeeDaoTest {

    /**
     * Main author: Caitlin Maguire
     *
     */

    /**
     * Tutorial used for JUnit testing for java and sql:
     * https://softwaredesign.home.blog/tutorials/junit-test-with-dao-data-access-object-class/
     */

    //Declare outside the tests to reuse
    MySqlEmployeeDao ed = new MySqlEmployeeDao();
    Connection connection = null;

    @org.junit.jupiter.api.Test
    void getAllEmployeesTest() throws DaoException {
        System.out.println("Test 1 - check whether all 10 employees display");
        List<Employee> employeesList = ed.getAllEmployees();
        assertNotNull(employeesList);
        assertEquals(11, employeesList.size()); //currently there are 11 employees - test will fail when more employees are added
    }

    @org.junit.jupiter.api.Test
    void findEmployeeByInvalidIdTest() throws DaoException {
        System.out.println("Test 2 - check whether an employee is found by testing an invalid id");

        MySqlEmployeeDao ed = new MySqlEmployeeDao();
        //exception scenario
        Employee employee = ed.findEmployeeById(-1);
        assertNull(employee);
    }

    @org.junit.jupiter.api.Test
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

    @org.junit.jupiter.api.Test
    void DeleteEmployeeTest() throws DaoException {
        System.out.println("Test 4 - check if an employee can be deleted from the database");
        Employee testEmployee = new Employee(12, "test", "testing", 32, "Sales", "Assistant Supervisor", 12.31f);
        connection = ed.getConnection();

        try {
            connection.setAutoCommit(false);
            ed.InsertEmployee(testEmployee);
            ed.DeleteEmployee(testEmployee.getEmpID());
            assertEquals(11, ed.getAllEmployees().size());

            //test the row I want to delete
            Employee newEmployee = ed.findEmployeeById(testEmployee.getEmpID());
            assertNull(newEmployee);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @org.junit.jupiter.api.Test
    void insertEmployeeTest() throws DaoException {
        System.out.println("Test 5 - check if a new employee is added to the database");
        Employee testEmployee = new Employee(11, "Sally", "McQueen", 22, "Sales", "Assistant Supervisor", 13.41f);
        connection = ed.getConnection();

        try {
            connection.setAutoCommit(false);
            ed.InsertEmployee(testEmployee);
            assertNotNull(testEmployee);                       //checking that the testEmployee object is not null
            assertTrue(10 < testEmployee.getEmpID()); //tesing if the empId is greater than 10 - already empId 1 - 10 in the database
            assertEquals(12, ed.getAllEmployees().size()); //test how many rows there are in the employees table

            //testing the new employee
            Employee newEmployee = ed.findEmployeeById(testEmployee.getEmpID());
            assertEquals(testEmployee.getFirstName(), newEmployee.getFirstName());
            assertEquals(testEmployee.getLastName(), newEmployee.getLastName());
            assertEquals(testEmployee.getAge(), newEmployee.getAge());
            assertEquals(testEmployee.getDepartment(), newEmployee.getDepartment());
            assertEquals(testEmployee.getRole(), newEmployee.getRole());
            assertEquals(testEmployee.getHourlyRate(), newEmployee.getHourlyRate());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.rollback(); //rollback any changes made to the database from this test
                connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}