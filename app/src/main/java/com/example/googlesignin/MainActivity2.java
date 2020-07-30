package com.example.googlesignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    TextView adress;
    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();
    boolean isGpsEnable = false;
    boolean isNetworkEnable = false;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        adress=findViewById(R.id.textView2);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getMyLocation();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                GoogleSignInClient gcv= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
                gcv.revokeAccess();
                if(isFacebookLoggedIn()){
                    LoginManager.getInstance().logOut();
                }

                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();


        }

        return super.onOptionsItemSelected(item);
    }
    private boolean isFacebookLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
    }
    void getMyLocation() {
        try {
            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {

        }
        try {
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {

        }


        if (!isGpsEnable || !isNetworkEnable) {
            Toast.makeText(this, "Plz give Access to location... \nLogOut", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            GoogleSignInClient gcv= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
            gcv.revokeAccess();
            if(isFacebookLoggedIn()){
                LoginManager.getInstance().logOut();
            }

            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();

        }

        if (isGpsEnable) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.
                    requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }


        if (isNetworkEnable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }
    class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);



                geocoder = new Geocoder(MainActivity2.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address1 = addresses.get(0).getAddressLine(0);

                adress.setText(("Address: "+address1));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }




}