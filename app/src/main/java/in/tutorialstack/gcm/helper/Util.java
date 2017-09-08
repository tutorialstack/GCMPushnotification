package in.tutorialstack.gcm.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Util {
    Context context;
    private static Util util;

    public static Util getInstance(Context context) {
        if (util == null) {
            util = new Util(context);
        }

        return util;
    }

    public Util(Context context) {
        this.context = context;
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
