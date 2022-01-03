package com.example.chat_app.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.common.api.Response;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class Util {
    public static boolean connectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        } else {
            return false;
        }
    }
    public static  String getTimeAgo(long time)
    {
        final  int SECOND_MILLIS = 1000;
        final  int MINUTE_MILLIS= 60 * SECOND_MILLIS;
        final  int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final  int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();

        if(time>now || time <=0)
        {
            return  "";
        }

        final  long diff = now-time;

        if(diff<MINUTE_MILLIS)
        {
            return  "właśnie teraz";
        }
        else if(diff <2* MINUTE_MILLIS)
        {
            return  "minutę temu";
        }
        else if(diff <59*MINUTE_MILLIS)
        {
            return  diff/MINUTE_MILLIS + " minut temu";
        }
        else  if(diff < 90 * MINUTE_MILLIS)
        {
            return "godzinę temu";
        }
        else if(diff<24*HOUR_MILLIS){
            return  diff/HOUR_MILLIS + " godzin temu";
        }
        else if( diff < 48 * HOUR_MILLIS)
        {
            return  "wczoraj";
        }
        else
        {
            return  diff/DAY_MILLIS  + " dni temu";
        }

    }
}
