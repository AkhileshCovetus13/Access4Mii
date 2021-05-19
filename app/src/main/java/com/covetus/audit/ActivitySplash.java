package com.covetus.audit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.google.firebase.iid.FirebaseInstanceId;

import ABS_HELPER.PreferenceManager;
import butterknife.ButterKnife;

public class ActivitySplash extends Activity {


    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        //System.out.println("<><><><> "+executeCmd("ping -c 1 -w 1 google.com", false));


    /*delay splash for 3 sec*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceManager.getFormiiIsLogin(getApplicationContext()).equals("1")) {
                    Intent i = new Intent(ActivitySplash.this, ActivityTabHostMain.class);
                    i.putExtra("mStrCurrentTab", "0");
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PreferenceManager.setFormiiDeviceId(ActivitySplash.this, refreshedToken);
        System.out.println("<><>token" + PreferenceManager.getFormiiDeviceId(ActivitySplash.this));


    }

   /* public boolean isInternetAvailable() {
        boolean result = false;
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            result = ipAddr.isReachable(1000);
        } catch (Exception e) {
            return result;
        }
        return result;
    }*/

   /* public static String executeCmd(String cmd, boolean sudo){
        try {
            Process p;
            if(!sudo)
                p= Runtime.getRuntime().exec(cmd);
            else{
                p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/

}