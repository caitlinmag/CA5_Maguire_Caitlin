package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlEmployeeDao extends MySqlDao implements EmployeeDaoInterface {

    /**
     * Main author: Caitlin Maguire
     */
    //Feature 1: Return a list of all employees and display
    @Override
    public List<Employee> getAllEmployees() throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Employee> employeesList = new ArrayList<>();

        try {
            //Get the connection object inherited from MySqlDao
            connection = this.getConnection();

            String displayAllQuery = "SELECT * FROM employees";
            preparedStatement = connection.prepareStatement(displayAllQuery);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int empID = resultSet.getInt("empID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                int age = resultSet.getInt("age");
                String department = resultSet.getString("department");
                String role = resultSet.getString("role");
                Float hourlyRate = resultSet.getFloat("hourlyRate");

                Employee e = new Employee(empID, firstName, lastName, age, department, role, hourlyRate);
                employeesList.add(e);
            }

        } catch (SQLException e) {
            throw new DaoException("getAllEmployees() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("getAllEmployees() " + e.getMessage());
            }
        }
        return employeesList;
    }
}


