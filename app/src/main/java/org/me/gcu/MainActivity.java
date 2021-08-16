package org.me.gcu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.me.gcu.fragments.FragmentAdapter;
import org.me.gcu.fragments.WeatherAdapter;
import org.me.gcu.fragments.ViewFragment;

public class MainActivity extends AppCompatActivity {

    private Weather todayWeather = new Weather();
    private Weather dayAfterTomorrow = new Weather();
    private Weather tomorrow = new Weather();
    private TextView temperature, date, polution, wind, pressure, humidity, sunrise, sunset, uv, time;
    private static long timeUpdate;
    private ImageView icon;
    private ViewPager viewPager;
    private TabLayout tabs;
    private ProgressDialog progressDialog;
    public static int cityCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);

        progressDialog = new ProgressDialog(MainActivity.this);
        temperature = findViewById(R.id.toptemp);
        date = findViewById(R.id.topdate);
        polution = findViewById(R.id.toppolution);
        wind = findViewById(R.id.topwind);
        pressure = findViewById(R.id.toppresure);
        humidity = findViewById(R.id.tophumidity);
        uv = findViewById(R.id.topuv);
        sunrise = findViewById(R.id.topsurise);
        sunset = findViewById(R.id.topsunset);
        time = findViewById(R.id.time);
        icon = findViewById(R.id.ico);

        //get weather data from xml
        refreshWeather();
    }

    public WeatherAdapter getAdapter(int day) {
        return new WeatherAdapter(day == 1 ? tomorrow : dayAfterTomorrow);
    }

    private int XMLParse(String result) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            final InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));

            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            //get city from xml
            String citytitle = doc.getElementsByTagName("title").item(0).getTextContent();
            String city = citytitle.split(",")[0].substring(citytitle.split(",")[0].lastIndexOf(" ") + 1);
            //splits the nodes
            NodeList nList = doc.getElementsByTagName("item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Weather weather = new Weather();
                    weather.setCity(city);
                    Element elemtn = (Element) nNode;
                    String description = elemtn.getElementsByTagName("title").item(0).getTextContent();
                    //choose weather icon based on the data
                    if (description.contains("cloud")) {
                        weather.setIcon("outline_cloud_white_20");
                    } else if (description.contains("rain")) {
                        weather.setIcon("outline_umbrella_white_20");
                    } else {
                        weather.setIcon("outline_wb_sunny_white_20");
                    }
                    //get weather data and forecast
                    String data = elemtn.getElementsByTagName("description").item(0).getTextContent();
                    //splits the date into parts and we will get the part depending on its location
                    String[] parts = data.split(",");
                    // check if all the weather parts are there
                    if (parts.length < 10) {
                        weather.setSunset(parts[8]);
                        weather.setTemperature(parts[0].split(":")[1]);
                        weather.setUv(parts[6]);
                        weather.setWind(parts[1]);
                        weather.setPolution(parts[7]);
                        weather.setPressure(parts[4]);
                        weather.setHumidity(parts[5]);
                    } else {
                        weather.setSunrise(parts[9]);
                        weather.setSunset(parts[10]);
                        weather.setTemperature(parts[0].split(":")[1]);
                        weather.setUv(parts[7]);
                        weather.setWind(parts[3]);
                        weather.setPolution(parts[8]);
                        weather.setPressure(parts[5]);
                        weather.setHumidity(parts[6]);
                    }
                    // choose which weather data applies to which day
                    if (temp == 0) {
                        Calendar calendar = Calendar.getInstance();
                        //gets current date
                        weather.setDate(calendar.getTime().toString());
                        this.todayWeather = weather;
                    } else if (temp == 1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, 1);
                        weather.setDate(calendar.getTime().toString());
                        this.tomorrow = weather;
                    } else if (temp == 2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, 2);
                        weather.setDate(calendar.getTime().toString());
                        this.dayAfterTomorrow = weather;
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return 0;
        }
        // on success return 1
        return 1;
    }


    private boolean online() {
        //check if there is online connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getForecast() {
        //place data to the appropriate places
        String city = todayWeather.getCity();
        ActionBar actionBar = getSupportActionBar();
        //check if action bar is set to prevent crashes
        if (actionBar != null) {
            actionBar.setTitle(city);
        }
        //check if the data being placed exists
        if (todayWeather.getTemperature() != null) {
            date.setText(todayWeather.getDate());
            temperature.setText(todayWeather.getTemperature());
            if (todayWeather.getPolution().equals("")) {
                //if data doesn't exist then don't show blank space
                polution.setVisibility(View.GONE);
            } else {
                polution.setText(todayWeather.getPolution());
            }
            if (todayWeather.getWind().equals("")) {
                wind.setVisibility(View.GONE);
            } else {
                wind.setText(todayWeather.getWind());
            }
            if (todayWeather.getUv().equals("")) {
                uv.setVisibility(View.GONE);
            } else {
                uv.setText(todayWeather.getUv());
            }
            if (todayWeather.getPressure().equals("")) {
                pressure.setVisibility(View.GONE);
            } else {
                pressure.setText(todayWeather.getPressure());
            }
            if (todayWeather.getHumidity().equals("")) {
                humidity.setVisibility(View.GONE);
            } else {
                humidity.setText(todayWeather.getHumidity());
            }
            if (todayWeather.getSunrise() == null || todayWeather.getSunrise().equals("")) {
                sunrise.setVisibility(View.GONE);
            } else {
                sunrise.setText(todayWeather.getSunrise());
            }
            if (todayWeather.getSunset().equals("")) {
                sunset.setVisibility(View.GONE);
            } else {
                sunset.setText(todayWeather.getSunset());
            }
            //set appropriate icon
            if (todayWeather.getIcon().contains("rain")) {
                icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.outline_umbrella_white_48));
            } else if (todayWeather.getIcon().contains("cloud")) {
                icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.outline_cloud_white_48));
            } else {
                icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.outline_wb_sunny_white_48));
            }

            //check if data is updated
            if (timeUpdate <= 0) {
                time.setText("");
            } else {
                //convert millis into date variable
                Date date = new Date(timeUpdate);
                //get the date formated
                String format = android.text.format.DateFormat.getTimeFormat(this).format(date);
                time.setText(getString(R.string.updated_on, format));
            }
        }
        //after loading today weather then load the next 2 days
        getFutureForecast();
    }

    private void getFutureForecast() {
        try {
            FragmentAdapter fragad = new FragmentAdapter(getSupportFragmentManager());

            //get current date by name
            String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
            Calendar calendar = Calendar.getInstance();
            String tomorrow = days[calendar.get(Calendar.DAY_OF_WEEK)];
            calendar.add(Calendar.DATE, 1);
            String afterTomorrow = days[calendar.get(Calendar.DAY_OF_WEEK)];

            //tab1
            Bundle bundleTomorrow = new Bundle();
            bundleTomorrow.putInt("tab", 1);
            ViewFragment viewfragTomorrow = new ViewFragment();
            viewfragTomorrow.setArguments(bundleTomorrow);
            fragad.addFragment(viewfragTomorrow, tomorrow);

            //tab2
            Bundle bundle = new Bundle();
            bundle.putInt("tab", 2);
            ViewFragment viewfrag = new ViewFragment();
            viewfrag.setArguments(bundle);
            fragad.addFragment(viewfrag, afterTomorrow);

            fragad.notifyDataSetChanged();
            viewPager.setAdapter(fragad);
            tabs.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(viewPager.getCurrentItem(), true);
        } catch (Exception ignore) {
        }
    }

    public void chooseCity() {
        //after choosing the action button to change city then it shows your options
        final CharSequence[] cities = new CharSequence[]{"Glasgow", "London", "NewYork", "Oman", "Mauritius", "Bangladesh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change location")
                .setSingleChoiceItems(cities, 0, (dialog, which) -> {
                    cityCode = which;
                    Toast.makeText(getApplicationContext(), cities[which], Toast.LENGTH_SHORT).show();
                })
                //on positive button click hte it hides the dialog and refreshes the weather depending the choice
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    refreshWeather();
                })
                //on nevative button click it just closed the window
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }


    public static void storeTime() {
        //get current time in millis
        Calendar now = Calendar.getInstance();
        timeUpdate = now.getTimeInMillis();
    }

    public void refreshWeather() {
        //if there is a connection then update the weather else display the error
        if (online()) {
            new TodayWeather(this, progressDialog).execute();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class TodayWeather extends XMLRequest {
        public TodayWeather(Context context, ProgressDialog progressDialog) {
            super(context, progressDialog);
        }

        @Override
        public void onPreExecute() {
            loading = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String output) {
            super.onPostExecute(output);
        }

        @Override
        public int parseResponse(String response) {
            return XMLParse(response);
        }

        @Override
        public void updateMainUI() {
            getForecast();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //se the manu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //action buttons
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshWeather();
            return true;
        }
        if (id == R.id.action_city) {
            chooseCity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
