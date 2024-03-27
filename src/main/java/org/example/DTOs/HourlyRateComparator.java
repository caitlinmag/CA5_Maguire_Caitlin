package org.example.DTOs;

import java.util.Comparator;

public class HourlyRateComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        if (e1.getHourlyRate()<e2.getHourlyRate()){
            return -1;
        }else if (e1.getHourlyRate()>e2.getHourlyRate()){
            return 1;
        }
        return 0;
    }
}