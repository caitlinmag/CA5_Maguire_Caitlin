package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MySqlEmployeeDao extends MySqlDao implements EmployeeDaoInterface {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    /**
     * Main author: Caitlin Maguire
     */
    //Feature 1: Return a list of all employees and display
    @Override
    public List<Employee> getAllEmployees() throws DaoException {
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

    /**
     * Main author: Rory O'Gorman
     */
    //Feature 2: find a single employee by ID
    @Override
    public Employee findEmployeeById(int employeeID) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try {
            connection = this.getConnection();

            String query = "SELECT * FROM employees WHERE empID = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int empID = resultSet.getInt("empID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                int age = resultSet.getInt("age");
                String department = resultSet.getString("department");
                String role = resultSet.getString("role");
                Float hourlyRate = resultSet.getFloat("hourlyRate");



                employee = new Employee(empID, firstName, lastName, age, department, role, hourlyRate);
            }
        } catch (SQLException e) {
            throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
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
                throw new DaoException("findEmployeeById() " + e.getMessage());
            }
        }
        return employee;     // reference to User object, or null value
    }
    /**
     * Main author: Jamie Lawlor
     */
    //Feature 3: Delete an entity by key
    @Override
    public int DeleteEmployee(int id) throws DaoException {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try{
            connection=this.getConnection();
            String DeleteQuery="DELETE FROM employees WHERE employees.empID = ?";
            preparedStatement= connection.prepareStatement(DeleteQuery);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch(SQLException ex){
            throw new DaoException("DeleteEmployee() "+ex.getMessage());
        }finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException ex)
            {
                throw new DaoException("DeleteEmployee() " + ex.getMessage());
            }
        }
        return id;
    }

    /**
     * Main author: Jamie Lawlor
     */
    //Feature 4: Insert an entity
    @Override
    public Employee InsertEmployee(Employee e)throws DaoException{
        Connection connection=null;
        PreparedStatement preparedStatement=null;

        try{
            connection=this.getConnection();
            String InsertQuery="INSERT INTO retail_store.employees VALUES (null,?,?,?,?,?,?)";
            preparedStatement= connection.prepareStatement(InsertQuery);
            preparedStatement.setString(1, e.getFirstName());
            preparedStatement.setString(2,e.getLastName());
            preparedStatement.setInt(3,e.getAge());
            preparedStatement.setString(4,e.getDepartment());
            preparedStatement.setString(5,e.getRole());
            preparedStatement.setFloat(6,e.getHourlyRate());
            preparedStatement.executeUpdate();
        } catch(SQLException ex){
            throw new DaoException("InsertEmployee() "+ex.getMessage());
        }finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException ex)
            {
                throw new DaoException("findUserByUsernamePassword() " + ex.getMessage());
            }
        }

        return e;
    }

    //  Feature 5: update an entity by ID

    /**
     * Main author: Jamie Lawlor
     */
    @Override
    public Employee updateEmployee(int id, Employee e) throws DaoException {
        EmployeeDaoInterface IUserDao = new MySqlEmployeeDao();
        try {
            connection = this.getConnection();
            String updateQuery = "UPDATE employees SET firstName=?, lastName=?,age=?,department=?,role=?,hourlyRate=? WHERE empID=?";
            preparedStatement = connection.prepareStatement(updateQuery);
            if (!e.getFirstName().isEmpty()) {
                preparedStatement.setString(1, e.getFirstName());
            } else {
                preparedStatement.setString(1, IUserDao.findEmployeeById(e.getEmpID()).getFirstName());
            }
            if (!e.getLastName().isEmpty()) {
                preparedStatement.setString(2, e.getLastName());
            } else {
                preparedStatement.setString(2, IUserDao.findEmployeeById(e.getEmpID()).getLastName());
            }
            if (e.getAge() != 0) {
                preparedStatement.setInt(3, e.getAge());
            } else {
                preparedStatement.setInt(3, IUserDao.findEmployeeById(e.getEmpID()).getAge());
            }
            if (!e.getDepartment().isEmpty()) {
                preparedStatement.setString(4, e.getDepartment());
            } else {
                preparedStatement.setString(4, IUserDao.findEmployeeById(e.getEmpID()).getDepartment());
            }
            if (!e.getRole().isEmpty()) {
                preparedStatement.setString(5, e.getRole());
            } else {
                preparedStatement.setString(5, IUserDao.findEmployeeById(e.getEmpID()).getRole());
            }
            if (e.getHourlyRate() != 0) {
                preparedStatement.setFloat(6, e.getHourlyRate());
            } else {
                preparedStatement.setFloat(6, IUserDao.findEmployeeById(e.getEmpID()).getHourlyRate());
            }
            preparedStatement.setInt(7, e.getEmpID());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException("UpdateEmployee() " + ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException ex) {
                throw new DaoException("UpdateEmployee() " + ex.getMessage());
            }
        }
        return e;
    }


    /**
     * Main author: Jamie Lawlor
     */
    //Feature 6: Filter entities


    @Override
    public List<Employee> findEmployeesUsingFilter(String filter, Comparator<Employee> names) throws DaoException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Employee> employeesList = new ArrayList<>();

        try {
            //Get the connection object inherited from MySqlDao
            connection = this.getConnection();

            String FilterNameQuery = "SELECT * FROM `employees` ORDER BY ?";
            preparedStatement = connection.prepareStatement(FilterNameQuery);
            preparedStatement.setString(1,filter);
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
            employeesList.sort(names);
        } catch (SQLException e) {
            throw new DaoException("FilterEmployees() " + e.getMessage());
        }
        return employeesList;
    }

}



