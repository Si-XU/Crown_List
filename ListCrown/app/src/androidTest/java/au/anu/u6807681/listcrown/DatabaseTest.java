package au.anu.u6807681.listcrown;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
/**
 * @author Hao Cao
 *
 */
public class DatabaseTest {
    private DatabaseManager databaseManager;
    @Before
    public void setUp(){
        databaseManager = new DatabaseManager(InstrumentationRegistry.getTargetContext());
        databaseManager.open();
    }

    @Test
    public void testInsert(){
        Cursor cursor1 = databaseManager.selectToMainPage();
        databaseManager.insert("a","b",1234567,1234567,"low","done","ANU");
        Cursor cursor2  = databaseManager.selectToMainPage();
        assertTrue("database insert failed", cursor2.getCount()-cursor1.getCount()==1);
    }

    @Test
    public void testSelectMaxId(){
        Cursor cursor = databaseManager.selectMaxId();
        assertTrue("database select failed", cursor.getCount() == 1);
    }

    @Test
    public void testSelectToMainPageTrack(){
        Cursor cursor = databaseManager.selectToMainPageTrack();
        while (cursor.moveToNext()){
            String state = cursor.getString(cursor.getColumnIndex("state"));
            assertTrue("SelectToMainPageTrack() got wrong data",state.equals("undone"));
        }
    }

    @Test
    public void testDelete(){
        Cursor cursor1 = databaseManager.selectToMainPage();
        databaseManager.insert("a","b",1234567,1234567,"low","done","ANU");
        Cursor cursor2 = databaseManager.selectMaxId();
        String idTemp = cursor2.getString(cursor2.getColumnIndex("MAX(_id)"));
        long id = Long.parseLong(idTemp);
        databaseManager.delete(id);
        Cursor cursor3 = databaseManager.selectToMainPage();
        assertTrue("delete fail, "+cursor1.getCount()+"!="+cursor3.getCount(), cursor1.getCount()==cursor3.getCount());
    }

    @Test
    public void testUpdate(){
        databaseManager.insert("a","b",1234567,1234567,"low","done","ANU");

        Cursor cursor1 = databaseManager.selectMaxId();
        String idTemp = cursor1.getString(cursor1.getColumnIndex("MAX(_id)"));
        long id = Long.parseLong(idTemp);
        databaseManager.update(id,"b","b",1234567,"low","done","ANU");

        Cursor cursor2 = databaseManager.selectMaxId();
        String idTemp2 = cursor2.getString(cursor2.getColumnIndex("MAX(_id)"));
        long id2 = Long.parseLong(idTemp);

        Cursor cursor3 = databaseManager.selectToModify(id2);
        String keywordTemp = cursor3.getString(cursor3.getColumnIndex("keyword"));

        assertTrue("update fail", keywordTemp.equals("b"));
    }




}