package org.example.DAOs;

import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.sql.*;

/**
 * Main author: Caitlin Maguire
 */

public abstract class MySqlDao {

    public Connection getConnection() throws DaoException {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "retail_store";
        String userName = "root";
        String password = "";
        Connection connection = null;

        //testing the connection using a try catch
        try {
            connection = DriverManager.getConnection(url + dbName, userName, password);

        } catch (SQLException ex) {
            System.out.println("Connection failed " + ex.getMessage());
        }
        return connection;
    }

    public void freeConnection(Connection connection) throws DaoException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }

    }

}


