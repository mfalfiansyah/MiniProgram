package com.mini.program.helper;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.mini.program.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class GlobalHelper {
    Context         context;
    DatabaseHelper  dbhelper;
    String[]        array;
    boolean         v_returnboolean;

    public GlobalHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void Profile(String orientation) {
        /*Function Profile Tampilan Menu, Fungsi Parameter :
         * orientationStyle => PORTRAIT atau selainnya*/
        Activity activity    = (Activity) context;
        //Pengecekan Perijinan
        if ((ContextCompat.checkSelfPermission( context, Manifest.permission
                .ACCESS_FINE_LOCATION)    != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( context, Manifest.permission
                .ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( context, Manifest.permission
                .CAMERA)                  != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( context, Manifest.permission
                .READ_EXTERNAL_STORAGE)   != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( context, Manifest.permission
                .WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission( context, Manifest.permission
                .RECORD_AUDIO)            != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions( activity, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO}, 10);}

        Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Objects.requireNonNull(window.getDecorView().getWindowInsetsController())
                    .setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS,
                            APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            int flags = window.getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(flags);
        }

        if (orientation.equals("PORTRAIT"))
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (orientation.equals("LANDSCAPE"))
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void GenerateDefaultData () {
        /*Function Generat Data awal Internal setting dan
         * Data awal Language/Message dari Values Array ke Database*/
        dbhelper = new DatabaseHelper(context.getApplicationContext());
        String[] firstmessage = context.getResources().getStringArray(R.array.firstmessage);
        for (String s : firstmessage) {
            String[] tData = (s + " ").split("#,#");
            dbhelper.insert_GS01(tData[0], tData[1], tData[2], tData[3], tData[4],
                    tData[5], tData[6]);
        }
    }

    public String get_message (String idmessage) {
        /*Function Untuk Mendapatkan Text Message (GS_13), Parameter = idmessage*/
        dbhelper = new DatabaseHelper(context.getApplicationContext());
        String query = "" +
                "SELECT CASE WHEN 'LANGUAGE1' = 'LANGUAGE1' THEN language1 " +
                "        END language " +
                "  FROM gs_01 " +
                " WHERE idmessage = '" + idmessage + "'";
        
        String msg = dbhelper.get_string(query, 0);
        if (msg.equals("")) msg = "N/A";
        return msg;
    }

    public boolean mandatoryAC (AutoCompleteTextView field, TextInputLayout ly_field) {
        v_returnboolean = true;
        if (ly_field.getVisibility() == View.VISIBLE &&
                field.getText().toString().isEmpty()) {
            field.setError(get_message("msg_mandatory"));
            field.requestFocus();
            v_returnboolean = false;
        }
        return v_returnboolean;
    }

    public boolean mandatoryET (EditText field, TextInputLayout ly_field) {
        v_returnboolean = true;
        if (ly_field.getVisibility() == View.VISIBLE &&
                field.getText().toString().isEmpty()) {
            field.setError(get_message("msg_mandatory"));
            field.requestFocus();
            v_returnboolean = false;
        }
        return v_returnboolean;
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        return cal1.equals(cal2);
    }


    public String[] split (String dataString, String sparator) {
        String[] strings = (dataString + " ").split(sparator);
        strings[strings.length-1] = strings[strings.length-1].substring(0,
                strings[strings.length-1].length() - 1);
        return strings;
    }

    public void closeDB(DatabaseHelper db) {
        db.close();
    }

    /** @noinspection rawtypes*/
    public void moveActivity(Class classActivity, String transition) {
        /*Function Pindah Activity/Class, Fungsi Parameter :
         * transitionType => RTL = Perpindahan dari Kanan ke Kiri
         *                   LTR = Perpindahan dari Kiri ke Kanan*/
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, classActivity);
        activity.startActivity(intent);
        activity.finish();
        if (transition.equals("RTL"))
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        if (transition.equals("LTR"))
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
