package in.tutorialstack.gcm.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import in.tutorialstack.gcm.helper.Util;

public class GCMRegistration {
    Context context;
    String regId;
    GoogleCloudMessaging gcm;
    static final String TAG = "GCMRegistration";
    public static GCMRegistration gcmRgistration;

    public GCMRegistration(Context context) {
        this.context = context;
    }

    public static GCMRegistration getInstance(Context context) {
        if (gcmRgistration == null) {
            gcmRgistration = new GCMRegistration(context);
        }

        return gcmRgistration;
    }

    public String Init() {
        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            Log.d("RegisterActivity", "Already Registered with GCM Server! " + regId);
        }

        return regId;
    }

    public String registerGCM() {
        gcm = GoogleCloudMessaging.getInstance(context);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
            Log.d(TAG, "registerGCM - successfully registered with GCM server - regId: " + regId);
        } else {
            //Toast.makeText(context,	"RegId already available. RegId: " + regId,	Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        String registrationId = GCMPrefs.getGCMPrefs(context).getRegistrationKey();
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = GCMPrefs.getGCMPrefs(context).getRegistrationAppVersion();
        int currentVersion = Util.getInstance(context).getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }

        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register("");  // put google product sender key here...
                    Log.d("RegisterActivity", "registerInBackground - regId: " + regId);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }

                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("Registration", "Registered with GCM Server.");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        GCMPrefs.getGCMPrefs(context).setRegistrationAppVersion(Util.getInstance(context).getAppVersion());
        GCMPrefs.getGCMPrefs(context).setRegistrationKey(regId);
    }
}
