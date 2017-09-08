package in.tutorialstack.gcm.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.tutorialstack.gcm.MainActivity;
import in.tutorialstack.gcm.R;

public class GcmMessageHandler extends IntentService {
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String msg = null, title = null, image = null;
            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);
            JSONObject object = new JSONObject(extras.getString("message"));
            String type = object.getString("type");
            msg = object.getString("message");
            //image = object.getString("image");

            saveNotification(title, msg, image);
            generateNotification(getApplicationContext(), title, msg, image, type);
        } catch (Exception ex) {
            Log.e("Message", ex.getMessage());
        }
    }

    private void saveNotification(String title, String message, String image) {
        //NotificationDb.getInstance(this).insertNotification(title, message, image);
    }

    private void generateNotification(Context context, String title, String message, String image_url, String type) {
        Bitmap message_bitmap = null;
        if ((image_url != null) && (!image_url.equals(""))) {
            String sample_url = image_url;
            message_bitmap = getBitmapFromURL(sample_url);
        }

        int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //title = context.getString(R.string.app_name);
        if ((title == null) || (title.equals(""))) {
            title = context.getString(R.string.app_name);
        }

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("Type", type);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent intent = PendingIntent.getActivity(context, uniqueInt, notificationIntent, 0);

        Notification notification = null;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if (message_bitmap == null) {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setTicker(title)
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setSmallIcon(icon)
                    .setLargeIcon(largeIcon)
                    .setWhen(when)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(getNotificationSound())
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setTicker(title)
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setSmallIcon(icon)
                    .setLargeIcon(largeIcon)
                    .setWhen(when)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .setSummaryText(message)
                            .bigPicture(message_bitmap))
                    .setSound(getNotificationSound())
                    .build();
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify((int) when, notification);
    }

    private Uri getNotificationSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException ex) {
            Log.e("Download Bitmap", ex.getMessage());
            return null;
        }
    }
}