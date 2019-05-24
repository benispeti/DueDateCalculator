package com.ddc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DueDateCalculator {

    public static final String SUBMIT_DATE_IS_NOT_VALID = "Submit date is not valid!";
    public static final String TURNAROUND_TIME_IS_NOT_VALID = "Turnaround time is not valid!";

    private DueDateCalculator() {
    }

    public static LocalDateTime calculateDueDate(LocalDateTime submitDate, int turnaroundTime) {
        if (!isValidSubmitDate(submitDate)) {
            throw new IllegalArgumentException(SUBMIT_DATE_IS_NOT_VALID);
        }
        if (!isValidTurnaroundTime(turnaroundTime)) {
            throw new IllegalArgumentException(TURNAROUND_TIME_IS_NOT_VALID);
        }
        return addTurnaroundTimeToDate(submitDate, turnaroundTime);
    }

    private static boolean isValidSubmitDate(LocalDateTime submitDate) {
        return isDuringWorkingHours(submitDate) && !isWeekendDate(submitDate);
    }

    private static boolean isValidTurnaroundTime(int turnaroundTime) {
        return turnaroundTime > 0;
    }

    private static LocalDateTime getWorkingHoursStart(LocalDateTime date) {
        return date.withHour(9).withMinute(0).withSecond(0).withNano(0).minusNanos(1);
    }

    private static LocalDateTime getWorkingHoursEnd(LocalDateTime date) {
        return date.withHour(17).withMinute(0).withSecond(0).withNano(0).plusNanos(1);
    }

    private static boolean isDuringWorkingHours(LocalDateTime date) {
        LocalDateTime workingHourStart = getWorkingHoursStart(date);
        LocalDateTime workingHourEnd = getWorkingHoursEnd(date);
        return date.isAfter(workingHourStart) && date.isBefore(workingHourEnd);
    }

    private static boolean isWeekendDate(LocalDateTime date) {
        return date.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private static int getHoursOfTurnaround(LocalDateTime date, int turnaroundTime) {
        int workHoursToAdd = turnaroundTime % 8;
        return getHoursFromWorkHours(date, workHoursToAdd);
    }

    private static int getDaysOfTurnaround(LocalDateTime date, int turnaroundTime) {
        int workDaysToAdd = (int) Math.floor(turnaroundTime / 8);
        return getDaysFromWorkdays(date, workDaysToAdd);
    }

    private static int getHoursFromWorkHours(LocalDateTime date, int workHoursToAdd) {
        return isDuringWorkingHours(date.plusHours(workHoursToAdd)) ? workHoursToAdd : workHoursToAdd+16;
    }

    private static int getDaysFromWorkdays(LocalDateTime date, int workDaysToAdd) {
        int daysToAdd = 0;
        while(workDaysToAdd > 0) {
            LocalDateTime tmp = date.plusDays(++daysToAdd);
            if (!isWeekendDate(tmp)) {
                workDaysToAdd--;
            }
        }
        return daysToAdd;
    }

    private static LocalDateTime addTurnaroundTimeToDate(LocalDateTime date, int turnaroundTime) {
        return date
                .plusDays(getDaysOfTurnaround(date, turnaroundTime))
                .plusHours(getHoursOfTurnaround(date, turnaroundTime));
    }
}
