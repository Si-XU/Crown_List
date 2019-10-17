package au.anu.u6807681.listcrown;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hao Cao
 *
 */
public class AddItemActivity extends Activity implements OnClickListener,View.OnTouchListener {

    private Button addTaskButton;
    private EditText keywordEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private EditText createTimeText;
    private EditText endTimeText;
    private Spinner importanceSpinner;
    private Spinner statusSpinner;
    private String[] importanceItems;
    private String[] statusItems;
    private ArrayAdapter<String> importanceAdapter;
    private ArrayAdapter<String> statusAdapter;
    private long id;
    private String keyword;
    private String description;
    private String location;
    private String importance;
    private String state;
    private long createdate;
    private long enddate;



    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add a task");
        setContentView(R.layout.activity_add_item);
        //get data from UI
        keywordEditText = findViewById(R.id.keyword_add_edittext);
        descriptionEditText = findViewById(R.id.description_add_edittext);
        locationEditText = findViewById(R.id.location_add_edittext);
        importanceSpinner = findViewById(R.id.importance_add);
        statusSpinner = findViewById(R.id.task_status_add);
        createTimeText = findViewById(R.id.create_time_add);
        endTimeText = findViewById(R.id.end_time_add);
        addTaskButton = findViewById(R.id.add_task);

        //create lists of items for the two spinner.
        importanceItems = new String[]{"low", "medium", "high"};
        statusItems = new String[]{"undone", "done"};
        //create an adapter to describe how the items are displayed
        importanceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, importanceItems);
        statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusItems);
        importanceSpinner.setAdapter(importanceAdapter);
        statusSpinner.setAdapter(statusAdapter);
        importanceSpinner.setSelection(0);
        statusSpinner.setSelection(0);

        //set time onTouchListemer
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        createTimeText.setText(month +"/"+ day +"/"+ year);

        endTimeText.setOnTouchListener(this);

        //get connect to the database
        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        addTaskButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //when the button is add_task
        switch (v.getId()) {
            case R.id.add_task:

                keyword = keywordEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                location = locationEditText.getText().toString();
                importance = importanceSpinner.getSelectedItem().toString();
                state = statusSpinner.getSelectedItem().toString();

                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy  HH:mm");

                Date createTemp = null;
                Date endTemp =null;
                try {
                    createTemp = (Date) sdf1.parse(createTimeText.getText().toString());
                    endTemp = (Date) sdf2.parse(endTimeText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                createdate = createTemp.getTime();
                enddate = endTemp.getTime();

                databaseManager.insert(keyword, description,createdate,enddate,importance, state, location);
                Cursor cursor = databaseManager.selectMaxId();
                String idTemp = cursor.getString(cursor.getColumnIndex("MAX(_id)"));
                id = Long.parseLong(idTemp);

                Intent main = new Intent(AddItemActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                //set an alarm
                reminder(v);


                break;
        }
    }

    //set a dialog for the datepicker and timepicker
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View view = View.inflate(this, R.layout.time_picker, null);
            final DatePicker datePicker = view.findViewById(R.id.date_picker);
            final TimePicker timePicker = view.findViewById(R.id.time_picker);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //create a alertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            builder.setTitle("set the deadline (reminder)");

            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), null);

            timePicker.setIs24HourView(true);
            timePicker.setHour(calendar.get(Calendar.HOUR));
            timePicker.setMinute(Calendar.MINUTE);

            //when touch the deadline editText box,
             if (v.getId() == R.id.end_time_add) {

                //when click the comfirm button OK
                builder.setPositiveButton("O K", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //to form date+time
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%02d/%02d/%d", datePicker.getMonth() + 1, datePicker.getDayOfMonth(), datePicker.getYear()));
                        sb.append("  ");
                        sb.append(timePicker.getHour()).append(":").append(timePicker.getMinute());
                        endTimeText.setText(sb);
                        dialog.cancel();
                    }
                });
            }
            Dialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }
    //a method that set a reminder at the deadline of the item when it added to the list
    public void reminder(View v){
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, Notification.class);
        alarmIntent.putExtra("id",id);
        PendingIntent sendBroadcast = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC_WAKEUP, enddate, sendBroadcast);
    }

}
