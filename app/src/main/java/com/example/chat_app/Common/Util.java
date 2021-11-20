package com.example.chat_app.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
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

}
