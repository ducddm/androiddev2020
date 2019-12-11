package vn.edu.usth.weather;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherAndForecastFragment extends Fragment {

    private String title;
    private int page;
    private TextView tempText;
    private TextView weather;
    private TextView lo;
    private ImageView weatherImage;
    private RequestQueue requestQueue;

    protected static WeatherAndForecastFragment newInstance(int page, String title) {
        WeatherAndForecastFragment weatherAndForecastFragment = new WeatherAndForecastFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        weatherAndForecastFragment.setArguments(args);
        return weatherAndForecastFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_and_forecast, container, false);
        RelativeLayout weather_container = (RelativeLayout) view.findViewById(R.id.weather_container);

        tempText = (TextView) weather_container.findViewById(R.id.current_temp);
        weather = (TextView) weather_container.findViewById(R.id.current_weather);
        lo = (TextView) weather_container.findViewById(R.id.location);
        weatherImage = (ImageView) weather_container.findViewById(R.id.image_weather);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        switch (title) {
            case "Hanoi":
                jsonParse("Hanoi,vn"); break;
            case "Paris":
                jsonParse("Paris,fr"); break;
            case "Berlin":
                jsonParse("Berlin,ger"); break;
        }


        // Inflate the menu_main for this fragment
        return view;
    }

    private void jsonParse(String loc){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + loc + "&APPID=" + getResources().getString(R.string.api_key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array1 = response.getJSONArray("weather");
                    JSONObject ob11 = array1.getJSONObject(0);
                    String weather_condition = ob11.getString("main");

                    JSONObject ob2 = response.getJSONObject("main");
                    String temp = ob2.getString("temp_min") + "F - " + ob2.getString("temp_max") + "F";

                    String loca = response.getString("name");

                    weather.setText(weather_condition);
                    tempText.setText(temp);
                    lo.setText(loca);
                    if (weather_condition.contains("Clouds") || (weather_condition.contains("Clear"))) {
                            weatherImage.setImageResource(R.drawable.cloudy);
                    } else if (weather_condition.contains("Sun")) {
                            weatherImage.setImageResource(R.drawable.sunny);
                    } else if (weather_condition.contains("Haze")) {
                            weatherImage.setImageResource(R.drawable.snow);
                    } else if (weather_condition.contains("Storm")) {
                            weatherImage.setImageResource(R.drawable.storm);
                    } else if (weather_condition.contains("Rain") || (weather_condition.contains("Drizzle"))) {
                            weatherImage.setImageResource(R.drawable.rain);
                    }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}

