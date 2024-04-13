package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.Comparator;
import java.util.List;

public interface EmployeeDaoInterface {
    public List<Employee> getAllEmployees()throws DaoException;

    //Feature 2: find a single employee by ID
    Employee findEmployeeById(int empID) throws DaoException;

    public Employee InsertEmployee(Employee e)throws DaoException;

    public int DeleteEmployee(int id)throws DaoException;

    public Employee updateEmployee(int id, Employee e) throws DaoException;
    public List<Employee> findEmployeesUsingFilter(String filter, Comparator<Employee> comp) throws DaoException;

}
