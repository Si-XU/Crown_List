package au.anu.u6807681.listcrown;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        final Date date = new Date();
        final CompactCalendarView compactCalendarView = findViewById(R.id.compactCalendarView);
        final ActionBar actionbar = getSupportActionBar();

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        actionbar.setTitle((1900+date.getYear()) + "." + (date.getMonth()+1));

        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        Cursor cursor = databaseManager.selectToCalendar();

        do {
            String keyword = cursor.getString(cursor.getColumnIndex("keyword"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            long endDate = Long.parseLong(cursor.getString(cursor.getColumnIndex("enddate")));
            Event event = new Event(Color.RED, endDate, keyword + "|" + description);
            compactCalendarView.addEvent(event);
        }
        while(cursor.moveToNext());

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.RED, 1570798800000L, "Event 1.");
        compactCalendarView.addEvent(ev1);
        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.RED, 1571144400000L, "Event 2");
        compactCalendarView.addEvent(ev2);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                ListView listView = findViewById(R.id.eventList);
                listView.removeAllViews();

                if (events.size() > 0) {
                    adapter = new ItemAdapter(events, DisplayMessageActivity.this);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "no event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionbar.setTitle((1900+firstDayOfNewMonth.getYear()) + "." + (firstDayOfNewMonth.getMonth()+1));
            }
        });
    }


}
