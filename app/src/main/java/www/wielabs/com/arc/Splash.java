package www.wielabs.com.arc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import www.wielabs.com.arc.activity.AccountKitLoginActivity;
import www.wielabs.com.arc.activity.Login;
import www.wielabs.com.arc.activity.MainActivity;
import www.wielabs.com.arc.activity.Profile;
import www.wielabs.com.arc.activity.Registration;


public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
                Boolean isLoggedIn = preferences.getBoolean("isLoggedIn",false);
                if (isLoggedIn) {
                Intent i = new Intent(Splash.this, Profile.class);
                startActivity(i);
                } else {

                    Intent i = new Intent(Splash.this, Login.class);
                                 startActivity(i);
                            }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

