package org.hsbo.gierthhensen.coolesachenmitleaflet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint({"SetJavaScriptEnabled"})
public class MapView extends AppCompatActivity implements LocationListener {

    public LocationManager locman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("file:///android_asset/index.html");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search Own Location", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                String lat = "51.8000";
                String lon = "7.7777";
                setLocationOnMap(lat, lon);

            }
        });
    }

    public void setLocationOnMap(String lat, String lon) {
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("javascript:callFromActivity(" + lat + "," + lon+ ");");

    }

    /**
     * Momentan nicht genutzt, noch fehlerhaft
     */
    public void getPosition() {
        String lat, lon;
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        boolean isNW = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNW == false) {

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
        if (id == R.id.action_settings) {

            WebView webView = (WebView) findViewById(R.id.webview);
            webView.loadUrl("javascript:toggleLayer();");

            return true;
        }

        return super.onOptionsItemSelected(item);
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