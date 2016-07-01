package example.com.findmycar;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SelectionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private LinearLayout saveSpot;
    private LinearLayout findCar;
    private TextView currentStatus;

    private GoogleApiClient googleApiClient;
    private LocationRequest requestCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        saveSpot = (LinearLayout) findViewById(R.id.activity_selection_save_spot);
        findCar = (LinearLayout) findViewById(R.id.activity_selection_find_car);
        currentStatus = (TextView) findViewById(R.id.activity_selection_current_status);

        saveSpot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("clicked!");

            }
        });

        findCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

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
        currentStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        currentStatus.setText("Latitude: " + location.getLatitude() +
                ", Logitude: " + location.getLongitude());
        System.out.println("Location changed");
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
}
