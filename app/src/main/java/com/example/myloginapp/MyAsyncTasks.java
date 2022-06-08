package com.example.myloginapp;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyAsyncTasks extends AsyncTask<String, String, String> {


    String myUrl = "https://cloudidentity.googleapis.com/v1/devices/-/deviceUsers:lookup?androidId=" ;
    //String myUrlMe =    "https://cloudidentity.googleapis.com/v1/devices/-/deviceUsers:lookup?userId=me";

    String TAG = "MyInfoAsyncTask999";
    String token = "";
    String androidID;

    MyAsyncTasks(String tokenParam, String androidParam) {
        token = tokenParam;
        androidID = androidParam;
        myUrl += androidID;
    }
    //@Override
    protected String doInBackground2(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog to show the user what is happening
    }

        @Override
        protected String doInBackground(String... strings) {

            // Fetch data from the API in the background.

            String result = "";
            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    //open a URL connection

                    urlConnection = (HttpsURLConnection) url.openConnection();
                    //headers.add("Authorization", "Bearer " + token.getTokenValue());
                    urlConnection.addRequestProperty("Authorization", "Bearer " + token);
                    Log.e(TAG, "doInBackground: token->" + token);

                    //???to make it post urlConnection.setRequestMethod("POST");
                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }

                    // return the data to onPostExecute method
                    Log.e(TAG, "doInBackground 999 : url" + url);
                    Log.e(TAG, "doInBackground 999999 googlebcreturned result data: " + result);
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute (String s){

            Log.e(TAG, "onPostExecute: " + s);
            // show results
        }

}