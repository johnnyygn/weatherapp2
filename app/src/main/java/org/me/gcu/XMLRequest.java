package org.me.gcu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class XMLRequest extends AsyncTask<String, String, String> {

    private final String[] cityCodes = new String[]{"2648579", "2643743", "5128581", "287286", "934154", "1185241"};
    private final ProgressDialog dialog;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public int loading = 0;
    private int error, result;

    public XMLRequest(Context context, ProgressDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    public void onPreExecute() {
        //before the execution of the thread then display a dialog
        if (!dialog.isShowing()) {
            loading = 1;
            dialog.setMessage(context.getString(R.string.downloading_data));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    public String doInBackground(String... params) {
        //in background thread
        String response = getRSS(MainActivity.cityCode);
        if (result == 1) {
            error = parseResponse(response);
        }
        return "";
    }

    private String getRSS(int city) {
        String response = "";
        try {
            //get city id and return the xml from bbc
            URL url = new URL("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + cityCodes[city]);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            if (connect.getResponseCode() == 200) {
                //if the http connection is successful
                InputStreamReader in = new InputStreamReader(connect.getInputStream());
                BufferedReader buff = new BufferedReader(in);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = buff.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                response += sb.toString();
                buff.close();
                in.close();
                connect.disconnect();
                result = 1;
                MainActivity.storeTime();
            } else {
                result = 0;
            }
        } catch (IOException e) {
            //else result is 0 which means there was a problem
            e.printStackTrace();
            result = 0;
        }
        // return the response
        return response;
    }

    public void updateMainUI() {
    }

    public abstract int parseResponse(String response);

    @Override
    protected void onPostExecute(String args) {
        //after the thread execution dismiss the dialog
        if (loading == 1) {
            dialog.dismiss();
            loading = 0;
        }
        updateMainUI();
        handleTaskOutput();
    }

    protected final void handleTaskOutput() {
        //depending on the result show the appropriate error if one exists
        switch (result) {
            case 1:
                if (error == 0) {
                    Toast.makeText(context, context.getString(R.string.parsing_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case 0:
                Toast.makeText(context, context.getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
