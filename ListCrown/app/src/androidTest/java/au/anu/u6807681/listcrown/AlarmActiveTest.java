package au.anu.u6807681.listcrown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.view.View;

import org.junit.Before;
import org.junit.Test;

import static android.content.Context.ALARM_SERVICE;
import static org.junit.Assert.*;

public class AlarmActiveTest {
    public Context context;
    @Before
    public void setUp() throws Exception {
       context = InstrumentationRegistry.getTargetContext();
    }

    //The mechanism for this test is FLAG_NO_CREATE will return null when the PendingIntent does not
    //already exist
    @Test
    public void testAlarmActive() {
        AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, Notification.class);
        PendingIntent sendBroadcast = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60*1000, sendBroadcast);
        boolean active = (PendingIntent.getBroadcast(context,0,alarmIntent,PendingIntent.FLAG_NO_CREATE))!=null;
        assertFalse("failed, ",active);
    }

}