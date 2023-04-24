package com.samvpn.app.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Helper {

    private static ProgressDialog pd;

    public static void saveServer(String key, int value, Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences("activeServer", 0);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString(key, value + "");
        editor.commit();
    }

    public static void deleteSaveServer(String key, Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences("activeServer", 0);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getSavedServer(String key, Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences("activeServer", 0);
        String data = sp.getString(key, "-1");
        return data;

    }


    public static long dateToUnix(String dt, String format) {
        SimpleDateFormat formatter;
        Date date = null;
        long unixtime;
        formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(dt);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        unixtime = date.getTime();
        return unixtime;

    }

    public static String getData(long unixTime, String formate) {

        long unixSeconds = unixTime;
        Date date = new Date(unixSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String getFormattedDate(String date, String currentFormat,
                                          String desiredFormat) {
        return getData(dateToUnix(date, currentFormat), desiredFormat);
    }




    public static double distance(double lat1, double lon1, double lat2,
                                  double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

     private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

     private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static int getRendNumber() {
        Random r = new Random();
        return r.nextInt(360);
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showLoder(Context context, String message) {
        pd = new ProgressDialog(context);

        pd.setCancelable(false);
        pd.setMessage(message);
        pd.show();
    }

    public static void showLoderImage(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setMessage(message);
        pd.show();
    }

    public static void dismissLoder() {
        pd.dismiss();
    }

    public static void toast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog showDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated method stub

            }
        });

        return builder.create();
    }

    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static boolean isURL(String url) {
        if (url == null)
            return false;

        boolean foundMatch = false;
        try {
            Pattern regex = Pattern
                    .compile(
                            "\\b(?:(https?|ftp|file)://|www\\.)?[-A-Z0-9+&#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]\\.[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]",
                            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher regexMatcher = regex.matcher(url);
            foundMatch = regexMatcher.matches();
            return foundMatch;
        } catch (PatternSyntaxException ex) {

            return false;
        }
    }

    public static boolean atLeastOneChr(String string) {
        if (string == null)
            return false;

        boolean foundMatch = false;
        try {
            Pattern regex = Pattern.compile("[a-zA-Z0-9]",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher regexMatcher = regex.matcher(string);
            foundMatch = regexMatcher.matches();
            return foundMatch;
        } catch (PatternSyntaxException ex) {

            return false;
        }
    }

    public static boolean isValidEmail(String email, Context context) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {


            return false;
        }
    }

    public static boolean isValidUserName(String email, Context context) {
        String expression = "^[0-9a-zA-Z]+$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            Helper.toast(context, "Username is not valid..!");
            return false;
        }
    }

    public static boolean isValidDateSlash(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isValidDateDash(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isValidDateDot(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

}