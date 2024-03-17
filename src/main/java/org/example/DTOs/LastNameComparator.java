package org.example.DTOs;


import java.util.Comparator;

public class LastNameComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.getLastName().compareTo(e2.getLastName());
    }

}