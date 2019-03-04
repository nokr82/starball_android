package com.devstories.starball_android.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.devstories.starball_android.R;

public class RootActivity extends Activity {

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("EEE");

            likedNoti();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("TTT");

        IntentFilter intentfilter = new IntentFilter("LIKED_NOTI");
        registerReceiver(mReceiver, intentfilter);

    }

    public Context getDialogContext() {
        Context context;
        if (getParent() != null) {
            context = getParent();
        } else {
            context = this;
        }
        return context;
    }

    public int getPixelSize(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return Math.round(dp * metrics.density);
    }

    public float getDpSize(long px) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return px * metrics.density;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void alert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getDialogContext());
        builder.setMessage(msg).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alert(String msg, final AlertListener alertListener) {
        if (alertListener != null && alertListener.before()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getDialogContext());
            builder.setMessage(msg);
            builder.setCancelable(false);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    if (alertListener != null) {
                        alertListener.after();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public boolean isWiFi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isAvailable();
    }

    public boolean is3G() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return mMobile.isAvailable();
    }

    public int getScreenWidth() {
        int width = 0;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth(); // deprecated
        }
        return width;
    }

    public int getScreenHeight() {
        int height = 0;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight(); // deprecated
        }
        return height;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
   /*
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
        // no-op
    }else{
        super.setRequestedOrientation(requestedOrientation);
    }
    */
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    @Override
    public void startActivity(Intent intent) {

        super.startActivity(intent);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void likedNoti() {

        System.out.println("likedNoti likedNoti");

        View layout = getLayoutInflater().inflate(R.layout.activity_liked_noti, null);

        /*
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
        */

        // LinearLayout rootLL = findViewById(R.id.rootLL);
        // rootLL.addView(layout);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSPARENT);

        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(layout, params);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}