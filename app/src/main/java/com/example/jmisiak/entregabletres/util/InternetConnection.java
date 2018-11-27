package com.example.jmisiak.entregabletres.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection {
    public static Boolean isConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
