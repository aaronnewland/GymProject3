package gymmanager.gymproject3;

import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Used to create new Date objects that can be compared. Used for date of birth and membership expiration date.
 * Contains methods used to test validity of given dates, and to determine if given year is a leap year and if an
 * individual is over the age of 18 (able to hold a gym membership).
 * @author Aaron Newland, Dylan Pina
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    /**
     * Creates Date object with today's date.
     */
    public Date() {
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DATE);
        year = c.get(Calendar.YEAR);
    }

    /**
     * Creates Date object with given String date.
     * @param date date given as string is parsed into Date object.
     */
    public Date(String date) {
        StringTokenizer st = new StringTokenizer(date);
        month = Integer.parseInt(st.nextToken("/"));
        day = Integer.parseInt(st.nextToken("/"));
        year = Integer.parseInt(st.nextToken("/"));
    }

    /**
     * Creates a new Date object from an existing one.
     * @param date date to be moved to new object.
     */
    public Date(Date date) {
        month = date.getMonth();
        day = date.getDay();
        year = date.getYear();
    }

    /**
     * Retrieve Year field.
     * @return year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Retrieve Month field.
     * @return month.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Retrieve Day field.
     * @return day.
     */
    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Determines if given year is a leap year.
     * @return true if it is a leap year, false otherwise.
     */
    private boolean isLeapYear() {
        if (year % Constants.QUADRENNIAL == 0 && year % Constants.CENTENNIAL == 0 &&
                year % Constants.QUATERCENTENNIAL == 0) {
            return true;
        } else if (year % Constants.QUADRENNIAL == 0 && year % Constants.CENTENNIAL != 0) return true;

        return false;
    }

    /**
     * Determines if two dates are equal to one another. Year, month, and day are the same for both.
     * @param date date to compare to given date
     * @return true if dates are the same, false otherwise.
     */
    public boolean equals(Date date) {
        return compareTo(date) == 0;
    }

    /**
     * Compares dates to one another. D1 and D2.
     * @param date date (D2) to compare to given date.
     * @return 0 if D1 == D2, 1 if D1 > D2, and -1 if D1 < D2.
     */
    @Override
    public int compareTo(Date date) {
        if (date.year == year && date.month == month && date.day == day) return 0;

        if (date.year == year && date.month == month)
            if (date.day < day) return 1;
            else return -1;

        if (date.year == year && date.month < month) return 1;
        else if (date.year == year && date.month > month) return -1;

        if (date.year < year) return 1;
        else return -1;
    }

    /**
     * Check if a date is a valid calendar date.
     * @return true if calendar date is valid, false otherwise.
     */
    public boolean isValid() {
        if (day < 1) return false;
        // Months that have 31 days
        if ((month == Constants.JAN) || (month == Constants.MAR) || (month == Constants.MAY)
                || (month == Constants.JUL) || (month == Constants.AUG)
                || (month == Constants.OCT) || (month == Constants.DEC)) {
            if (day <= Constants.MAX_DAYS_1)
                return true;
            // Months that have 30 days
        } else if ((month == Constants.APR) || (month == Constants.JUN) ||
                (month == Constants.SEP) || (month == Constants.NOV)) {
            if (day <= Constants.MAX_DAYS_2)
                return true;
            // February case
        } else if (month == Constants.FEB) {
            if (isLeapYear()) {
                if (day <= Constants.MAX_DAYS_LEAP)
                    return true;
            } else if (day <= Constants.MAX_DAYS_NO_LEAP)
                return true;
        }
        return false;
    }

    /**
     * Determines if date of birth is over the age of 18.
     * @return true if date of birth is over the age of 18, false otherwise.
     */
    public boolean isOfAge() {
        Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH) + 1;
        int currDay = c.get(Calendar.DATE);

        if (currYear - year > Constants.LEGAL_AGE)
            return true;
        if (currYear - year == Constants.LEGAL_AGE)
            if (currMonth > month)
                return true;
            else if (currMonth == month)
                if (currDay >= day)
                    return true;
        return false;
    }

    /**
     * Determines if date of birth is before the date of today, and valid.
     * @return true if date given is before today's date, false otherwise.
     */
    public boolean isExpired() {
        Date today = new Date();
        if (this.compareTo(today) <= 0)
            return true;
        return false;
    }

    /**
     * Modifies date to be three months ahead of current date.
     * @return date object with date three months from today's date.
     */
    public Date addThreeMonths() {
        Date newDate = new Date(this);
        int newMonth = newDate.getMonth() + 3;
        int newYear = newDate.getYear();
        if (newMonth > 12) {
            newMonth %= 12;
            newYear++;
        }
        newDate.setMonth(newMonth);
        newDate.setYear(newYear);
        return newDate;
    }

    /**
     * Modifies date to be one year ahead of current date.
     * @return date object with date three months from today's date.
     */
    public Date addOneYear() {
        Date newDate = new Date(this);
        newDate.setYear(newDate.getYear() + 1);
        return newDate;
    }

    /**
     * Provides a string representation of a Date object.
     * @return String of date object.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}
