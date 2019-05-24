package com.ddc;

import java.time.LocalDateTime;

public class DueDateCalculatorApp {
    /**
     *
     * @param args 0th item is the submit date (e.g. 2019-01-01T09:00:00.000). 1st item is Turnaround Time
     */
    public static void main(String[] args) {
        LocalDateTime date = LocalDateTime.parse(args[0]);
        int turnaroundTime = Integer.parseInt(args[1]);
        System.out.println("Due Date is: " + DueDateCalculator.calculateDueDate(date, turnaroundTime));
    }
}
