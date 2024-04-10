package org.example.DAOs;

import com.google.gson.Gson;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;
import java.util.List;

public class JsonConverter{

    /**
     * Main Author: Caitlin Maguire
     */

    public static void main(String[] args) {

    }

    // Feature 7:
    // Method to convert a list of employee entities to a JSON String
    public String employeesListToJson(List<Employee> employeesList) throws DaoException {
        Gson gsonParser = new Gson();

       return gsonParser.toJson(employeesList); // passing in employeesList to convert to a json string
//        return jsonString;
    }

    /**
     * Main Author: Rory O'Gorman
     */
    //Feature 8 - Method to convert a single employee to Json String
    public String singleEmployeeToJson(Employee employee) throws DaoException{

        Gson gsonParser = new Gson();

        String jsonString = gsonParser.toJson(employee);

        System.out.println("Employee JSON String is: \n" + jsonString);

        return jsonString;
    }


}
