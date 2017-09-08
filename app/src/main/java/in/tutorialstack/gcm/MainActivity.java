package in.tutorialstack.gcm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.tutorialstack.gcm.gcm.GCMPrefs;
import in.tutorialstack.gcm.gcm.GCMRegistration;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // getting registration key from GCM
        if (GCMPrefs.getGCMPrefs(context).getRegistrationKey().length() == 0) {
            GCMRegistration.getInstance(context).Init();
        }
    }
}