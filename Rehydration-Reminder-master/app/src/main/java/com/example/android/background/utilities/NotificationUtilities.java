package com.example.android.background.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;
import com.example.android.background.sync.ReminderTasks;
import com.example.android.background.sync.WaterReminderIntentService;

public class NotificationUtilities {

    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;

    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;

    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID =
            "reminder_notification_channel";

    private static final int ACTION_DRINK_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    public static void clearAllNotification(Context context) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }


    public static void remindUserBecauseCharging(Context context) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        }

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static Action ignoreReminderAction(Context context) {

        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);

        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
                "No , Thanks",
                ignoreReminderPendingIntent);

        return ignoreReminderAction;

    }

    private static NotificationCompat.Action drinkWaterAction(Context context) {

        Intent incrementWaterCountIntent = new Intent(context, WaterReminderIntentService.class);

        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Action drinkWaterAction = new Action(R.drawable.ic_local_drink_black_24px,
                "I did it !",
                incrementWaterPendingIntent);

        return drinkWaterAction;


    }


    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);


        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);

        return largeIcon;

    }


}