package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface EmployeeDaoInterface {
    public List<Employee> getAllEmployees()throws DaoException;
}
