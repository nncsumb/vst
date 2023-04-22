package nathan.csumb.vst;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import java.util.List;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class QuantityNotification {

    private static vstDAO mvstDAO;
    private final Context mContext;

    public QuantityNotification(Context context) {
        mContext = context;
    }
    public void start() {
        getDatabase();

        NotificationChannel channel = new NotificationChannel("vst_notify", "VST Notifications", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        int thresholdQuantity = 5;
        List<Vitamin> vitamins = mvstDAO.getVitaminsByUserId(userId);
        for (Vitamin vitamin : vitamins) {
            if (vitamin.getQuantity() < thresholdQuantity) {
                @SuppressLint("DefaultLocale") String message = String.format("%s is running low. Only %d left.", vitamin.getName(), vitamin.getQuantity());
                Notification notification = new NotificationCompat.Builder(mContext, "vst_notify").setContentTitle("Vitamin Low Quantity Alert").setContentText(message).setSmallIcon(R.drawable.notif).setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true).build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
                notificationManagerCompat.notify(vitamin.getVitaminId(), notification);
            }
        }
    }


    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(mContext, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();

    }
}
