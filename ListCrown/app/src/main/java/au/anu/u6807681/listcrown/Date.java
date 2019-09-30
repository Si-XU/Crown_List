package au.anu.u6807681.listcrown;

public class Date {
    int year;
    int month;
    int day;

    Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /* unfinished */
    public boolean isValid(Date t) {
        int[] daysInEachMonth = new int[]{0, 31, 29, 31, 30,31 ,30,31 ,31 ,30, 31, 30, 31};
        return t.year > 0 && t.month > 0 && t.month<= 12 && t.day > 0 && t.day <= daysInEachMonth[t.month];
    }
}
