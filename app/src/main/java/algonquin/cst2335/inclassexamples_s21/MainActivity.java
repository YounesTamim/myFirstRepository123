package algonquin.cst2335.inclassexamples_s21;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastButton = findViewById(R.id.forecastButton);
        EditText cityText = findViewById(R.id.cityTextField);


    forecastButton.setOnClickListener((click)->  {
        String cityName = cityText.getText().toString();

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
runOnUiThread(()->{
                TextView tv = findViewById(R.id.temp);
                tv.setText("The current temperature is "+ current);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.minTemp);
                tv.setText("The min temperature is "+ current);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.maxTemp);
                tv.setText("The max temperature is "+ current);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.humidity);
                tv.setText("The humidity is "+ current);
                tv.setVisibility(View.VISIBLE);

                tv = findViewById(R.id.description);
                tv.setText("The Description is "+ current);
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
                                current = xpp.getAttributeValue(null, "value");
                                min = xpp.getAttributeValue(null, "min");
                                max = xpp.getAttributeValue(null, "max");
                            }
                            else if(xpp.getName().equals("weather"))
                            {
                                description = xpp.getAttributeValue(null, "value");
                                iconName = xpp.getAttributeValue(null, "icon");
                            }
                            else if(xpp.getName().equals("humidity"))
                            {
                                humidity = xpp.getAttributeValue(null,"value");

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