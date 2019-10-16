package au.anu.u6807681.listcrown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hao Cao
 *
 */
public class DatabaseManager {

    private MyDatabaseHelper databaseHelper;

    private Context context;

    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        databaseHelper = new MyDatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        databaseHelper.close();
    }

    public void insert(String keyword, String description,long createdate, long enddate, String importance, String state, String location) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(MyDatabaseHelper.KEYWORD, keyword);
        contentValue.put(MyDatabaseHelper.DESCRIPTION, description);
        contentValue.put(MyDatabaseHelper.CREATEDATE, createdate);
        contentValue.put(MyDatabaseHelper.ENDDATE, enddate);
        contentValue.put(MyDatabaseHelper.IMPORTANCE, importance);
        contentValue.put(MyDatabaseHelper.STATE, state);
        contentValue.put(MyDatabaseHelper.LOCATION, location);
        database.insert(MyDatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor selectToMainPage() {
        String[] columns = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION, MyDatabaseHelper.ENDDATE, MyDatabaseHelper.STATE };
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor selectToMainPageTrack() {
        String[] columns = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION, MyDatabaseHelper.ENDDATE, MyDatabaseHelper.STATE };
        String[] arg = new String[]{"undone"};
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, columns, "state = ?", arg, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor selectToModify(long _id) {
        String[] arg = new String[]{String.valueOf(_id)};
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, null, "_id = ?", arg, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectToCalendar() {
        String[] columns = new String[] { MyDatabaseHelper.ID, MyDatabaseHelper.KEYWORD, MyDatabaseHelper.DESCRIPTION, MyDatabaseHelper.ENDDATE };
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long id, String keyword, String description,long enddate, String importance, String state, String location) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(MyDatabaseHelper.KEYWORD, keyword);
        contentValue.put(MyDatabaseHelper.DESCRIPTION, description);
        contentValue.put(MyDatabaseHelper.ENDDATE, enddate);
        contentValue.put(MyDatabaseHelper.IMPORTANCE, importance);
        contentValue.put(MyDatabaseHelper.STATE, state);
        contentValue.put(MyDatabaseHelper.LOCATION, location);
        int i = database.update(MyDatabaseHelper.TABLE_NAME, contentValue, MyDatabaseHelper.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(MyDatabaseHelper.TABLE_NAME, MyDatabaseHelper.ID + "=" + id, null);
    }

}