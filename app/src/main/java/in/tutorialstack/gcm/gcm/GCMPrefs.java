package in.tutorialstack.gcm.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GCMPrefs {
    private static final String PREFERENCE = "GCM";
    private static GCMPrefs gcmPrefs;

    private SharedPreferences gcmSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    Context context;

    public GCMPrefs(Context context) {
        this.context = context;
        this.gcmSharedPrefs = context.getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);
        this.prefsEditor = gcmSharedPrefs.edit();
    }

    public static GCMPrefs getGCMPrefs(Context context) {
        if (gcmPrefs == null) {
            gcmPrefs = new GCMPrefs(context);
        }

        return gcmPrefs;
    }

    public void commit() {
        prefsEditor.commit();
    }

    public void setRegistrationKey(String value) {
        prefsEditor.putString("prefs_gcm_reg_id", value).commit();
    }

    public String getRegistrationKey() {
        return gcmSharedPrefs.getString("prefs_gcm_reg_id", "");
    }

    public void setRegistrationAppVersion(int appVersion) {
        prefsEditor.putInt("prefs_app_version", appVersion).commit();
    }

    public int getRegistrationAppVersion() {
        return gcmSharedPrefs.getInt("prefs_app_version", 0);
    }

    public void setIsDeviceIdRegister(boolean isSetDeviceId) {
        prefsEditor.putBoolean("prefs_set_device_id", isSetDeviceId).commit();
    }

    public boolean getIsDeviceIdRegister() {
        return gcmSharedPrefs.getBoolean("prefs_set_device_id", false);
    }
}