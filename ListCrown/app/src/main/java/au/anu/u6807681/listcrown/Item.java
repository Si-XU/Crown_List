package au.anu.u6807681.listcrown;

import java.util.Date;

public class Item {
    public String keyword;
    public String description;
    public Date startDate = new Date();
    public Date endDate;
    public String reminderTime;
    public ImportantColor importantLevel;
    public String state;
    public String location;
    boolean finished = false;

    // unfinished
    public Item(String keyword, String description) {
        this.keyword = keyword;
        this.description = description;
    }
}
