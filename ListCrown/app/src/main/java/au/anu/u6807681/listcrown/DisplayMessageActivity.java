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
/**
 * @author Si Xu
 *
 */
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
            String importance = cursor.getString(cursor.getColumnIndex("importance"));
            Event event;
            if (importance.equals("low")) {
                event = new Event(Color.GREEN, endDate, keyword + "|" + description);
            } else if (importance.equals("medium")) {
                event = new Event(getResources().getColor(R.color.champagneYellow), endDate, keyword + "|" + description);
            } else {
                event = new Event(Color.RED, endDate, keyword + "|" + description);
            }
            compactCalendarView.addEvent(event);
        }
        while(cursor.moveToNext());

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                ListView listView = findViewById(R.id.eventList);
                listView.removeAllViewsInLayout();

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
