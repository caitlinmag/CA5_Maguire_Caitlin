package org.example.DAOs;

import com.google.gson.Gson;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.ArrayList;
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

        String jsonString = gsonParser.toJson(employeesList); // passing in employeesList to convert to a json string

        System.out.println("Json String of employees : \n" + jsonString); // output json string of employees list
        return jsonString;
    }


}
