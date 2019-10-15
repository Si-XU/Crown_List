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
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

//this is the start activity
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseManager databaseManager;
    private ListView listView;
    private FloatingActionButton calendar;
    private FloatingActionButton addTask;
    private FloatingActionButton track;
    private NotificationManager notificationManager;

    //map columns from a cursor to Text
    private SimpleCursorAdapter sca;
    private String[] data = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION};
    private int[] ids = new int[] { R.id.id, R.id.keyword, R.id.description};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the default show text in the main page
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_notifier));

        calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(this);

        addTask = findViewById(R.id.add_a_task);
        addTask.setOnClickListener(this);

        track = findViewById(R.id.track);
        track.setOnClickListener(this);



        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        Cursor cursor = databaseManager.selectToMainPage();

        sca = new SimpleCursorAdapter(this, R.layout.activity_view_item, cursor, data, ids, 0);
        sca.notifyDataSetChanged();
        //listview shows the data
        listView.setAdapter(sca);


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
        //call the createNotificationChannel method
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        String channelId = "R";
        String channelName = "Reminder";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId,channelName,importance);
        }


    }
//    // this is the add button modification
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    //jump to the add activity
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.add_record) {
//
//            Intent add_mem = new Intent(this, AddItemActivity.class);
//            startActivity(add_mem);
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        }

    }

    //implement createNotificationChannel method
    private void createNotificationChannel(String channelId,String channelName,int importance){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //set a Reminder
    public void reminder(View view){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,"R");
        notification.setContentTitle("Deadline!!!");
        notification.setContentText("Keyword for the item");
        notification.setWhen(System.currentTimeMillis());
        notification.setSmallIcon(R.drawable.ic_launcher_background);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background));
        notification.setAutoCancel(true);
        notificationManager.notify(1,notification.build());
    }
}
