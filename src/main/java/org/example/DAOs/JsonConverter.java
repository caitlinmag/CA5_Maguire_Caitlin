package org.example.DAOs;

import com.google.gson.Gson;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JsonConverter implements EmployeeDaoInterface{
    public static void main(String[] args) {
        JsonConverter converter = new JsonConverter();
        converter.employeesListToJson();
    }

    /**
     * Main Author: Caitlin Maguire
     */

    // Method to convert a list of employee entities to a JSON String
    public String employeesListToJson(List<Employee> employeesList){
        Gson gsonParser = new Gson();

        String jsonString = gsonParser.toJson(employeesList); // converting employees list to json

        System.out.println("Json String of employees : \n" + jsonString);
        return jsonString;
    }


}
