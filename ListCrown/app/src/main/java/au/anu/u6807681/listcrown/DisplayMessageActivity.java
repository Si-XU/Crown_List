package au.anu.u6807681.listcrown;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        System.out.println("sdfsdfsd");

        final Date date = new Date();
        final CompactCalendarView compactCalendarView = findViewById(R.id.compactCalendarView);
        // final ActionBar actionbar = getSupportActionBar();

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        // actionbar.setTitle((1900+date.getYear()) + "." + (date.getMonth()+1));

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1570798800000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1571144400000L);
        compactCalendarView.addEvent(ev2);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                String notes = "";
                for (int i = 0; i < events.size() ;i++) {
                    if(i > 0)  notes += "\n";
                    notes += (String) events.get(i).getData();
                }

                if (events.size() > 0) {
                    Toast.makeText(context, notes, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "no event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // actionbar.setTitle((1900+firstDayOfNewMonth.getYear()) + "." + (firstDayOfNewMonth.getMonth()+1));
            }
        });
    }


}
