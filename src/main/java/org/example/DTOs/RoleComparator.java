package org.example.DTOs;


import java.util.Comparator;

public class RoleComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.getRole().compareTo(e2.getRole());
    }

}