package au.anu.u6807681.listcrown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * @author Hao Cao
 *
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    //define the name of the table
    public static final String TABLE_NAME = "ITEM";
    // define the columns of the table
    // There is a bug when using "id", it seems not allowed to use "id" as identification.
    public static final String ID = "_id";
    public static final String KEYWORD = "keyword";
    public static final String DESCRIPTION = "description";
    public static final String CREATEDATE = "createdate";
    public static final String ENDDATE = "enddate";
    //public static final String REMINDERTIME = "reminderTime";
    public static final String IMPORTANCE = "importance";
    public static final String STATE = "state";
    public static final String LOCATION = "location";

    //database name
    static final String DATABASE_NAME = "LIST_CROWN";

    // database version
    static final int VERSION = 1;

    //Create the table
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
//            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + KEYWORD + " TEXT NOT NULL, "
//            + DESCRIPTION + " TEXT, "
//            + STARTDATE + " DATETIME, "
//            + ENDDATE + " DATETIME,"
//            + REMINDERTIME +" TEXT, "
//            + IMPORTANCE +" TEXT, "
//            + STATE + " TEXT, "
//            + LOCATION +" TEXT);";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEYWORD + " TEXT NOT NULL, "
            + DESCRIPTION + " TEXT, "
            + CREATEDATE + " INTEGER, "
            + ENDDATE + " INTEGER, "
            + IMPORTANCE +" TEXT, "
            + STATE + " TEXT, "
            + LOCATION +" TEXT);";
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "( " + ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEYWORD + " TEXT NOT NULL, " + DESCRIPTION + " TEXT);";

    public MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
