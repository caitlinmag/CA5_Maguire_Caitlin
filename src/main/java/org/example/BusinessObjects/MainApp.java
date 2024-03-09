package org.example.BusinessObjects;

import org.example.DAOs.EmployeeDaoInterface;
import org.example.DAOs.MySqlEmployeeDao;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        EmployeeDaoInterface IEmployeeDao = new MySqlEmployeeDao();

    }

}