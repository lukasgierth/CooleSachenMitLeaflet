package org.hsbo.gierthhensen.coolesachenmitleaflet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint({"SetJavaScriptEnabled", "ShowToast"})
public class MapView extends AppCompatActivity implements LocationListener {

    public LocationManager locman;
    public String city = "London";

    /**
     * Methods
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Show Toast if no internet connection
         */
        if (!isOnline()){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");
        myWebView.addJavascriptInterface(new JSInterface(this), "Android");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(city.equals("London")) {
                    Snackbar.make(view, "Go To Bochum", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    String lat = "51.4818";
                    String lon = "7.2162";

                    setLocationOnMap(lat, lon, "Bochum");
                    city= "Bochum";
                }
                else {
                    Snackbar.make(view, "Back To London", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //51.505, -0.09
                    String lat = "51.505";
                    String lon = "-0.09";

                    setLocationOnMap(lat, lon, "London");
                    city = "London";
                }

            }
        });
    }

    public void setLocationOnMap(String lat, String lon, String cityName) {
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("javascript:toCity(" + lat + "," + lon+ "," + "'" +cityName+ "'" +");");
    }

    /**
     * Momentan nicht genutzt, noch fehlerhaft
     */

    /*
    public void getPosition() {
        String lat, lon;
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        boolean isNW = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isNW) {

        } else {
            Location loc;

            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                loc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lat = Double.toString(loc.getLatitude());
                lon = Double.toString(loc.getLongitude());
                String coords = (lon + "," + lat);

               // setLocationOnMap(coords);
            } catch (Exception e){

            }
        }
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bplanwms) {

            WebView webView = (WebView) findViewById(R.id.webview);
            webView.loadUrl("javascript:toggleWMS1();");

            return true;
        }

        if (id == R.id.histlanduse) {

            WebView webView = (WebView) findViewById(R.id.webview);
            webView.loadUrl("javascript:toggleWMS2();");

            return true;
        }

        if (id == R.id.osmbase) {

            WebView webView = (WebView) findViewById(R.id.webview);
            webView.loadUrl("javascript:toggleBase();");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else {return false;}
    }

public class JSInterface {
    Context context;

    JSInterface(Context c){
        context = c;
    }

    @JavascriptInterface
    public void cityToast() {

        if(city.equals("London")) {
            Toast.makeText(context, "London", Toast.LENGTH_SHORT).show();
        }
        else if (city.equals("Bochum")){
            Toast.makeText(context, "Bochum", Toast.LENGTH_SHORT).show();
        }

    }


}

    @Override
    public void onLocationChanged(Location location){}

    @Override
    public void onProviderDisabled(String location){}

    @Override
    public void onProviderEnabled(String location){}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}
}