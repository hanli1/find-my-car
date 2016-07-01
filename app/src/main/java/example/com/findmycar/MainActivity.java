package example.com.findmycar;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        SelectionFragment.OnDataPass
{


    private GoogleApiClient googleApiClient;
    private LocationRequest requestCurrentLocation;

    private Location currentLocation;
    private Location parkingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_container, SelectionFragment.newInstance()).commit();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        requestCurrentLocation = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000);

    }
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        currentLocation = location;

        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        if(currentFrag instanceof SelectionFragment)
        {
            ((SelectionFragment) currentFrag).setCurrentStatus(location);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Toast.makeText(this, "successfully connected", Toast.LENGTH_LONG).show();
        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,  requestCurrentLocation, this);
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this, "connection failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setParkingLocation()
    {
        parkingLocation = currentLocation;
        System.out.println("Parkin location set");
    }
}
