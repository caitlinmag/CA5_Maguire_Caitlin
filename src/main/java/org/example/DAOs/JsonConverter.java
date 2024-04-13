package org.example.DAOs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.DTOs.Employee;
import org.example.Exceptions.DaoException;

import java.util.List;

public class JsonConverter {

    /**
     * Main Author: Caitlin Maguire
     */

    public static void main(String[] args) {


    }

    Gson gsonParser = new Gson();


    // Feature 7:
    // Method to convert a list of employee entities to a JSON String
    public String employeesListToJson(List<Employee> employeesList) throws DaoException {
        Gson gsonParser = new Gson();

        //https://medium.com/@sujathamudadla1213/json-formatter-a6dab2503601#:~:text=If%20you%20are%20working%20with,set%20to%20format%20JSON%20data.
//        Gson gsonParser = new GsonBuilder().setPrettyPrinting().create();

        return gsonParser.toJson(employeesList); // passing in employeesList to convert to a json string

        // TypeToken for gson parser to create an arraylist of employee objec
//         employeesList = gsonParser.fromJson();
    }

    /**
     * Main Author: Rory O'Gorman
     */
    //Feature 8 - Method to convert a single employee to Json String
    public String singleEmployeeToJson(Employee employee) throws DaoException {

        String jsonString = gsonParser.toJson(employee);

        System.out.println("Employee JSON String is: \n" + jsonString);

        return jsonString;
    }

}
