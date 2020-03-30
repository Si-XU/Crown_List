package au.anu.u6807681.listcrown;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/**
 * @author Hao Cao, Si Xu, YunHuang
 *  this is the start activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseManager databaseManager;
    private ListView listView;
    private FloatingActionButton calendar;
    private FloatingActionButton addTask;
    private FloatingActionButton track;
    private boolean isActive;
    private NotificationManager notificationManager;

    //map columns from a cursor to Text
    private SimpleCursorAdapter sca;
    private ListAdapter listAdapter;
    private String[] data = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION, MyDatabaseHelper.ENDDATE, MyDatabaseHelper.STATE};
    private int[] ids = new int[] { R.id.id, R.id.keyword, R.id.description, R.id.enddate, R.id.state};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the default show text in the main page
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_notifier));
        isActive = true;

        calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(this);

        addTask = findViewById(R.id.add_a_task);
        addTask.setOnClickListener(this);

        track = findViewById(R.id.track);
        track.setOnClickListener(this);


        //fetch data from the database
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        ArrayList<HashMap<String, String>> itemList = getItemMainPage();
        //listview shows the data
        listAdapter = new MainAdapter(itemList, this);
        listView.setAdapter(listAdapter);

        // OnCLickListener for tasks which are listed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                // list the main info of tasks
                TextView idTextView = view.findViewById(R.id.id);
                String id = idTextView.getText().toString();

                //when it is clicked, new intent and pass the chosen task id to the next activity
                Intent editIntent = new Intent(getApplicationContext(), ModifyItemActivity.class);
                editIntent.putExtra("id", id);

                startActivity(editIntent);
            }
        });

        //initialise a notificationManager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //make sure the target API 26+ because the NotificationChannel class is new
        //call the createNotificationChannel method and create three channels with
        // different importance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "H";
            String channelName = "ImportantReminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "M";
            channelName = "Reminder";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar:
                Intent checkCalendar = new Intent(this, DisplayMessageActivity.class);
                startActivity(checkCalendar);
                break;
            case R.id.add_a_task:
                Intent add_item = new Intent(this, AddItemActivity.class);
                startActivity(add_item);
                break;
            case R.id.track:
                if (isActive) {
                    isActive = false;
                    ArrayList<HashMap<String, String>> itemListTrack = getUndoneTrack();
                    listAdapter = new MainAdapter(itemListTrack, this);
                } else {
                    isActive = true;
                    ArrayList<HashMap<String, String>> itemList = getItemMainPage();
                    listAdapter = new MainAdapter(itemList, this);
                }
                listView.setAdapter(listAdapter);
                break;

        }


    }

    private ArrayList<HashMap<String, String>> getItemMainPage() {
        Cursor cursor = databaseManager.selectToMainPage();
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
            item.put("keyword", cursor.getString(cursor.getColumnIndex("keyword")));
            item.put("description", cursor.getString(cursor.getColumnIndex("description")));

            //deal with the deadline date
            long deadline = Long.parseLong(cursor.getString(cursor.getColumnIndex("enddate")));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  HH:mm");
            Date date = new Date(deadline);
            String str = "Deadline: " + sdf.format(date);
            item.put("enddate", str);

            item.put("state", cursor.getString(cursor.getColumnIndex("state")));
            itemList.add(item);
        }
        return itemList;
    }

    private ArrayList<HashMap<String, String>> getUndoneTrack() {
        Cursor cursor = databaseManager.selectToMainPageTrack();
        ArrayList<HashMap<String, String>> itemListTrack = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
            item.put("keyword", cursor.getString(cursor.getColumnIndex("keyword")));
            item.put("description", cursor.getString(cursor.getColumnIndex("description")));

            //deal with the deadline date
            long deadline = Long.parseLong(cursor.getString(cursor.getColumnIndex("enddate")));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  HH:mm");
            Date date = new Date(deadline);
            String str = "Deadline: " + sdf.format(date);
            item.put("enddate", str);

            item.put("state", cursor.getString(cursor.getColumnIndex("state")));
            itemListTrack.add(item);
        }
        return itemListTrack;
    }

    //the method to create NotificationChannel
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
