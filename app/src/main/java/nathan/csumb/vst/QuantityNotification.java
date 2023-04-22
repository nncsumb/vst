package nathan.csumb.vst;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import java.util.List;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class QuantityNotification {

    private Context mContext;
    private static vstDAO mvstDAO;

    public QuantityNotification(Context context) {
        mContext = context;
    }

    public void start() {
        getDatabase();
        // Set up the notification channel
        NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        // Schedule a repeating task using AlarmManager
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, VitaminNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.RTC_WAKEUP, pendingIntent);
    }

    // In the VitaminNotificationReceiver class, check the quantity of each vitamin and create a notification for any whose quantity falls below the threshold
    public static class VitaminNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userId", -1);
            int thresholdQuantity = sharedPreferences.getInt("thresholdQuantity", -1);
            List<Vitamin> vitamins = mvstDAO.getVitaminsByUserId(userId);
            for (Vitamin vitamin : vitamins) {
                if (vitamin.getQuantity() < thresholdQuantity) {
                    @SuppressLint("DefaultLocale") String message = String.format("%s is running low. Only %d left.", vitamin.getName(), vitamin.getQuantity());
                    Notification notification = new NotificationCompat.Builder(context, "my_channel_id")
                            .setContentTitle("Vitamin Low Quantity Alert")
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .build();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    notificationManager.notify(vitamin.getVitaminId(), notification);
                }
            }
        }
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(mContext, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getvstDAO();

    }
}
