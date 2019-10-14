package au.anu.u6807681.listcrown;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//this is the start activity
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseManager databaseManager;
    private ListView listView;
    private Button calendar;

    //map columns from a cursor to Text
    private SimpleCursorAdapter sca;
    private String[] data = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION };
    private int[] ids = new int[] { R.id.id, R.id.keyword, R.id.description };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the default show text in the main page
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_notifier));

        calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(this);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        Cursor cursor = databaseManager.selectToMainPage();
        sca = new SimpleCursorAdapter(this, R.layout.activity_view_item, cursor, data, ids, 0);
        sca.notifyDataSetChanged();
        //listview get the data from the database
        listView.setAdapter(sca);

        // OnCLickListener for tasks which are listed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                // list the main info of tasks
                TextView idTextView = view.findViewById(R.id.id);
                TextView keywordTextView = view.findViewById(R.id.keyword);
                TextView descriptionTextView = view.findViewById(R.id.description);
                String id = idTextView.getText().toString();

                //when it is clicked, new intent and pass the chosen task id to the next activity
                Intent editIntent = new Intent(getApplicationContext(), ModifyItemActivity.class);
                editIntent.putExtra("id", id);

                startActivity(editIntent);
            }
        });

    }
    // this is the add button modification
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //jump to the add activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddItemActivity.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }

//    public void changeToCalender(View V) {
//        Intent checkCalendar = new Intent(this, DisplayMessageActivity.class);
//        startActivity(checkCalendar);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar:
                Intent checkCalendar = new Intent(this, DisplayMessageActivity.class);
                startActivity(checkCalendar);
        }
    }
}
