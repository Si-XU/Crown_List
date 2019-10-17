package au.anu.u6807681.listcrown;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hao Cao
 *
 */
public class ModifyItemActivity extends Activity implements OnClickListener, View.OnTouchListener {


    private Button updateButton;
    private Button deleteButton;
    private EditText keywordText;
    private EditText descriptionText;
    private EditText locationText;
    private Spinner importanceSpinner;
    private Spinner statusSpinner;
    private ArrayAdapter<String> importanceAdapter;
    private ArrayAdapter<String> statusAdapter;
    private TextView createTimeText;
    private EditText endTimeText;
    private long time;


    private long id;

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Edit task");
        setContentView(R.layout.activity_modify_item);

        //connect with the database
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        keywordText = findViewById(R.id.keyword_edit_edittext);
        descriptionText = findViewById(R.id.description_edit_edittext);
        importanceSpinner = findViewById(R.id.importance_edit);
        statusSpinner = findViewById(R.id.task_status_edit);
        locationText = findViewById(R.id.location_edit_edittext);
        createTimeText = findViewById(R.id.create_time_edit);
        endTimeText = findViewById(R.id.end_time_edit);

        //create lists of items for the two spinner.
        String[] importanceItems = new String[]{"low", "medium", "high"};
        String[] statusItems = new String[]{"undone", "done"};
        //create an adapter to describe how the items are displayed
        importanceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, importanceItems);
        statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusItems);
        importanceSpinner.setAdapter(importanceAdapter);
        statusSpinner.setAdapter(statusAdapter);

        //buttons
        updateButton = findViewById(R.id.update);
        deleteButton = findViewById(R.id.delete);

        Intent intent = getIntent();
        String idTemp = intent.getStringExtra("id");
        id = Long.parseLong(idTemp);

        Cursor cursor = databaseManager.selectToModify(id);
        String keyword = cursor.getString(cursor.getColumnIndex("keyword"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String location = cursor.getString(cursor.getColumnIndex("location"));
        String importanceTemp = cursor.getString(cursor.getColumnIndex("importance"));
        String stateTemp = cursor.getString(cursor.getColumnIndex("state"));
        long createdate = Long.parseLong(cursor.getString(cursor.getColumnIndex("createdate")));
        long enddate = Long.parseLong(cursor.getString(cursor.getColumnIndex("enddate")));

        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy  HH:mm");

        Date date1 = new Date(createdate);
        Date date2 = new Date(enddate);
        String str1 = sdf1.format(date1);
        String str2 = sdf2.format(date2);

        //make the record to be the text in the EditText boxes
        keywordText.setText(keyword);
        descriptionText.setText(description);
        locationText.setText(location);
        createTimeText.setText(str1);
        endTimeText.setText(str2);
        endTimeText.setOnTouchListener(this);


        if (importanceTemp.equals("low") ){
            importanceSpinner.setSelection(0);
        }else if (importanceTemp.equals("medium")){
            importanceSpinner.setSelection(1);
        }else if (importanceTemp.equals("high")){
            importanceSpinner.setSelection(2);
        }

        if (stateTemp.equals("undone")){
            statusSpinner.setSelection(0);
        }else if (stateTemp.equals("done")){
            statusSpinner.setSelection(1);
        }

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                String keyword = keywordText.getText().toString();
                String description = descriptionText.getText().toString();
                String importance = importanceSpinner.getSelectedItem().toString();
                String state = statusSpinner.getSelectedItem().toString();
                String location = locationText.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  HH:mm");
                Date endTemp =null;
                try {
                    endTemp = (Date) sdf.parse(endTimeText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long enddate = endTemp.getTime();
                time = enddate;

                databaseManager.update(id, keyword, description, enddate, importance, state, location);
                this.returnHome();
                //update previous reminder
                reminderUpdate(v);
                break;

            case R.id.delete:
                databaseManager.delete(id);
                this.returnHome();
                //delete previous reminder
                reminderDelete(v);
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

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
            if (v.getId() == R.id.end_time_edit) {

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
    //a method that modify the reminder of the item after it been updated
    public void reminderUpdate(View v) {
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, Notification.class);
        alarmIntent.putExtra("id", id);
        PendingIntent sendBroadcast = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC_WAKEUP, time, sendBroadcast);
    }
    //a method that delete the reminder of the item after it been deleted
    public void reminderDelete(View v) {
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, Notification.class);
        alarmIntent.putExtra("id", id);
        PendingIntent sendBroadcast = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(sendBroadcast);
    }
}
