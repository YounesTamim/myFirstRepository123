package algonquin.cst2335.inclassexamples_s21;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    EditText theEdit;
    TextView tv;
    Button btn;
    boolean isHidden = false;

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.nav_clear:

            case R.id.hide_views:
                tv.setVisibility((isHidden)?View.INVISIBLE:View.VISIBLE);
                break;
            case R.id.nav_increase:
            case R.id.increase:
                float oldSize = theEdit.getTextSize();
                float newSize = Float.max(oldSize+1,5);
                tv.setTextSize(newSize);
                theEdit.setTextSize(newSize);
                btn.setTextSize(newSize);
                break;

            case R.id.nav_decrease:
            case R.id.decrease:
                oldSize = theEdit.getTextSize();
                newSize = Float.max(oldSize+1,5);
                tv.setTextSize(newSize);
                theEdit.setTextSize(newSize);
                btn.setTextSize(newSize);
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolBar = findViewById(R.id.myToolBar);
        setSupportActionBar(myToolBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,myToolBar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.myNavView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                onOptionsItemSelected(item);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });


        tv = findViewById(R.id.textView);
        Button forecastButton = findViewById(R.id.forecastButton);
        EditText cityText = findViewById(R.id.cityTextField);


    forecastButton.setOnClickListener((click)->  {


        String cityName = cityText.getText().toString();

        myToolBar.getMenu()
                .add(0,5,0,cityName)
                .setIcon(R.drawable.clear)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Getting Forecast")
                .setMessage("We're calling people in "+ cityName+ " to look outside their windows and tell us what's the weather like over there.")
                .setView(new ProgressBar(MainActivity.this))
                .show();

        Executor newThread = Executors.newSingleThreadExecutor();

        newThread.execute( () -> {
            try {

                String serverURL ="https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}"
                        + URLEncoder.encode(cityName,"UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&Units=Metric&mode=xml";

                URL url = new URL(serverURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));


                JSONObject theDocument = new JSONObject(text);
                JSONArray theArray = new JSONArray( text );
                JSONObject coord = theDocument.getJSONObject( "coord" );
                JSONArray weatherArray = theDocument.getJSONArray ( "weather" );
                JSONObject position0 = weatherArray.getJSONObject(0);
                int vis = theDocument.getInt("visibility");
                String name = theDocument.getString( "name" );
                String description = position0.getString("description");
                String iconName = position0.getString("icon");

                JSONObject mainObject = theDocument.getJSONObject( "main" );
                double current = mainObject.getDouble("temp");
                double min = mainObject.getDouble("temp_min");
                double max = mainObject.getDouble("temp_max");
                int humidity = mainObject.getInt("humidity");

                double finalCurrent = current;
                runOnUiThread(()->{
                TextView tv = findViewById(R.id.temp);
                tv.setText("The current temperature is "+ finalCurrent);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.minTemp);
                tv.setText("The min temperature is "+ finalCurrent);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.maxTemp);
                tv.setText("The max temperature is "+ finalCurrent);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.humidity);
                tv.setText("The humidity is "+ finalCurrent);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.description);
                tv.setText("The Description is "+ finalCurrent);
                tv.setVisibility(View.VISIBLE);

                dialog.hide();
});

                Bitmap image = null;
                URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    ImageView iv= findViewById(R.id.icon);
                    iv.setImageBitmap(image);
                }
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( in  , "UTF-8");

                while( xpp.next() != XmlPullParser.END_DOCUMENT )
                {
                    switch (xpp.getEventType())
                    {
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equals("temperature")) {
                                current = Double.parseDouble(xpp.getAttributeValue(null, "value"));
                                min = Double.parseDouble(xpp.getAttributeValue(null, "min"));
                                max = Double.parseDouble(xpp.getAttributeValue(null, "max"));
                            }
                            else if(xpp.getName().equals("weather"))
                            {
                                description = xpp.getAttributeValue(null, "value");
                                iconName = xpp.getAttributeValue(null, "icon");
                            }
                            else if(xpp.getName().equals("humidity"))
                            {
                                humidity = Integer.parseInt(xpp.getAttributeValue(null,"value"));

                            }
                            break;

                        case XmlPullParser.END_TAG:

                            break;
                        case XmlPullParser.TEXT:

                            break;
                    }
                }
            }
            catch(IOException | JSONException | XmlPullParserException ioe){
                Log.e("Connection Error",ioe.getMessage());
            }
        });
    });
    }
}