package ldansorean.s6hikerswatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_UPDATE_MIN_TIME = 2000;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1;
    private static final String DETAILS_FORMAT = "Latitude: %.1f \n\r"
                                                + "Longitude: %.1f \n\r"
                                                + "Accuracy: %.1f m\n\r"
                                                + "Speed: %.1f m/s\n\r"
                                                + "Bearing: %.1f \n\r"
                                                + "Altitude: %.1f m\n\r"
                                                + "Address: %s \n\r";

    private TextView detailsView;
    private LocationManager locationManager;
    private String locationProvider;
    private Geocoder geocoder;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailsView = findViewById(R.id.details);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), true);
        locationManager.requestLocationUpdates(locationProvider, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        Location initialLocation = locationManager.getLastKnownLocation(locationProvider);
        showDetails(initialLocation);
    }

    private void showDetails(Location loc) {
        if (loc != null) {
            String address = "";
            try {
                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses != null & addresses.size() > 0) {
                    Log.i("address", addresses.get(0).getMaxAddressLineIndex() + "");
                    for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++)
                        address += addresses.get(0).getAddressLine(i) + "\n";
                }
            } catch (IOException e) {
                ; // not showing any address
            }
            String details = String.format(DETAILS_FORMAT, loc.getLatitude(), loc.getLongitude(), loc.getAccuracy(), loc.getSpeed(), loc.getBearing(), loc.getAltitude(), address);
            detailsView.setText(details);
        } else {
            detailsView.setText("");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
         showDetails(location);
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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(locationProvider, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
    }


}
