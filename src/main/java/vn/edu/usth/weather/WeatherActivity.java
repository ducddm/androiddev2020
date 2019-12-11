package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    MediaPlayer music;
    URL url;

    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // This method is executed in main thread
            String content = msg.getData().getString("server_response");
            Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        HomeFragmentPaperAdapter adapter = new HomeFragmentPaperAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);


        // Give the TabLayout the ViewPager
        //ForecastFragment firstFragment = new ForecastFragment();
        // Add the fragment to the ' container' FrameLayout
        //getSupportFragmentManager(). beginTransaction(). add(
        //R.id.container, firstFragment). commit();

        copyFileToExternalStorage(R.raw.musique, "musique.mp3");

        music = MediaPlayer.create(WeatherActivity.this, R.raw.musique);
        music.start();

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String content = msg.getData().getString("server_response");
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
            }
        };
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // wait for 5 seconds to simulate a long network access
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Assume that we got our data from server
                Bundle bundle = new Bundle();
                bundle.putString("server_response", "some sample json here");
                // notify main thread
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        t.start();


    }
    private void request(){
        // once, should be performed once per app instance
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // a listener (kinda similar to onPostExecute())
        Response.Listener<Bitmap> listener =
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ImageView iv = (ImageView) findViewById(R.id.logo);
                        iv.setImageBitmap(response);
                    }
                };
        // a simple request to the required image
        ImageRequest imageRequest = new ImageRequest(
                "https://ictlab.usth.edu.vn/wp-content/uploads/logos/usth.png",
                listener, 0, 0, ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "Response is: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // go!
        queue. add(imageRequest);
    }
    private class AsyncTaskRunner extends AsyncTask<URL, Integer, Bitmap> {
        Bitmap bitmap;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            try {
                url = new URL("https://ictlab.usth.edu.vn/wp-content/uploads/logos/usth.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // do some preparation here, if needed
            progressDialog = ProgressDialog.show(WeatherActivity.this,
                    "Updating weather...",
                    "Wait for 5 seconds!");
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            try {
                Thread.sleep(5000);
                // Make a request to server
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                // allow reading response code and response dataconnection.
                connection.connect();
                // Receive response
                int response = connection.getResponseCode();
                Log.i("USTHWeather", "The response is: " + response);
                InputStream is = connection.getInputStream();
                // Process image response
                bitmap = BitmapFactory.decodeStream(is);

                connection.disconnect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // execution of result of Long time consuming operation
            // Assume that we got our data from server
            ImageView logo = (ImageView) findViewById(R.id.logo);
            logo.setImageBitmap(bitmap);
            progressDialog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putString("server_response", "some sample json here");
            // notify main thread
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            // Do something here
        }

    }

    

    private void copyFileToExternalStorage(int resourceId, String resourceName) {
        String pathSDCard = Environment.getExternalStorageDirectory()
                + "/Android/data/vn.edu.usth.weather/" + resourceName;
        try {
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.refresh:
                request();
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("THIS", "Welcome to USTH Weather");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("THIS", "Welcome to  Weather");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("THIS", "USTH Weather");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("THIS", "Weather");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("THIS", "to");
    }


}
