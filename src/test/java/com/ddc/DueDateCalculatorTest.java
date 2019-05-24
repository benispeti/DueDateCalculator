package com.ddc;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Due Date Calculation")
public class DueDateCalculatorTest {

    @Nested
    @DisplayName("should fail when turnaround time is")
    class WhenTurnaroundTimeIsNotValid {
        private LocalDateTime date;

        @BeforeEach
        void init() {
            date = LocalDateTime.parse("2019-01-01T09:00:00.000");
        }

        @Test
        @DisplayName("zero")
        void calculateDueDateWhenTurnaroundTimeIsZero() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                DueDateCalculator.calculateDueDate(date, 0);
            });
            assertEquals(DueDateCalculator.TURNAROUND_TIME_IS_NOT_VALID, exception.getMessage());
        }

        @Test
        @DisplayName("negative")
        void calculateDueDateWhenTurnaroundTimeIsNegative() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                DueDateCalculator.calculateDueDate(date, -1);
            });
            assertEquals(DueDateCalculator.TURNAROUND_TIME_IS_NOT_VALID, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("should fail when submit date is")
    class WhenSubmitDateIsNotValid {
        private int turnaroundTime;

        @BeforeEach
        void init() {
            turnaroundTime = 1;
        }

        @Test
        @DisplayName("before working hours")
        void calculateDueDateWhenSubmitDateIsBeforeWorkingHours() {
            LocalDateTime date = LocalDateTime.parse("2019-01-01T08:59:59.999");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                DueDateCalculator.calculateDueDate(date, turnaroundTime);
            });
            assertEquals(DueDateCalculator.SUBMIT_DATE_IS_NOT_VALID, exception.getMessage());
        }

        @Test
        @DisplayName("after working hours")
        void calculateDueDateWhenSubmitDateIsAfterWorkingHours() {
            LocalDateTime date = LocalDateTime.parse("2019-01-01T17:00:00.001");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                DueDateCalculator.calculateDueDate(date, turnaroundTime);
            });
            assertEquals(DueDateCalculator.SUBMIT_DATE_IS_NOT_VALID, exception.getMessage());
        }

        @Test
        @DisplayName("on weekend")
        void calculateDueDateWhenSubmitDateIsWeekend() {
            LocalDateTime date = LocalDateTime.parse("2019-01-05T09:00:00.000");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                DueDateCalculator.calculateDueDate(date, turnaroundTime);
            });
            assertEquals(DueDateCalculator.SUBMIT_DATE_IS_NOT_VALID, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("should calculate due date when due date is")
    class WhenCalculationShouldBePossible {

        private LocalDateTime date;

        @BeforeEach
        void init() {
            date = LocalDateTime.parse("2019-01-01T09:00:00.000");
        }

        @Test
        @DisplayName("on the same day")
        void calculateDueDateWhenItShouldBeOnTheSameDay() {
            LocalDateTime expected = LocalDateTime.parse("2019-01-01T10:00:00.000");
            final int ONE_HOUR = 1;
            LocalDateTime actual = DueDateCalculator.calculateDueDate(date, ONE_HOUR);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        @DisplayName("on the next day")
        void calculateDueDateWhenItShouldBeOnTheNextDay() {
            LocalDateTime expected = LocalDateTime.parse("2019-01-02T09:00:00.000");
            final int ONE_WORK_DAY = 8;
            LocalDateTime actual = DueDateCalculator.calculateDueDate(date, ONE_WORK_DAY);
            Assertions.assertEquals(expected, actual);
        }

        @Test
        @DisplayName("on the next week")
        void calculateDueDateWhenItShouldBeOnTheNextWeek() {
            LocalDateTime expected = LocalDateTime.parse("2019-01-08T09:00:00.000");
            final int ONE_WORK_WEEK = 40;
            LocalDateTime actual = DueDateCalculator.calculateDueDate(date, ONE_WORK_WEEK);
            Assertions.assertEquals(expected, actual);
        }
    }
}
