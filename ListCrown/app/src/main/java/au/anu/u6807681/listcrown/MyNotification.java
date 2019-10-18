package au.anu.u6807681.listcrown;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

//@author Yun Huang
//class that receive broadcast and send notification
public class MyNotification extends BroadcastReceiver  {
    NotificationManager notificationManager;
    DatabaseManager databaseManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        //unique ID for every item
        long id = 0;
        id = intent.getLongExtra("id",id);
        databaseManager = new DatabaseManager(context);
        databaseManager.open();
        Cursor cursor = databaseManager.selectToModify(id);
        String keyword = cursor.getString(cursor.getColumnIndex("keyword"));
        String importance = cursor.getString(cursor.getColumnIndex("importance"));
        String state = cursor.getString(cursor.getColumnIndex("state"));
        //initialise a notification
        NotificationCompat.Builder notification;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //choose a proper channel to send a notification according to importance level of the item
        //customize characteristic for every channel
        //if (state.equals("undone")){
        switch (""+importance){
            case "high" :
                notification = new NotificationCompat.Builder(context, "H");
                notification.setContentTitle("Deadline!!!");
                notification.setContentText("" +keyword  +" is due");
                notification.setWhen(System.currentTimeMillis());
                notification.setSmallIcon(R.drawable.tick);
                notification.setAutoCancel(true);
                notification.setColor(Color.RED);
                notificationManager.notify((int) id, notification.build());
                break;
            case "medium" :
                notification = new NotificationCompat.Builder(context, "M");
                notification.setContentTitle("Reminder");
                notification.setContentText("" +keyword  +" is due");
                notification.setWhen(System.currentTimeMillis());
                notification.setSmallIcon(R.drawable.tick);
                notification.setAutoCancel(true);
                notificationManager.notify((int) id, notification.build());
                break;
            case "low" :
                break;
        }
       // }
    }
}
