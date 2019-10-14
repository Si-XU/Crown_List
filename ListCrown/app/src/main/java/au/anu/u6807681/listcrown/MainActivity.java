package au.anu.u6807681.listcrown;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    /**
     * include all items
     */
    private ArrayList<Item> itemList = new ArrayList<>();
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initListItems() throws ParseException {
        String[] dateString = {"2019-10-02","2018-10-23","2018-10-15"};

        for (int i = 0; i < 2; i++) {
            Item addItem = new Item("test " + itemList.size(), "this is test " + itemList.size());
            addItem.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString[i]);
            itemList.add(addItem);
        }
    }

    public void clickAdd(View V) throws ParseException {
        Item addItem = new Item("test " + itemList.size(), "this is test " + itemList.size());
        addItem.endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-15");
        itemList.add(addItem);
        adapter = new ItemAdapter(itemList, MainActivity.this);
        ListView listView = (ListView) findViewById(R.id.itemList);
        listView.setAdapter(adapter);
    }

    public void clickCalendar(View V) {
        CalendarView calendar = findViewById(R.id.calendar);
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

}
