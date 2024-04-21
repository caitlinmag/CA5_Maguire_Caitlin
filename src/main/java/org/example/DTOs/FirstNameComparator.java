package org.example.DTOs;


import java.util.Comparator;

public class FirstNameComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.getFirstName().compareToIgnoreCase(e2.getFirstName());
    }

}