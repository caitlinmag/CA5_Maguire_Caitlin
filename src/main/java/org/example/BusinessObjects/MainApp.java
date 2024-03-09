package org.example.BusinessObjects;

import org.example.DAOs.EmployeeDaoInterface;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

        //Feature 1: Display and return all employees
        try {
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
    }

}